package net.ipetty.ibang.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.service.IdentityVerificationService;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import net.ipetty.ibang.web.rest.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 身份审核Web Controller接口
 * @author luocanfeng
 * @date 2014年11月8日
 */
@Controller("verifyController")
public class IdentityVerificationController extends BaseController {

	@Resource
	private IdentityVerificationService identityVerificationService;

	@Resource
	private HttpServletRequest request;

	/**
	 * 审核
	 */
	@RequestMapping(value = "/verification/verify", method = RequestMethod.POST)
	public void verify(String verifyInfo, boolean approve) {
		HttpSession session = request.getSession(false);

	}

	/**
	 * 获取待审核列表
	 */
	@RequestMapping(value = "/verification/listVerifying", method = RequestMethod.GET)
	public List<IdentityVerificationVO> listVerifying(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.listVerifying(pageNumber,
				pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

	/**
	 * 获取身份审核列表
	 */
	@RequestMapping(value = "/verification/list", method = RequestMethod.GET)
	public List<IdentityVerificationVO> list(int pageNumber, int pageSize) {
		List<IdentityVerification> identityVerifications = identityVerificationService.list(pageNumber, pageSize);
		return IdentityVerification.listToVoList(identityVerifications);
	}

}
