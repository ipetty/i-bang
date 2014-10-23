package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.model.Evaluation;
import net.ipetty.ibang.model.OffererInfo;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.SeekerInfo;
import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.repository.EvaluationDao;
import net.ipetty.ibang.vo.Constants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * EvaluationService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class EvaluationService extends BaseService {

	@Resource
	private EvaluationDao evaluationDao;

	@Resource
	private SeekService seekService;

	@Resource
	private OfferService offerService;

	@Resource
	private DelegationService delegationService;

	@Resource
	private UserService userService;

	@Resource
	private SeekerInfoService seekerInfoService;

	@Resource
	private OffererInfoService offererInfoService;

	@Resource
	private SystemMessageService systemMessageService;

	/**
	 * 评价
	 */
	public void evaluate(Evaluation evaluation) {
		Assert.notNull(evaluation, "评价不能为空");
		Assert.notNull(evaluation.getDelegationId(), "评价的委托单不能为空");
		Assert.notNull(evaluation.getType(), "评价类型不能为空");
		Assert.notNull(evaluation.getEvaluatorId(), "评价发起人不能为空");
		Assert.notNull(evaluation.getEvaluateTargetId(), "评价目标人不能为空");

		Delegation delegation = delegationService.getById(evaluation.getDelegationId());
		Assert.notNull(delegation, "评价的委托单不存在");

		Seek seek = seekService.getById(delegation.getSeekId());
		Assert.notNull(seek, "找不到对应的求助单");

		// 更新委托单状态
		if (Constants.DELEGATE_STATUS_FINISHED.equals(delegation.getStatus())) {
			if (Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER.equals(evaluation.getType())) {
				delegationService.seekerEvaluated(delegation.getId());
			} else if (Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER.equals(evaluation.getType())) {
				delegationService.offererEvaluated(delegation.getId());
			} else {
				throw new BusinessException("评价类型错误");
			}
		} else if (Constants.DELEGATE_STATUS_SEEKER_EVALUATED.equals(delegation.getStatus())) {
			if (Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER.equals(evaluation.getType())) {
				throw new BusinessException("您已评价，不能重复评价");
			} else if (Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER.equals(evaluation.getType())) {
				delegationService.biEvaluated(delegation.getId());
			} else {
				throw new BusinessException("评价类型错误");
			}
		} else if (Constants.DELEGATE_STATUS_OFFERER_EVALUATED.equals(delegation.getStatus())) {
			if (Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER.equals(evaluation.getType())) {
				delegationService.biEvaluated(delegation.getId());
			} else if (Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER.equals(evaluation.getType())) {
				throw new BusinessException("您已评价，不能重复评价");
			} else {
				throw new BusinessException("评价类型错误");
			}
		} else if (Constants.DELEGATE_STATUS_BI_EVALUATED.equals(delegation.getStatus())) {
			throw new BusinessException("双方已评价，不能重复评价");
		} else {
			// TODO 关闭的委托单能否评价？
			throw new BusinessException("委托单状态异常，不能评价");
		}

		// 保存评价
		evaluationDao.save(evaluation);

		// 更新积分与头衔
		if (evaluation.getPoint() != 0) {
			if (Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER.equals(evaluation.getType())) {
				OffererInfo offererInfo = offererInfoService.getByUserId(evaluation.getEvaluateTargetId());
				offererInfo.setTotalPoint(offererInfo.getTotalPoint() + evaluation.getPoint());
				offererInfo.setTitle(offererInfoService.getTitle(offererInfo.getTotalPoint()));
				offererInfoService.saveOrUpdate(offererInfo);
			} else if (Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER.equals(evaluation.getType())) {
				SeekerInfo seekerInfo = seekerInfoService.getByUserId(evaluation.getEvaluateTargetId());
				seekerInfo.setTotalPoint(seekerInfo.getTotalPoint() + evaluation.getPoint());
				seekerInfo.setTitle(seekerInfoService.getTitle(seekerInfo.getTotalPoint()));
				seekerInfoService.saveOrUpdate(seekerInfo);
			}
		}

		// 更新求助单状态
		List<Delegation> delegations = delegationService.listBySeekId(delegation.getSeekId());
		boolean seekFinished = true;
		if (seek.getDelegateNumber() > delegations.size()) {
			seekFinished = false;
		} else {
			int delegationNum = 0;
			for (Delegation item : delegations) {
				if (!Constants.DELEGATE_STATUS_BI_EVALUATED.equals(item.getStatus())
						&& !Constants.DELEGATE_STATUS_CLOSED.equals(item.getStatus())) {
					seekFinished = false;
					break;
				} else {
					delegationNum++;
				}
			}
			if (seek.getDelegateNumber() > delegationNum) {
				seekFinished = false;
			}
		}
		if (seekFinished) {
			seekService.finish(delegation.getSeekId());

			// 保存系统消息
			SystemMessage systemMessage = new SystemMessage(null, delegation.getSeekerId(),
					Constants.SYS_MSG_TYPE_SEEK_FINISHED, "您的一个求助单已成功完成。", seek.getContent());
			systemMessage.setSeekId(delegation.getSeekId());
			systemMessageService.save(systemMessage);
		}

		// 保存系统消息
		User evaluator = userService.getById(evaluation.getEvaluatorId());
		SystemMessage systemMessage = new SystemMessage(evaluation.getEvaluatorId(), evaluation.getEvaluateTargetId(),
				Constants.SYS_MSG_TYPE_NEW_EVALUATION, "您收到了来自" + evaluator.getNickname() + "的评价。",
				evaluation.getContent());
		systemMessage.setSeekId(delegation.getSeekId());
		systemMessage.setOfferId(delegation.getOfferId());
		systemMessage.setDelegationId(delegation.getId());
		systemMessage.setEvaluationId(evaluation.getId());
		systemMessageService.save(systemMessage);
	}

	/**
	 * 获取评价
	 */
	public Evaluation getById(Long id) {
		return evaluationDao.getById(id);
	}

	/**
	 * 获取委托对应的评价
	 */
	public List<Evaluation> listByDelegationId(Long delegationId) {
		List<Long> evaluationIds = evaluationDao.listByDelegationId(delegationId);
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		for (Long evaluationId : evaluationIds) {
			evaluations.add(evaluationDao.getById(evaluationId));
		}
		return evaluations;
	}

	/**
	 * 获取指定用户给出的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Evaluation> listByEvaluatorId(Integer userId, int pageNumber, int pageSize) {
		List<Long> evaluationIds = evaluationDao.listByEvaluatorId(userId, pageNumber, pageSize);
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		for (Long evaluationId : evaluationIds) {
			evaluations.add(evaluationDao.getById(evaluationId));
		}
		return evaluations;
	}

	/**
	 * 获取指定用户获得的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Evaluation> listByEvaluateTargetId(Integer userId, int pageNumber, int pageSize) {
		List<Long> evaluationIds = evaluationDao.listByEvaluateTargetId(userId, pageNumber, pageSize);
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		for (Long evaluationId : evaluationIds) {
			evaluations.add(evaluationDao.getById(evaluationId));
		}
		return evaluations;
	}

}
