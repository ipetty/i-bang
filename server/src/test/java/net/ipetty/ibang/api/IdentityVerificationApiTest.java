package net.ipetty.ibang.api;

import junit.framework.Assert;
import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Test;

/**
 * IdentityVerificationApiTest
 * @author luocanfeng
 * @date 2014年11月8日
 */
public class IdentityVerificationApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private IdentityVerificationApi identityVerificationApi = IbangApi.init().create(IdentityVerificationApi.class);

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		userApi.logout();
		RegisterVO register = new RegisterVO();
		register.setUsername("test_user_identity_verify_");
		register.setPassword(TEST_PASSWORD);
		LoginResultVO result = userApi.register(register);
		UserVO user = result.getUserVo();

		IdentityVerificationVO identityVerification = new IdentityVerificationVO();
		identityVerification.setUserId(user.getId());
		identityVerification.setRealName("张三");
		identityVerification.setIdNumber("210320************");
		identityVerification.setIdCardFront("/usr/local/id/front/zhangsan.jpg");
		Assert.assertTrue(identityVerificationApi.submit(identityVerification));

		identityVerification = identityVerificationApi.get();
		Assert.assertNotNull(identityVerification);
	}

}
