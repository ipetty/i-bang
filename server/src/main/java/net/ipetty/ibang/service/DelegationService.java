package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.repository.DelegationDao;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.Constants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * DelegationService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class DelegationService extends BaseService {

	@Resource
	private DelegationDao delegationDao;

	@Resource
	private SeekService seekService;

	@Resource
	private OfferService offerService;

	@Resource
	private UserService userService;

	@Resource
	private SystemMessageService systemMessageService;

	/**
	 * 生成委托
	 */
	public void delegate(Delegation delegation) {
		Assert.notNull(delegation, "委托单不能为空");
		Assert.notNull(delegation.getSeekId(), "对应的求助单不能为空");
		Assert.notNull(delegation.getSeekerId(), "求助者不能为空");
		Assert.notNull(delegation.getOfferId(), "对应的应征单不能为空");
		Assert.notNull(delegation.getOffererId(), "应征者不能为空");

		Seek seek = seekService.getById(delegation.getSeekId());
		Assert.notNull(seek, "对应的求助单不存在");
		Offer offer = offerService.getById(delegation.getOfferId());
		Assert.notNull(offer, "对应的应征单不存在");

		// 生成序列号 TODO 生成日期数字的序列号
		delegation.setSn(UUIDUtils.generateShortUUID());

		// 状态
		delegation.setStatus(Constants.DELEGATE_STATUS_DELEGATED);

		// 保存委托单
		delegationDao.save(delegation);

		// 更新求助单与应征单的状态
		List<Long> delegationIds = delegationDao.listBySeekId(seek.getId());
		if (delegationIds != null && delegationIds.size() >= seek.getDelegateNumber()) {
			seekService.delegated(seek.getId());
		}
		offerService.delegated(offer.getId());

		// 保存系统消息
		User seeker = userService.getById(seek.getSeekerId());
		// TODO 这里系统消息的内容应该是什么？
		SystemMessage systemMessage = new SystemMessage(seeker.getId(), offer.getOffererId(),
				Constants.SYS_MSG_TYPE_NEW_DELEGATION, "您的应征已被" + seeker.getNickname() + "接受。", offer.getContent());
		systemMessage.setSeekId(seek.getId());
		systemMessage.setOfferId(offer.getId());
		systemMessage.setDelegationId(delegation.getId());
		systemMessageService.save(systemMessage);
	}

	/**
	 * 获取
	 */
	public Delegation getById(Long id) {
		return delegationDao.getById(id);
	}

	/**
	 * 获取指定用户的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Delegation> listByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> delegationIds = delegationDao.listByUserId(userId, pageNumber, pageSize);
		List<Delegation> delegations = new ArrayList<Delegation>();
		for (Long delegationId : delegationIds) {
			delegations.add(delegationDao.getById(delegationId));
		}
		return delegations;
	}

	/**
	 * 获取指定用户的指定状态的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Delegation> listByUserIdAndStatus(Integer userId, String status, int pageNumber, int pageSize) {
		List<Long> delegationIds = delegationDao.listByUserIdAndStatus(userId, status, pageNumber, pageSize);
		List<Delegation> delegations = new ArrayList<Delegation>();
		for (Long delegationId : delegationIds) {
			delegations.add(delegationDao.getById(delegationId));
		}
		return delegations;
	}

	/**
	 * 获取指定求助单的所有委托ID列表
	 */
	public List<Delegation> listBySeekId(Long seekId) {
		List<Long> delegationIds = delegationDao.listBySeekId(seekId);
		List<Delegation> delegations = new ArrayList<Delegation>();
		for (Long delegationId : delegationIds) {
			delegations.add(delegationDao.getById(delegationId));
		}
		return delegations;
	}

	/**
	 * （应征者）完成委托
	 */
	public void finish(Long delegationId) {
		delegationDao.updateStatus(delegationId, Constants.DELEGATE_STATUS_FINISHED);

		// 同时将应征单状态更改为已完成
		Delegation delegation = delegationDao.getById(delegationId);
		offerService.finish(delegation.getOfferId());

		// 保存系统消息
		Offer offer = offerService.getById(delegation.getOfferId());
		User offerer = userService.getById(delegation.getOffererId());
		SystemMessage systemMessage = new SystemMessage(offerer.getId(), delegation.getSeekerId(),
				Constants.SYS_MSG_TYPE_DELEGATION_FINISHED, "帮助者" + offerer.getNickname() + "已经完成您的委托。",
				offer.getContent());
		systemMessage.setSeekId(delegation.getSeekId());
		systemMessage.setOfferId(delegation.getOfferId());
		systemMessage.setDelegationId(delegation.getId());
		systemMessageService.save(systemMessage);
	}

	/**
	 * （应征者）关闭委托
	 */
	public void close(Long delegationId) {
		delegationDao.updateStatus(delegationId, Constants.DELEGATE_STATUS_CLOSED);

		// 同时将应征单状态更改为已关闭
		Delegation delegation = delegationDao.getById(delegationId);
		offerService.close(delegation.getOfferId());

		// 保存系统消息
		Offer offer = offerService.getById(delegation.getOfferId());
		User offerer = userService.getById(delegation.getOffererId());
		SystemMessage systemMessage = new SystemMessage(offerer.getId(), delegation.getSeekerId(),
				Constants.SYS_MSG_TYPE_DELEGATION_CLOSED, "帮助者" + offerer.getNickname() + "未能完成您的委托，该委托成功关闭。",
				offer.getContent());
		systemMessage.setSeekId(delegation.getSeekId());
		systemMessage.setOfferId(delegation.getOfferId());
		systemMessage.setDelegationId(delegation.getId());
		systemMessageService.save(systemMessage);
	}

	/**
	 * 更新委托单状态至求助者已评价状态
	 */
	public void seekerEvaluated(Long delegationId) {
		delegationDao.updateStatus(delegationId, Constants.DELEGATE_STATUS_SEEKER_EVALUATED);
	}

	/**
	 * 更新委托单状态至帮助者已评价状态
	 */
	public void offererEvaluated(Long delegationId) {
		delegationDao.updateStatus(delegationId, Constants.DELEGATE_STATUS_OFFERER_EVALUATED);
	}

	/**
	 * 更新委托单状态至双方已评价状态
	 */
	public void biEvaluated(Long delegationId) {
		delegationDao.updateStatus(delegationId, Constants.DELEGATE_STATUS_BI_EVALUATED);
	}

}
