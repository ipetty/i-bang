package net.ipetty.ibang.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Letter;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.LetterContacts;

import org.junit.Assert;
import org.junit.Test;

/**
 * LetterServiceTest
 * @author luocanfeng
 * @date 2014年11月7日
 */
public class LetterServiceTest extends BaseServiceTest {

	@Resource
	private LetterService letterService;

	@Resource
	private UserService userService;

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		User userA = new User();
		userA.setUsername("user_a");
		userA.setPassword(TEST_PASSWORD);
		userService.register(userA);
		User userB = new User();
		userB.setUsername("user_b");
		userB.setPassword(TEST_PASSWORD);
		userService.register(userB);
		User userC = new User();
		userC.setUsername("user_c");
		userC.setPassword(TEST_PASSWORD);
		userService.register(userC);

		Letter letter = letterService.send(userA.getId(), userB.getId(), "a2b");
		Assert.assertNotNull(letter.getId());
		letter = letterService.getById(letter.getId());
		Assert.assertNotNull(letter);

		letter = letterService.send(userA.getId(), userC.getId(), "a2c");
		letter = letterService.send(userB.getId(), userA.getId(), "b2a");
		letter = letterService.send(userB.getId(), userC.getId(), "b2c");
		letter = letterService.send(userC.getId(), userA.getId(), "c2a");

		List<LetterContacts> letterContacts = letterService.listContactsByUserId(userA.getId(), 0, 20);
		Assert.assertEquals(2, letterContacts.size());
		letterContacts = letterService.listContactsByUserId(userB.getId(), 0, 20);
		Assert.assertEquals(2, letterContacts.size());
		letterContacts = letterService.listContactsByUserId(userC.getId(), 0, 20);
		Assert.assertEquals(2, letterContacts.size());

		int unreadLetterNum = letterService.getUnreadNumberByUserId(userA.getId());
		Assert.assertEquals(2, unreadLetterNum);
		unreadLetterNum = letterService.getUnreadNumberByUserId(userB.getId());
		Assert.assertEquals(1, unreadLetterNum);
		unreadLetterNum = letterService.getUnreadNumberByUserId(userC.getId());
		Assert.assertEquals(2, unreadLetterNum);

		List<Letter> letters = letterService.listByUserIdAndContactUserId(userA.getId(), userB.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterService.listByUserIdAndContactUserId(userB.getId(), userA.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterService.listByUserIdAndContactUserId(userA.getId(), userC.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterService.listByUserIdAndContactUserId(userC.getId(), userA.getId(), 0, 20);
		Assert.assertEquals(2, letters.size());
		letters = letterService.listByUserIdAndContactUserId(userB.getId(), userC.getId(), 0, 20);
		Assert.assertEquals(1, letters.size());
		letters = letterService.listByUserIdAndContactUserId(userC.getId(), userB.getId(), 0, 20);
		Assert.assertEquals(1, letters.size());
	}

}
