package net.ipetty.ibang.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.web.rest.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController extends BaseController {

	@Resource
	private UserService userService;

	@Resource
	private HttpServletRequest request;

	/**
	 * 跳转到登录界面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView toLogin() {
		return new ModelAndView("admin/login");
	}

	/**
	 * 登陆
	 */
	@RequestMapping(value = "/login2", method = RequestMethod.POST)
	public ModelAndView login(String loginName, String password) {
		logger.debug("login with loginName={}", loginName);
		Assert.hasText(loginName, "登录名不能为空");
		Assert.hasText(password, "密码不能为空");
		User user = userService.login(loginName, password); // 未发生异常则已登录成功

		HttpSession session = request.getSession(true);
		session.setAttribute(AdminConstants.SESSION_NAME, user);
		return new ModelAndView("verify/listVerifying");
	}

}
