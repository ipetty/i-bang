package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.util.SaltEncoder;
import net.ipetty.ibang.vo.SeekCategory;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public class UserServiceTest extends BaseServiceTest {

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_USERNAME = "ibang";
	private static final String TEST_ACCOUNT_EMAIL = "ibang@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final String TEST_PASSWORD = TEST_ACCOUNT_PASSWORD;

	@Test
	public void testLogin() {
		User user = userService.login(TEST_ACCOUNT_USERNAME, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		Assert.assertEquals(SaltEncoder.encode(TEST_ACCOUNT_PASSWORD, user.getSalt()), user.getPassword());
		logger.debug("userVo is {}", user.toVO());
	}

	@Test
	public void testLoginFail() {
		try {
			userService.login("testnonexistsusername", TEST_ACCOUNT_PASSWORD);
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testRegisterByUsername() {
		User user = new User();
		String username = "testusername";
		user.setUsername(username);
		user.setPassword(TEST_PASSWORD);
		userService.register(user);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getCreatedOn());

		User result = userService.getByLoginName(username);
		Assert.assertNotNull(result);
		Assert.assertEquals(user.getPassword(), result.getPassword());
	}

	@Test
	public void testRegisterByEmail() {
		User user = new User();
		String email = "testusername@ipetty.net";
		user.setEmail(email);
		user.setPassword(TEST_PASSWORD);
		userService.register(user);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getCreatedOn());

		User result = userService.getByLoginName(email);
		Assert.assertNotNull(result);
		Assert.assertEquals(user.getPassword(), result.getPassword());
	}

	@Test
	public void testRegisterFail() {
		try {
			User user = new User();
			user.setUsername(TEST_ACCOUNT_USERNAME);
			user.setPassword(TEST_ACCOUNT_PASSWORD);
			userService.register(user);
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetByUsername() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST_ACCOUNT_USERNAME, user.getUsername());
		Assert.assertEquals(TEST_ACCOUNT_EMAIL, user.getEmail());
	}

	@Test
	public void testGetByEmail() {
		User user = userService.getByEmail(TEST_ACCOUNT_EMAIL);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST_ACCOUNT_USERNAME, user.getUsername());
		Assert.assertEquals(TEST_ACCOUNT_EMAIL, user.getEmail());
	}

	@Test
	public void testUpdateEmail() {
		User user = new User();
		String email = "testupdateemail@ipetty.net";
		user.setEmail(email);
		user.setPassword(TEST_PASSWORD);
		userService.register(user);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getCreatedOn());

		String updatedEmail = "updatedemail@ipetty.net";
		userService.updateEmail(user.getId(), updatedEmail);

		user = userService.getById(user.getId());
		Assert.assertEquals(updatedEmail, user.getEmail());
	}

	@Test
	public void testChangePassword() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		String updatedPassword = TEST_PASSWORD + "2";
		userService.changePassword(user.getId(), TEST_PASSWORD, updatedPassword);
		user = userService.getById(user.getId());
		Assert.assertEquals(SaltEncoder.encode(updatedPassword, user.getSalt()), user.getPassword());
		userService.changePassword(user.getId(), updatedPassword, TEST_PASSWORD);
	}

	@Test
	public void testUpdateProfile() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		String udpatedNickname = "爱帮";
		user.setNickname(udpatedNickname);
		userService.updateProfile(user);
		user = userService.getById(user.getId());
		Assert.assertEquals(udpatedNickname, user.getNickname());
	}

	@Test
	public void testUpdateOfferRange() {
		User user = userService.getByUsername(TEST_ACCOUNT_USERNAME);
		List<SeekCategory> offerRange = new ArrayList<SeekCategory>();
		offerRange.add(new SeekCategory("家政服务", "保洁"));
		userService.updateOfferRange(user.getId(), offerRange);
	}

}
