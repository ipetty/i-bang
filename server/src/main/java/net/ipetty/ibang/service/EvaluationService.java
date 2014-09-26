package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.model.Evaluation;
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
	private DelegationService delegationService;

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

		// 保持评价
		evaluationDao.save(evaluation);

		// 更新求助单状态
		List<Delegation> delegations = delegationService.listBySeekId(delegation.getSeekId());
		boolean seekFinished = true;
		for (Delegation item : delegations) {
			if (!Constants.DELEGATE_STATUS_BI_EVALUATED.equals(item.getStatus())
					&& !Constants.DELEGATE_STATUS_CLOSED.equals(item.getStatus())) {
				seekFinished = false;
				break;
			}
		}
		if (seekFinished) {
			seekService.finish(delegation.getSeekId());
		}
	}

	/**
	 * 获取评价
	 */
	public Evaluation getById(Long id) {
		return evaluationDao.getById(id);
	}

	/**
	 * 获取指定用户给出的评价列表
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
