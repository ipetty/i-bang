package net.ipetty.ibang.api;

import net.ipetty.ibang.api.util.ApiException;
import net.ipetty.ibang.api.util.IBangApiFactory;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserApiTest
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class UserApiTest extends BaseApiTest {

	private UserApi userApi = IBangApiFactory.create(UserApi.class);

	private static final String TEST_ACCOUNT_USERNAME = "ibang";
	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final String TEST_PASSWORD = TEST_ACCOUNT_PASSWORD;

	@Test
	public void testLogin() {
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testLoginWithEmail() {
		LoginResultVO result = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testRelogin() {
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		result = userApi.relogin(result.getUserToken(), result.getRefreshToken());
		logger.debug("relogin success, principal={}", result);
	}

	@Test
	public void testLogout() {
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		userApi.logout();
		logger.debug("logout success.");
	}

	@Test
	public void testRegister() {
		LoginResultVO result = userApi.register(new RegisterVO("testRegister", TEST_PASSWORD, "testRegister",
				"159*****757"));
		logger.debug("register success, principal={}", result);
	}

	@Test
	public void testCheckUsernameAvailable() {
		boolean usernameAvailable = userApi.checkUsernameAvailable(TEST_ACCOUNT_USERNAME);
		Assert.assertTrue(!usernameAvailable);

		usernameAvailable = userApi.checkUsernameAvailable("testCheckUsernameAvailable");
		Assert.assertTrue(usernameAvailable);
	}

	@Test
	public void testGetById() {
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		UserVO user = userApi.getById(result.getUserVo().getId());
		logger.debug("get by id success, user={}", user);
	}

	@Test
	public void testGetByIdFail() {
		try {
			userApi.getById(0);
		} catch (ApiException e) {
			Assert.assertTrue(true);
		}
	}

}
