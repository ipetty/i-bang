package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.ipetty.ibang.model.OffererInfo;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.SeekCategory;

import org.junit.Test;

/**
 * OffererInfoServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
public class OffererInfoServiceTest extends BaseServiceTest {

	@Resource
	private OffererInfoService offererInfoService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";

	@Test
	public void testAll() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		OffererInfo offererInfo = new OffererInfo(user.getId(), 0, 0, "no title");
		offererInfoService.saveOrUpdate(offererInfo);

		offererInfo = offererInfoService.getByUserId(user.getId());
		Assert.assertEquals(0, offererInfo.getOfferCount());

		List<SeekCategory> offerRange = new ArrayList<SeekCategory>();
		offerRange.add(new SeekCategory("IT服务", null));
		offerRange.add(new SeekCategory("教育培训", "其他"));
		offererInfo = new OffererInfo(user.getId(), 5, 30, "领主", offerRange);
		offererInfoService.saveOrUpdate(offererInfo);

		offererInfo = offererInfoService.getByUserId(user.getId());
		Assert.assertEquals(5, offererInfo.getOfferCount());
		Assert.assertEquals(2, offererInfo.getOfferRange().size());
	}

}
