package net.ipetty.ibang.api;

import junit.framework.Assert;
import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.ReportVO;
import net.ipetty.ibang.vo.UserVO;

import org.junit.Test;

/**
 * ReportApiTest
 * @author luocanfeng
 * @date 2014年11月14日
 */
public class ReportApiTest extends BaseApiTest {

	private UserApi userApi = IbangApi.init().getUserApi();
	private ReportApi reportApi = IbangApi.init().create(ReportApi.class);

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		userApi.logout();
		RegisterVO register = new RegisterVO();
		register.setUsername("test_user_report_api");
		register.setPassword(TEST_PASSWORD);
		LoginResultVO result = userApi.register(register);
		UserVO user = result.getUserVo();

		ReportVO report = new ReportVO();
		report.setType(Constants.REPORT_TYPE_USER);
		report.setUserId(user.getId());
		report.setBehave(Constants.REPORT_BEHAVE_TYPE_AD);
		report.setReporterId(user.getId());
		report = reportApi.report(report);
		Assert.assertNotNull(report);
		Assert.assertNotNull(report.getId());

		report = reportApi.getById(report.getId());
		Assert.assertNotNull(report);
	}

}
