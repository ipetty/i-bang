package net.ipetty.ibang.service;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.ipetty.ibang.model.SeekerInfo;
import net.ipetty.ibang.model.User;

import org.junit.Test;

/**
 * SeekerInfoServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
public class SeekerInfoServiceTest extends BaseServiceTest {

	@Resource
	private SeekerInfoService seekerInfoService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";

	@Test
	public void testAll() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		SeekerInfo seekerInfo = new SeekerInfo(user.getId(), 0, 0, "no title");
		seekerInfoService.saveOrUpdate(seekerInfo);

		seekerInfo = seekerInfoService.getByUserId(user.getId());
		Assert.assertEquals(0, seekerInfo.getSeekCount());

		seekerInfo = new SeekerInfo(user.getId(), 5, 30, "领主");
		seekerInfoService.saveOrUpdate(seekerInfo);

		seekerInfo = seekerInfoService.getByUserId(user.getId());
		Assert.assertEquals(5, seekerInfo.getSeekCount());
	}

}
