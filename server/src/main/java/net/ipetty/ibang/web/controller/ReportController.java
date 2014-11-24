package net.ipetty.ibang.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.model.Report;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.ReportService;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.ReportVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 举报Web Controller接口
 * @author luocanfeng
 * @date 2014年11月14日
 */
@Controller("reportWebController")
public class ReportController {

	private static final int PAGE_SIZE = 20;

	@Resource
	private ReportService reportService;

	@Resource
	private UserService userService;

	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public String verify(Model model, HttpServletRequest request, HttpServletResponse response) {
		return listReports(model, request, response, "1");
	}

	@RequestMapping(value = "/reports/page/{currentPage}", method = RequestMethod.GET)
	public String listReports(Model model, HttpServletRequest request, HttpServletResponse response, String currentPage) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			model.addAttribute("error", "您尚未登录，该界面需要登录后才能操作！");
			return "/admin/login";
		}
		User user = (User) session.getAttribute(AdminConstants.SESSION_NAME);
		if (user == null) {
			model.addAttribute("error", "您尚未登录，该界面需要登录后才能操作！");
			return "/admin/login";
		}
		if (!AdminConstants.ADMIN_USER_NAME.equals(user.getUsername())) {
			model.addAttribute("error", "您不是管理员，该界面需要管理员身份才能操作！");
			return "/admin/reportlist";
		}

		model.addAttribute("user", user);
		if (StringUtils.isBlank(currentPage)) {
			currentPage = "1";
		}
		int cPage = Integer.parseInt(currentPage);
		// TODO:这里需要根据参数区分是全部还是部分
		List<ReportVO> reportList = listToVoList(reportService.listUndealed(cPage - 1, PAGE_SIZE));
		int totalNum = reportService.getUndealedNum();
		int totalPage = totalNum / PAGE_SIZE;
		if (totalNum % PAGE_SIZE != 0) {
			++totalPage;
		}

		model.addAttribute("reportList", reportList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);

		return "admin/report";
	}

	private List<ReportVO> listToVoList(List<Report> list) {
		List<ReportVO> voList = new ArrayList<ReportVO>();
		for (Report entity : list) {
			ReportVO vo = entity.toVO();
			Integer userId = entity.getUserId();
			if (userId != null) {
				User user = userService.getById(userId);
				vo.setUserNickname(user.getNickname());
				vo.setUserAvatar(user.getAvatar());
			}
			userId = entity.getReporterId();
			if (userId != null) {
				User user = userService.getById(userId);
				vo.setReporterNickname(user.getNickname());
				vo.setReporterAvatar(user.getAvatar());
			}
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 审核
	 */
	@RequestMapping(value = "/dealreport", method = RequestMethod.POST)
	public boolean dealReport(HttpServletRequest request, HttpServletResponse response, Long id, boolean result) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		User user = (User) session.getAttribute(AdminConstants.SESSION_NAME);
		if (user == null) {
			return false;
		}
		if (!AdminConstants.ADMIN_USER_NAME.equals(user.getUsername())) {
			return false;
		}

		reportService.deal(id, result ? Constants.REPORT_RESULT_PUNISH : Constants.REPORT_RESULT_FAKE_REPORT);
		return true;
	}

}
