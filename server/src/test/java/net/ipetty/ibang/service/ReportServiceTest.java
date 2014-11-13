package net.ipetty.ibang.service;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.ipetty.ibang.model.Report;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.vo.Constants;

import org.junit.Test;

/**
 * ReportServiceTest
 * @author luocanfeng
 * @date 2014年11月13日
 */
public class ReportServiceTest extends BaseServiceTest {

	@Resource
	private ReportService reportService;

	@Resource
	private UserService userService;

	private static final String TEST_PASSWORD = "888888";

	@Test
	public void testAll() {
		User user = new User();
		user.setUsername("test_user_report");
		user.setPassword(TEST_PASSWORD);
		userService.register(user);
		Assert.assertNotNull(user.getId());

		Assert.assertEquals(0, reportService.getUndealedNum());
		Assert.assertEquals(0, reportService.getDealedNum());
		Assert.assertEquals(0, reportService.getAllNum());

		Report report = new Report();
		report.setType(Constants.REPORT_TYPE_USER);
		report.setUserId(user.getId());
		report.setBehave(Constants.REPORT_BEHAVE_TYPE_AD);
		report.setReporterId(user.getId());
		reportService.report(report);
		Assert.assertNotNull(report.getId());

		Assert.assertEquals(1, reportService.getUndealedNum());
		Assert.assertEquals(0, reportService.getDealedNum());
		Assert.assertEquals(1, reportService.getAllNum());

		report = reportService.getById(report.getId());
		Assert.assertNotNull(report);

		List<Report> list = reportService.listUndealed(0, 20);
		Assert.assertEquals(1, list.size());
		list = reportService.listDealed(0, 20);
		Assert.assertEquals(0, list.size());
		list = reportService.list(0, 20);
		Assert.assertEquals(1, list.size());

		reportService.deal(report.getId(), Constants.REPORT_RESULT_PENDING);

		Assert.assertEquals(0, reportService.getUndealedNum());
		Assert.assertEquals(1, reportService.getDealedNum());
		Assert.assertEquals(1, reportService.getAllNum());
	}

}
