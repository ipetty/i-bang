package net.ipetty.ibang.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * OfferApiTest
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class OfferApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private SeekApi seekApi = IbangApi.init().create(SeekApi.class);
	private OfferApi offerApi = IbangApi.init().create(OfferApi.class);

	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testAll() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO user = loginResult.getUserVo();

		SeekVO seek = new SeekVO();
		seek.setSeekerId(user.getId());
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

		OfferVO offer = new OfferVO();
		offer.setOffererId(user.getId());
		offer.setSeekId(seek.getId());
		offer.setContent("我做过，半个月能出产品。");
		offer.setDescription("我做过");
		offer.setDeadline(DateUtils.addDays(new Date(), 15));
		offer = offerApi.offer(offer);
		Assert.assertNotNull(offer.getId());
		Assert.assertEquals(Constants.OFFER_STATUS_OFFERED, offer.getStatus());
		seek = seekApi.getById(seek.getId());
		Assert.assertEquals(Constants.SEEK_STATUS_OFFERED, seek.getStatus());

		offer = offerApi.getById(offer.getId());
		Assert.assertEquals(seek.getId(), offer.getSeekId());

		List<OfferVO> offers = offerApi.listBySeekId(seek.getId());
		Assert.assertEquals(1, offers.size());

		offers = offerApi.listByUserId(user.getId(), 0, 20);
		Assert.assertEquals(1, offers.size());

		offerApi.close(offer.getId());
		offer = offerApi.getById(offer.getId());
		Assert.assertNotNull(offer.getClosedOn());

		// close seek
		seekApi.close(seek.getId());
	}

}
