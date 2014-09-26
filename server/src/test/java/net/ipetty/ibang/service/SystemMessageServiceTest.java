package net.ipetty.ibang.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.Constants;

import org.junit.Assert;
import org.junit.Test;

/**
 * SystemMessageServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月26日
 */
public class SystemMessageServiceTest extends BaseServiceTest {

	@Resource
	private SystemMessageService systemMessageService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";

	@Test
	public void testAll() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		SystemMessage systemMessage = new SystemMessage();
		systemMessage.setReceiverId(user.getId());
		systemMessage.setType(Constants.SYS_MSG_TYPE_NEW_OFFER);
		systemMessageService.save(systemMessage);
		Assert.assertNotNull(systemMessage.getId());

		systemMessage = systemMessageService.getById(systemMessage.getId());
		Assert.assertEquals(Constants.SYS_MSG_TYPE_NEW_OFFER, systemMessage.getType());

		List<SystemMessage> systemMessages = systemMessageService.listUnreadByUserId(user.getId(), 0, 20);
		Assert.assertTrue(systemMessages.size() > 0);

		systemMessageService.read(systemMessage.getId());
		systemMessage = systemMessageService.getById(systemMessage.getId());
		Assert.assertTrue(systemMessage.isRead());

		systemMessages = systemMessageService.listByUserId(user.getId(), 0, 20);
		Assert.assertTrue(systemMessages.size() > 0);
	}

}
