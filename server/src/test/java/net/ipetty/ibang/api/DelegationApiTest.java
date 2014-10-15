package net.ipetty.ibang.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * DelegationApiTest
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class DelegationApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private SeekApi seekApi = IbangApi.init().create(SeekApi.class);
	private OfferApi offerApi = IbangApi.init().create(OfferApi.class);
	private DelegationApi delegationApi = IbangApi.init().create(DelegationApi.class);

	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final String TEST_OFFERER_ACCOUNT = "testoffer";
	private static final String TEST_OFFERER_PASSWORD = TEST_ACCOUNT_PASSWORD;

	@Test
	public void testAll() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO seeker = loginResult.getUserVo();

		RegisterVO register = new RegisterVO();
		register.setUsername(TEST_OFFERER_ACCOUNT);
		register.setPassword(TEST_OFFERER_PASSWORD);
		LoginResultVO offererLoginResult = userApi.register(register);
		UserVO offerer = offererLoginResult.getUserVo();
		Assert.assertNotNull(offerer.getId());

		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		SeekVO seek = new SeekVO();
		seek.setSeekerId(seeker.getId());
		seek.setCategoryL1("IT");
		seek.setCategoryL2("软件");
		seek.setContent("求开发一款类似陌陌的手机App");
		List<ImageVO> images = new ArrayList<ImageVO>();
		images.add(new ImageVO(null, null, "small_url", "original_url"));
		images.add(new ImageVO(null, null, "small_url2", "original_url2"));
		seek.setImages(images);
		seek.setRequirement("要求支持短视频分享");
		seek.setReward("一个月内完成，两万；一个半月内完成，一万五。");

		seek = seekApi.publish(seek);
		Assert.assertNotNull(seek.getId());

		userApi.login(TEST_OFFERER_ACCOUNT, TEST_OFFERER_PASSWORD);
		OfferVO offer = new OfferVO();
		offer.setOffererId(offerer.getId());
		offer.setSeekId(seek.getId());
		offer.setContent("我做过，半个月能出产品。");
		offer.setDescription("我做过");
		offer.setDeadline(DateUtils.addDays(new Date(), 15));
		offer = offerApi.offer(offer);
		Assert.assertNotNull(offer.getId());

		offer = offerApi.getById(offer.getId());
		Assert.assertEquals(seek.getId(), offer.getSeekId());

		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		DelegationVO delegation = new DelegationVO();
		delegation.setSeekId(seek.getId());
		delegation.setSeekerId(seek.getSeekerId());
		delegation.setOfferId(offer.getId());
		delegation.setOffererId(offer.getOffererId());
		delegation.setDeadline(offer.getDeadline());
		delegation = delegationApi.delegate(delegation);
		Assert.assertNotNull(delegation.getId());
		Assert.assertEquals(Constants.DELEGATE_STATUS_DELEGATED, delegation.getStatus());

		List<DelegationVO> delegations = delegationApi.listByUserId(seeker.getId(), 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationApi.listByUserId(offerer.getId(), 0, 20);
		Assert.assertEquals(1, delegations.size());

		delegations = delegationApi.listByUserIdAndStatus(seeker.getId(), Constants.DELEGATE_STATUS_DELEGATED, 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationApi.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_DELEGATED, 0, 20);
		Assert.assertEquals(1, delegations.size());

		delegations = delegationApi
				.listByUserIdAndStatus(seeker.getId(), Constants.DELEGATE_STATUS_BI_EVALUATED, 0, 20);
		Assert.assertEquals(0, delegations.size());
		delegations = delegationApi.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_BI_EVALUATED, 0,
				20);
		Assert.assertEquals(0, delegations.size());

		delegationApi.finish(delegation.getId());
		delegations = delegationApi.listByUserIdAndStatus(seeker.getId(), Constants.DELEGATE_STATUS_FINISHED, 0, 20);
		Assert.assertEquals(1, delegations.size());
		delegations = delegationApi.listByUserIdAndStatus(offerer.getId(), Constants.DELEGATE_STATUS_FINISHED, 0, 20);
		Assert.assertEquals(1, delegations.size());
	}

}
