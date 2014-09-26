package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.Constants;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * OfferServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
public class OfferServiceTest extends BaseServiceTest {

	@Resource
	private SeekService seekService;

	@Resource
	private OfferService offerService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";

	@Test
	public void testAll() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);

		Seek seek = new Seek();
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
		offer.setOffererId(user.getId());
		offer.setSeekId(seek.getId());
		offer.setContent("我做过，半个月能出产品。");
		offer.setDescription("我做过");
		offer.setDeadline(DateUtils.addDays(new Date(), 15));
		offerService.offer(offer);
		Assert.assertNotNull(offer.getId());
		Assert.assertEquals(Constants.OFFER_STATUS_OFFERED, offer.getStatus());
		seek = seekService.getById(seek.getId());
		Assert.assertEquals(Constants.SEEK_STATUS_OFFERED, seek.getStatus());

		offer = offerService.getById(offer.getId());
		Assert.assertEquals(seek.getId(), offer.getSeekId());

		List<Offer> offers = offerService.listBySeekId(seek.getId(), 0, 20);
		Assert.assertEquals(1, offers.size());

		offers = offerService.listByUserId(user.getId(), 0, 20);
		Assert.assertEquals(1, offers.size());

		offerService.close(offer.getId());
		offer = offerService.getById(offer.getId());
		Assert.assertNotNull(offer.getClosedOn());
	}

}
