package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.Constants;

import org.junit.Assert;
import org.junit.Test;

/**
 * SeekServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
public class SeekServiceTest extends BaseServiceTest {

	@Resource
	private SeekService seekService;

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
		Assert.assertEquals(Constants.SEEK_STATUS_CREATED, seek.getStatus());

		List<Long> seekIds = new ArrayList<Long>();
		seekIds.add(seek.getId());
		List<Seek> seeks = seekService.listByIds(seekIds);
		Assert.assertEquals(1, seeks.size());

		seek = seekService.getById(seek.getId());
		Assert.assertEquals(2, seek.getImages().size());

		seeks = seekService.listLatest(new Date(), 0, 20);
		Assert.assertEquals(1, seeks.size());

		seeks = seekService.listByUserId(user.getId(), 0, 20);
		Assert.assertEquals(1, seeks.size());

		seekService.close(seek.getId());
		seeks = seekService.listLatest(new Date(), 0, 20);
		Assert.assertEquals(0, seeks.size());

		seekService.listLatestByCategory(null, null, new Date(), 0, 20);
		seekService.listLatestByCategory("categoryL1", null, new Date(), 0, 20);
		seekService.listLatestByCategory("categoryL1", "categoryL2", new Date(), 0, 20);

		seekService.listLatestByCityOrCategory(null, null, null, null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory(null, null, "categoryL1", null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory(null, null, "categoryL1", "categoryL2", new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", null, null, null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", null, "categoryL1", null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", null, "categoryL1", "categoryL2", new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", "district", null, null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", "district", "categoryL1", null, new Date(), 0, 20);
		seekService.listLatestByCityOrCategory("city", "district", "categoryL1", "categoryL2", new Date(), 0, 20);
	}

}
