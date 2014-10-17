package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * ImageApiTest
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class ImageApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private SeekApi seekApi = IbangApi.init().create(SeekApi.class);
	private ImageApi imageApi = IbangApi.init().create(ImageApi.class);

	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testAll() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO seeker = loginResult.getUserVo();

		SeekVO seek = new SeekVO();
		seek.setSeekerId(seeker.getId());
		seek.setCategoryL1("IT");
		seek.setCategoryL2("软件");
		seek.setContent("求开发一款类似陌陌的手机App");
		seek.setRequirement("要求支持短视频分享");
		seek.setReward("一个月内完成，两万；一个半月内完成，一万五。");

		ImageVO image = imageApi.upload(getTestPhoto());
		Assert.assertNotNull(image.getId());
		image = imageApi.getById(image.getId());
		Assert.assertNotNull(image);

		seek.getImages().add(image);
		seek = seekApi.publish(seek);
		Assert.assertNotNull(seek.getId());

		List<ImageVO> images = imageApi.listBySeekId(seek.getId());
		Assert.assertEquals(1, images.size());
	}

}
