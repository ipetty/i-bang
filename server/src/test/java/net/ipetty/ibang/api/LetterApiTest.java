package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.LetterContacts;
import net.ipetty.ibang.vo.LetterVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * LetterApiTest
 * @author luocanfeng
 * @date 2014年11月11日
 */
public class LetterApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private LetterApi letterApi = IbangApi.init().create(LetterApi.class);

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		RegisterVO registerA = new RegisterVO();
		registerA.setUsername("user_la");
		registerA.setPassword(TEST_PASSWORD);
		UserVO userA = userApi.register(registerA).getUserVo();
		RegisterVO registerB = new RegisterVO();
		registerB.setUsername("user_lb");
		registerB.setPassword(TEST_PASSWORD);
		UserVO userB = userApi.register(registerB).getUserVo();
		RegisterVO registerC = new RegisterVO();
		registerC.setUsername("user_lc");
		registerC.setPassword(TEST_PASSWORD);
		UserVO userC = userApi.register(registerC).getUserVo();

		userApi.login(userA.getUsername(), TEST_PASSWORD);
		LetterVO letter = letterApi.send(userB.getId(), "a2b");
		Assert.assertNotNull(letter.getId());
		letter = letterApi.send(userC.getId(), "a2c");

		userApi.login(userB.getUsername(), TEST_PASSWORD);
		letter = letterApi.send(userA.getId(), "b2a");
		letter = letterApi.send(userC.getId(), "b2c");

		userApi.login(userC.getUsername(), TEST_PASSWORD);
		letter = letterApi.send(userA.getId(), "c2a");

		userApi.login(userA.getUsername(), TEST_PASSWORD);
		List<LetterContacts> letterContacts = letterApi.listContacts(0, 20);
		Assert.assertEquals(2, letterContacts.size());
		int unreadLetterNum = letterApi.getUnreadNumber();
		Assert.assertEquals(2, unreadLetterNum);
		List<LetterVO> letters = letterApi.list(userB.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterApi.list(userC.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());

		userApi.login(userB.getUsername(), TEST_PASSWORD);
		letterContacts = letterApi.listContacts(0, 20);
		Assert.assertEquals(2, letterContacts.size());
		unreadLetterNum = letterApi.getUnreadNumber();
		Assert.assertEquals(1, unreadLetterNum);
		letters = letterApi.list(userA.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterApi.list(userC.getId(), 0, 20);
		Assert.assertEquals(1, letters.size());

		userApi.login(userC.getUsername(), TEST_PASSWORD);
		letterContacts = letterApi.listContacts(0, 20);
		Assert.assertEquals(2, letterContacts.size());
		unreadLetterNum = letterApi.getUnreadNumber();
		Assert.assertEquals(2, unreadLetterNum);
		letters = letterApi.list(userA.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterApi.list(userB.getId(), 0, 20);
		Assert.assertEquals(1, letters.size());
	}

}
