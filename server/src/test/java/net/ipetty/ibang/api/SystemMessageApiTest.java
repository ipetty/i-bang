package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.SystemMessageVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * SystemMessageApiTest
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class SystemMessageApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private SystemMessageApi systemMessageApi = IbangApi.init().create(SystemMessageApi.class);

	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testAll() {
		LoginResultVO loginResult = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		UserVO user = loginResult.getUserVo();

		List<SystemMessageVO> systemMessages = systemMessageApi.listUnreadByUserId(user.getId(), 0, 20);
		Assert.assertTrue(systemMessages.size() >= 0);

		systemMessages = systemMessageApi.listByUserId(user.getId(), 0, 20);
		Assert.assertTrue(systemMessages.size() >= 0);
	}

}
