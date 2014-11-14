package net.ipetty.ibang.service;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.model.User;

import org.junit.Test;

/**
 * IdentityVerificationServiceTest
 * @author luocanfeng
 * @date 2014年11月7日
 */
public class IdentityVerificationServiceTest extends BaseServiceTest {

	@Resource
	private IdentityVerificationService identityVerificationService;

	@Resource
	private UserService userService;

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		User user = new User();
		user.setUsername("test_user_identity_verify");
		user.setPassword(TEST_PASSWORD);
		userService.register(user);

		Assert.assertEquals(0, identityVerificationService.getVerifyingTotalNum());
		Assert.assertEquals(0, identityVerificationService.getVerifiedTotalNum());
		Assert.assertEquals(0, identityVerificationService.getTotalNum());

		IdentityVerification identityVerification = new IdentityVerification();
		identityVerification.setUserId(user.getId());
		identityVerification.setRealName("张三");
		identityVerification.setIdNumber("210320************");
		identityVerification.setIdCardFront("/usr/local/id/front/zhangsan.jpg");
		identityVerificationService.submit(identityVerification);

		Assert.assertEquals(1, identityVerificationService.getVerifyingTotalNum());
		Assert.assertEquals(0, identityVerificationService.getVerifiedTotalNum());
		Assert.assertEquals(1, identityVerificationService.getTotalNum());

		identityVerification = identityVerificationService.getByUserId(user.getId());
		Assert.assertNotNull(identityVerification);

		List<IdentityVerification> identityVerifications = identityVerificationService.list(0, 20);
		Assert.assertEquals(1, identityVerifications.size());
		identityVerifications = identityVerificationService.listVerifying(0, 20);
		Assert.assertEquals(1, identityVerifications.size());

		identityVerificationService.verify(user.getId(), false, "");
		identityVerifications = identityVerificationService.list(0, 20);
		Assert.assertEquals(1, identityVerifications.size());
		identityVerifications = identityVerificationService.listVerifying(0, 20);
		Assert.assertEquals(0, identityVerifications.size());

		Assert.assertEquals(0, identityVerificationService.getVerifyingTotalNum());
		Assert.assertEquals(1, identityVerificationService.getVerifiedTotalNum());
		Assert.assertEquals(1, identityVerificationService.getTotalNum());
	}

}
