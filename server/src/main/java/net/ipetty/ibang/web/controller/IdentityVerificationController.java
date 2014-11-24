package net.ipetty.ibang.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.IdentityVerificationService;
import net.ipetty.ibang.service.UserService;
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

	@Resource
	private UserService userService;

	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String verify(Model model, HttpServletRequest request, HttpServletResponse response) {
		return verify(model, request, response, "1");
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
		List<IdentityVerification> identityVerifications = identityVerificationService.listVerifying(cPage - 1,
				PAGE_SIZE);
		List<IdentityVerificationVO> verifies = listToVoList(identityVerifications);
		int totalNum = identityVerificationService.getVerifyingTotalNum();
		int totalPage = totalNum / PAGE_SIZE;
		if (totalNum % PAGE_SIZE != 0) {
			++totalPage;
		}

		model.addAttribute("verifies", verifies);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);

		return "/admin/verify";
	}

	private List<IdentityVerificationVO> listToVoList(List<IdentityVerification> list) {
		List<IdentityVerificationVO> voList = new ArrayList<IdentityVerificationVO>();
		for (IdentityVerification entity : list) {
			IdentityVerificationVO vo = entity.toVO();
			Integer userId = entity.getUserId();
			if (userId != null) {
				User user = userService.getById(userId);
				vo.setUserNickname(user.getNickname());
				vo.setUserAvatar(user.getAvatar());
			}
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 审核
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	@ResponseBody
	public boolean verify(HttpServletRequest request, HttpServletResponse response, Integer userId, boolean approved,
			String verifyInfo) {
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

		identityVerificationService.verify(userId, approved, verifyInfo);
		return true;
	}

}
