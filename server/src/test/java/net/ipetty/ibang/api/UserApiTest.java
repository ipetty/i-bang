package net.ipetty.ibang.api;

import net.ipetty.ibang.api.context.ApiContext;
import net.ipetty.ibang.api.exception.ApiException;
import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserApiTest<br />
 * 由于Retrofit 没法进行Post 拦截进行ApiContext 操作，故这里进行了一下ApiContext 的操作；<br />
 * 实际客户端在调用Api 以后也要进行这些ApiContext 的操作
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class UserApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().create(UserApi.class);

	private static final String TEST_ACCOUNT_USERNAME = "ibang";
	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final String TEST_PASSWORD = TEST_ACCOUNT_PASSWORD;

	@Test
	public void testLogin() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testLoginWithEmail() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testRelogin() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());
		result = userApi.relogin(result.getUserToken(), result.getRefreshToken());
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		logger.debug("relogin success, principal={}", result);
	}

	@Test
	public void testLogout() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		userApi.logout();
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);

		logger.debug("logout success.");
	}

	@Test
	public void testRegister() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.register(new RegisterVO("testRegister", TEST_PASSWORD, "昵称", "159*****757"));
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		logger.debug("register success, principal={}", result);
	}

	@Test
	public void testCheckUsernameAvailable() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		boolean usernameAvailable = userApi.checkUsernameAvailable(TEST_ACCOUNT_USERNAME);
		Assert.assertTrue(!usernameAvailable);

		usernameAvailable = userApi.checkUsernameAvailable("testCheckUsernameAvailable");
		Assert.assertTrue(usernameAvailable);
	}

	@Test
	public void testGetById() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		UserVO user = userApi.getById(result.getUserVo().getId());
		logger.debug("get by id success, user={}", user);
	}

	@Test
	public void testGetByIdFail() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		try {
			userApi.getById(0);
		} catch (ApiException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetByUsername() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		UserVO user = userApi.getByUsername(TEST_ACCOUNT_USERNAME);
		logger.debug("get by username success, user={}", user);
	}

	@Test
	public void testGetByUsernameFail() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		try {
			userApi.getByUsername("testGetByUsernameFail");
		} catch (ApiException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testChangePassword() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		Assert.assertTrue(userApi.changePassword(TEST_ACCOUNT_PASSWORD, "666666"));
		Assert.assertTrue(userApi.changePassword("666666", TEST_ACCOUNT_PASSWORD));
		logger.debug("change password success.");
	}

	@Test
	public void testChangePasswordWithoutLogin() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		Assert.assertTrue(userApi.logout());
		try {
			userApi.changePassword(TEST_ACCOUNT_PASSWORD, "666666");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testChangePasswordFail() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		try {
			userApi.changePassword("666666", TEST_ACCOUNT_PASSWORD);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testUpdateAvatar() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		UserVO user = userApi.updateAvatar(getTestPhoto());
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getAvatar());
	}

	@Test
	public void testUpdate() {
		ApiContext.getInstance().setUserToken(null);
		ApiContext.getInstance().setRefreshToken(null);
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());

		UserVO user = result.getUserVo();
		UserFormVO userFormVO = new UserFormVO();
		userFormVO.setId(user.getId());
		userFormVO.setJob("系统架构师");
		user = userApi.update(userFormVO);
		Assert.assertNotNull(user.getJob());
	}

}
