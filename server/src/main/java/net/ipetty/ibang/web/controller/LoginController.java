package net.ipetty.ibang.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.admin.AdminConstants;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController{

	@Resource
	private UserService userService;

	@Resource
	private HttpServletRequest request;

	/**
	 * 跳转到登录界面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin() {
		return "/admin/login";
	}

	/**
	 * 登陆
	 */
	@RequestMapping(value = "/web/login", method = RequestMethod.POST)
	public @ResponseBody AjaxMessage<?> login(String username, String password) {
		//logger.debug("login with loginName={}", username);
		//Assert.hasText(username, "登录名不能为空");
		//Assert.hasText(password, "密码不能为空");
		AjaxMessage<?> result = AjaxMessage.OPERATION_SUCCESS_MESSAGE;
		try{
			User user = userService.login(username, password); // 未发生异常则已登录成功
			HttpSession session = request.getSession(true);
			session.setAttribute(AdminConstants.SESSION_NAME, user);
		}catch(BusinessException e){
			result = new AjaxMessage<String>(e);
		}
		return result;
	}
	
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String managaer(Model model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute(AdminConstants.SESSION_NAME);
		//TODO:判断是否有权限登录
		if(user == null){
			return "/admin/login";
		}
		model.addAttribute("user",user);
		return "/admin/manager";
	}
	
	@RequestMapping(value = "/web/logout", method = RequestMethod.GET)
	public String logout() {
		request.getSession().removeAttribute(AdminConstants.SESSION_NAME);
		return "/admin/login";
	}

}
