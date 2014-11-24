package net.ipetty.ibang.web.rest;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Report;
import net.ipetty.ibang.service.ReportService;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.ReportVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 举报REST Controller接口
 * @author luocanfeng
 * @date 2014年11月14日
 */
@Controller("reportRestController")
public class ReportController extends BaseController {

	@Resource
	private ReportService reportService;

	/**
	 * 举报
	 */
	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public ReportVO report(@RequestBody ReportVO report) {
		Assert.notNull(report, "举报对象不能为空");
		Assert.hasText(report.getType(), "举报内容类型不能为空");
		if (Constants.REPORT_TYPE_SEEK.equals(report.getType())) {
			Assert.notNull(report.getSeekId(), "要举报的求助ID不能为空");
			Assert.hasText(report.getSeekType(), "求助类型不能为空");
		} else if (Constants.REPORT_TYPE_OFFER.equals(report.getType())) {
			Assert.notNull(report.getOfferId(), "要举报的帮助ID不能为空");
		} else if (Constants.REPORT_TYPE_USER.equals(report.getType())) {
			Assert.notNull(report.getUserId(), "要举报的用户ID不能为空");
		}
		Assert.hasText(report.getBehave(), "举报行为类型不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能举报");
		}
		report.setReporterId(currentUser.getId());

		Report r = Report.fromVO(report);
		reportService.report(r);
		r = reportService.getById(r.getId());
		return r.toVO();
	}

	/**
	 * 获取举报信息
	 */
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public ReportVO getById(Long id) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能查看举报信息");
		}

		Report report = reportService.getById(id);
		if (report == null) {
			return null;
		}

		if (!currentUser.getId().equals(report.getReporterId())) {
			throw new RestException("普通用户只能查看自己举报的举报信息");
		}

		return report.toVO();
	}

}
