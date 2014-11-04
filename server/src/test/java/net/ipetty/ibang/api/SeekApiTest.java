package net.ipetty.ibang.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.util.DateUtils;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.LocationVO;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.SeekWithLocationVO;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * SeekApiTest
 * 
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class SeekApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private SeekApi seekApi = IbangApi.init().create(SeekApi.class);

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
		Assert.assertEquals(Constants.SEEK_STATUS_CREATED, seek.getStatus());

		List<SeekVO> seeks = seekApi.listByIds(seek.getId());
		Assert.assertEquals(1, seeks.size());

		seek = seekApi.getById(seek.getId());
		Assert.assertEquals(2, seek.getImages().size());

		seeks = seekApi.listLatest(DateUtils.toDatetimeString(new Date()), 0, 20);
		Assert.assertEquals(1, seeks.size());

		seeks = seekApi.listByUserId(user.getId(), 0, 20);
		Assert.assertEquals(1, seeks.size());

		Assert.assertTrue(seekApi.close(seek.getId()));
		seeks = seekApi.listLatest(DateUtils.toDatetimeString(new Date()), 0, 20);
		Assert.assertEquals(0, seeks.size());

		seek = new SeekVO();
		seek.setSeekerId(user.getId());
		seek.setCategoryL1("IT服务");
		seek.setCategoryL2("电脑软件");
		seek.setContent("求开发一款类似陌陌的手机App");
		images = new ArrayList<ImageVO>();
		images.add(new ImageVO(null, null, "small_url", "original_url"));
		images.add(new ImageVO(null, null, "small_url2", "original_url2"));
		seek.setImages(images);
		seek.setRequirement("要求支持短视频分享");
		seek.setReward("一个月内完成，两万；一个半月内完成，一万五。");
		seekApi.publish(seek);

		List<SeekCategory> offerRange = new ArrayList<SeekCategory>();
		offerRange.add(new SeekCategory("家政服务", "保洁"));
		offerRange.add(new SeekCategory("IT服务", "电脑软件"));
		userApi.updateOfferRange(new UserOfferRange(user.getId(), offerRange));
		seeks = seekApi.listLatestByCityAndOfferRange(null, null, user.getId(), DateUtils.toDatetimeString(new Date()),
				0, 20);
		seeks = seekApi.listLatestByCityAndOfferRange("city", null, user.getId(),
				DateUtils.toDatetimeString(new Date()), 0, 20);
		seeks = seekApi.listLatestByCityAndOfferRange("city", "district", user.getId(),
				DateUtils.toDatetimeString(new Date()), 0, 20);
	}

	@Test
	public void testPublishWithLocation() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO user = loginResult.getUserVo();
		SeekWithLocationVO seek = new SeekWithLocationVO();
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

		LocationVO location = new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true);
		seek.setLocation(location);

		SeekVO s = seekApi.publish(seek);
		Assert.assertNotNull(s.getId());
		Assert.assertEquals(Constants.SEEK_STATUS_CREATED, s.getStatus());
	}

	@Test
	public void testPublishAssistance() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO user = loginResult.getUserVo();
		SeekWithLocationVO seek = new SeekWithLocationVO();
		seek.setSeekerId(user.getId());
		seek.setCategoryL1("IT");
		seek.setCategoryL2("软件");
		seek.setContent("我有大量业余时间，可以提供手机App的开发服务。");
		List<ImageVO> images = new ArrayList<ImageVO>();
		images.add(new ImageVO(null, null, "small_url", "original_url"));
		images.add(new ImageVO(null, null, "small_url2", "original_url2"));
		seek.setImages(images);
		seek.setRequirement("需要100元/小时以上的报酬");
		seek.setReward("100-500元/小时");

		LocationVO location = new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true);
		seek.setLocation(location);

		SeekVO s = seekApi.publishAssistance(seek);
		Assert.assertNotNull(s.getId());
		Assert.assertEquals(Constants.SEEK_STATUS_CREATED, s.getStatus());
	}

}
