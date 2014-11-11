package net.ipetty.ibang.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String verify(Model model, HttpServletRequest request, HttpServletResponse response,String currentPage) {
		User user = (User)request.getSession().getAttribute(AdminConstants.SESSION_NAME);
		//TODO:判断是否有权限登录
		if(user == null){
			return "/admin/login";
		}
		model.addAttribute("user",user);
		
		int cPage = 1;
		if (StringUtils.isNotEmpty(currentPage)) {
			cPage = Integer.parseInt(currentPage);
		} else {
			currentPage = "1";
		}
		//TODO:这里需要根据参数区分是全部还是部分
		List<IdentityVerificationVO> listVerify = listVerifying(cPage,
				PAGE_SIZE);
		model.addAttribute("listVerify", listVerify);
		model.addAttribute("currentPage", currentPage);
		//TODO: 这里可能需要知道一共有多少页
		model.addAttribute("totalPage", 0);
		

		return "admin/verify";
	}
	/**
	 * 审核
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public void verify(String verifyInfo, boolean approve) {

	}

	/**
	 * 获取待审核列表
	 */
	@RequestMapping(value = "/verify/listVerifying", method = RequestMethod.GET)
	public @ResponseBody List<IdentityVerificationVO> listVerifying(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.listVerifying(pageNumber,
				pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

	/**
	 * 获取身份审核列表
	 */
	@RequestMapping(value = "/verify/listAll", method = RequestMethod.GET)
	public @ResponseBody List<IdentityVerificationVO> list(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.list(pageNumber, pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

}
