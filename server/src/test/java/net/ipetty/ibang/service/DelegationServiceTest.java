package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.Constants;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * DelegationServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
public class DelegationServiceTest extends BaseServiceTest {

	@Resource
	private DelegationService delegationService;

	@Resource
	private SeekService seekService;

	@Resource
	private OfferService offerService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";
	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);

		User offerer = new User();
		String username = "testoffer";
		offerer.setUsername(username);
		offerer.setPassword(TEST_PASSWORD);
		userService.register(offerer);
		Assert.assertNotNull(offerer.getId());

		Seek seek = new Seek();
		seek.setType(Constants.SEEK_TYPE_SEEK);
		seek.setSeekerId(user.getId());
		seek.setCategoryL1("IT");
		seek.setCategoryL2("软件");
		seek.setContent("求开发一款类似陌陌的手机App");
		List<Image> images = new ArrayList<Image>();
		images.add(new Image(null, null, "small_url", "original_url"));
		images.add(new Image(null, null, "small_url2", "original_url2"));
		seek.setImages(images);
		seek.setRequirement("要求支持短视频分享");
		seek.setReward("一个月内完成，两万；一个半月内完成，一万五。");

		seekService.publish(seek);
		Assert.assertNotNull(seek.getId());

		Offer offer = new Offer();
		offer.setOffererId(offerer.getId());
		offer.setSeekId(seek.getId());
		offer.setContent("我做过，半个月能出产品。");
		offer.setDescription("我做过");
		offer.setDeadline(DateUtils.addDays(new Date(), 15));
		offerService.offer(offer);
		Assert.assertNotNull(offer.getId());

		offer = offerService.getById(offer.getId());
		Assert.assertEquals(seek.getId(), offer.getSeekId());

		Delegation delegation = new Delegation();
		delegation.setSeekId(seek.getId());
		delegation.setSeekerId(seek.getSeekerId());
		delegation.setOfferId(offer.getId());
		delegation.setOffererId(offer.getOffererId());
		delegation.setDeadline(offer.getDeadline());
		delegationService.delegate(delegation);
		Assert.assertNotNull(delegation.getId());
		Assert.assertEquals(Constants.DELEGATE_STATUS_DELEGATED, delegation.getStatus());

		delegation = delegationService.getById(delegation.getId());
		Assert.assertNotNull(delegation);
		delegation = delegationService.getByOfferId(delegation.getOfferId());
		Assert.assertNotNull(delegation);

		List<Delegation> delegations = delegationService.listByUserId(user.getId(), 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationService.listByUserId(offerer.getId(), 0, 20);
		Assert.assertEquals(1, delegations.size());

		delegations = delegationService.listByUserIdAndStatus(user.getId(), Constants.DELEGATE_STATUS_DELEGATED, 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationService.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_DELEGATED, 0,
				20);
		Assert.assertEquals(1, delegations.size());

		delegations = delegationService.listByUserIdAndStatus(user.getId(), Constants.DELEGATE_STATUS_BI_EVALUATED, 0,
				20);
		Assert.assertEquals(0, delegations.size());
		delegations = delegationService.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_BI_EVALUATED,
				0, 20);
		Assert.assertEquals(0, delegations.size());

		delegationService.finish(delegation.getId());
		delegations = delegationService.listByUserIdAndStatus(user.getId(), Constants.DELEGATE_STATUS_FINISHED, 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationService.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_FINISHED, 0,
				20);
		Assert.assertEquals(1, delegations.size());
	}

}
