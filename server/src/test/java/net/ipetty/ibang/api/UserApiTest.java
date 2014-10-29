package net.ipetty.ibang.api;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.api.exception.ApiException;
import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserOfferRange;
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

	private UserApi userApi = IbangApi.init().getUserApi();

	private static final String TEST_ACCOUNT_USERNAME = "ibang";
	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final String TEST_PASSWORD = TEST_ACCOUNT_PASSWORD;

	@Test
	public void testLogin() {
		userApi.logout();
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testLoginWithEmail() {
		userApi.logout();
		LoginResultVO result = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		logger.debug("login success, principal={}", result);
	}

	@Test
	public void testRelogin() {
		userApi.logout();
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		result = userApi.relogin(result.getUserToken(), result.getRefreshToken());

		logger.debug("relogin success, principal={}", result);
	}

	@Test
	public void testLogout() {
		userApi.logout();
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		userApi.logout();

		logger.debug("logout success.");
	}

	@Test
	public void testRegister() {
		userApi.logout();
		LoginResultVO result = userApi.register(new RegisterVO("testRegister", TEST_PASSWORD, "昵称", "159*****757"));

		logger.debug("register success, principal={}", result);
	}

	@Test
	public void testCheckUsernameAvailable() {
		userApi.logout();
		boolean usernameAvailable = userApi.checkUsernameAvailable(TEST_ACCOUNT_USERNAME);
		Assert.assertTrue(!usernameAvailable);

		usernameAvailable = userApi.checkUsernameAvailable("testCheckUsernameAvailable");
		Assert.assertTrue(usernameAvailable);
	}

	@Test
	public void testGetById() {
		userApi.logout();
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		UserVO user = userApi.getById(result.getUserVo().getId());
		logger.debug("get by id success, user={}", user);
	}

	@Test
	public void testGetByIdFail() {
		userApi.logout();
		try {
			userApi.getById(0);
		} catch (ApiException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetByUsername() {
		userApi.logout();
		UserVO user = userApi.getByUsername(TEST_ACCOUNT_USERNAME);
		logger.debug("get by username success, user={}", user);
	}

	@Test
	public void testGetByUsernameFail() {
		userApi.logout();
		try {
			userApi.getByUsername("testGetByUsernameFail");
		} catch (ApiException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testChangePassword() {
		userApi.logout();
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		Assert.assertTrue(userApi.changePassword(TEST_ACCOUNT_PASSWORD, "666666"));

		userApi.login(TEST_ACCOUNT_USERNAME, "666666");
		Assert.assertTrue(userApi.changePassword("666666", TEST_ACCOUNT_PASSWORD));
		logger.debug("change password success.");
	}

	@Test
	public void testChangePasswordWithoutLogin() {
		userApi.logout();
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		Assert.assertTrue(userApi.logout());
		try {
			userApi.changePassword(TEST_ACCOUNT_PASSWORD, "666666");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testChangePasswordFail() {
		userApi.logout();
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		try {
			userApi.changePassword("666666", TEST_ACCOUNT_PASSWORD);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testUpdateAvatar() {
		userApi.logout();
		userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		UserVO user = userApi.updateAvatar(getTestPhoto());
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getAvatar());
	}

	@Test
	public void testUpdate() {
		userApi.logout();
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);

		UserVO user = result.getUserVo();
		UserFormVO userFormVO = new UserFormVO();
		userFormVO.setId(user.getId());
		userFormVO.setNickname(user.getNickname());
		userFormVO.setGender(user.getGender());
		userFormVO.setPhone(user.getPhone());
		userFormVO.setTelephone(user.getTelephone());
		userFormVO.setSignature(user.getSignature());
		userFormVO.setJob("系统架构师");
		userFormVO.setAddress(user.getAddress());
		user = userApi.update(userFormVO);
		Assert.assertNotNull(user.getJob());
	}

	@Test
	public void testUpdateOfferRange() {
		LoginResultVO result = userApi.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		List<SeekCategory> offerRange = new ArrayList<SeekCategory>();
		offerRange.add(new SeekCategory("家政服务", "保洁"));
		offerRange.add(new SeekCategory("IT服务", "电脑软件"));
		UserVO user = userApi.updateOfferRange(new UserOfferRange(result.getUserVo().getId(), offerRange));
		logger.debug("updated offer range = {}", user);
	}

}
