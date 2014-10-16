package net.ipetty.ibang.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.service.DelegationService;
import net.ipetty.ibang.service.OfferService;
import net.ipetty.ibang.service.SeekService;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * DelegationController
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
@Controller
public class DelegationController extends BaseController {

	@Resource
	private DelegationService delegationService;

	@Resource
	private SeekService seekService;

	@Resource
	private OfferService offerService;

	/**
	 * 接受应征
	 */
	@RequestMapping(value = "/delegate", method = RequestMethod.POST)
	public DelegationVO delegate(@RequestBody DelegationVO delegation) {
		Assert.notNull(delegation, "委托单不能为空");
		Assert.notNull(delegation.getSeekId(), "对应的求助单不能为空");
		Assert.notNull(delegation.getOfferId(), "对应的应征单不能为空");

		Seek seek = seekService.getById(delegation.getSeekId());
		Assert.notNull(seek, "找不到对应的求助单");
		delegation.setSeekerId(seek.getSeekerId());
		Offer offer = offerService.getById(delegation.getOfferId());
		Assert.notNull(offer, "找不到对应的应征单");
		delegation.setOffererId(offer.getOffererId());
		if (delegation.getDeadline() == null) {
			delegation.setDeadline(offer.getDeadline());
		}

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能接受应征");
		}
		if (!currentUser.getId().equals(seek.getSeekerId())) {
			throw new RestException("求助发起人才能接受应征");
		}

		Delegation d = Delegation.fromVO(delegation);
		delegationService.delegate(d);
		d = delegationService.getById(d.getId());

		return d.toVO();
	}

	/**
	 * 获取
	 */
	@RequestMapping(value = "/delegation", method = RequestMethod.GET)
	public DelegationVO getById(Long id) {
		Assert.notNull(id, "委托单ID不能为空");

		return delegationService.getById(id).toVO();
	}

	/**
	 * 根据应征单ID获取委托单，如没有对应的委托则返回null
	 */
	@RequestMapping(value = "/delegation/byoffer", method = RequestMethod.GET)
	public DelegationVO getByOfferId(Long offerId) {
		Assert.notNull(offerId, "委托单ID不能为空");

		Delegation delegation = delegationService.getByOfferId(offerId);
		return delegation == null ? null : delegation.toVO();
	}

	/**
	 * 获取指定用户的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/delegationlist/byuser", method = RequestMethod.GET)
	public List<DelegationVO> listByUserId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Delegation> delegations = delegationService.listByUserId(userId, pageNumber, pageSize);
		return Delegation.listToVoList(delegations);
	}

	/**
	 * 获取指定用户的指定状态的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/delegationlist/byuserandstatus", method = RequestMethod.GET)
	public List<DelegationVO> listByUserIdAndStatus(Integer userId, String status, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Delegation> delegations = delegationService.listByUserIdAndStatus(userId, status, pageNumber, pageSize);
		return Delegation.listToVoList(delegations);
	}

	/**
	 * 获取指定求助单的所有委托ID列表
	 */
	@RequestMapping(value = "/delegationlist/byseek", method = RequestMethod.GET)
	public List<DelegationVO> listBySeekId(Long seekId) {
		Assert.notNull(seekId, "求助单ID不能为空");

		List<Delegation> delegations = delegationService.listBySeekId(seekId);
		return Delegation.listToVoList(delegations);
	}

	/**
	 * 完成委托
	 */
	@RequestMapping(value = "/delegation/finish", method = RequestMethod.POST)
	public boolean finish(Long delegationId) {
		Assert.notNull(delegationId, "委托单ID不能为空");

		delegationService.finish(delegationId);
		return true;
	}

	/**
	 * 关闭委托
	 */
	@RequestMapping(value = "/delegation/close", method = RequestMethod.POST)
	public boolean close(Long delegationId) {
		Assert.notNull(delegationId, "委托单ID不能为空");

		delegationService.close(delegationId);
		return true;
	}

}
