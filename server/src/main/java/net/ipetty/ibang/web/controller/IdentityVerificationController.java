package net.ipetty.ibang.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.IdentityVerificationService;
import net.ipetty.ibang.vo.IdentityVerificationVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 身份审核Web Controller接口
 * @author luocanfeng
 * @date 2014年11月8日
 */
@Controller("verifyController")
public class IdentityVerificationController {

	private static final int PAGE_SIZE = 20;

	@Resource
	private IdentityVerificationService identityVerificationService;
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String verify(Model model, HttpServletRequest request, HttpServletResponse  response){
		return verify(model, request, response,"1");
	}
	
	
	@RequestMapping(value = "/verify/page/{currentPage}", method = RequestMethod.GET)
	public String verify(Model model, HttpServletRequest request, HttpServletResponse response, String currentPage) {
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
			return "/admin/verify";
		}

		model.addAttribute("user", user);
		if (StringUtils.isBlank(currentPage)) {
			currentPage = "1";
		}
		int cPage = Integer.parseInt(currentPage);
		// TODO:这里需要根据参数区分是全部还是部分
		List<IdentityVerificationVO> listVerify = listVerifying(cPage - 1, PAGE_SIZE);
		int totalNum = identityVerificationService.getVerifyingTotalNum();
		int totalPage = totalNum / PAGE_SIZE;
		if (totalNum % PAGE_SIZE != 0) {
			++totalPage;
		}

		model.addAttribute("listVerify", listVerify);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);

		return "admin/verify";
	}

	/**
	 * 审核
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public void verify(Integer userId, boolean approved, String verifyInfo) {
		identityVerificationService.verify(userId, approved, verifyInfo);
	}

	/**
	 * 获取待审核列表
	 */
	@RequestMapping(value = "/verify/listVerifying", method = RequestMethod.GET)
	@ResponseBody
	public List<IdentityVerificationVO> listVerifying(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.listVerifying(pageNumber,
				pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

	/**
	 * 获取身份审核列表
	 */
	@RequestMapping(value = "/verify/listAll", method = RequestMethod.GET)
	@ResponseBody
	public List<IdentityVerificationVO> list(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.list(pageNumber, pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

}
