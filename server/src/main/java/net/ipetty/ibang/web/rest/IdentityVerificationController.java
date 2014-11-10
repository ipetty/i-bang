package net.ipetty.ibang.web.rest;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.service.IdentityVerificationService;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 身份审核REST接口
 * @author luocanfeng
 * @date 2014年11月8日
 */
@Controller("verificationController")
public class IdentityVerificationController extends BaseController {

	@Resource
	private IdentityVerificationService identityVerificationService;

	/**
	 * 提交审核/修改后重新提交审核
	 */
	@RequestMapping(value = "/verification/submit", method = RequestMethod.POST)
	public boolean submit(@RequestBody IdentityVerificationVO identityVerification) {
		Assert.notNull(identityVerification, "身份审核不能为空");
		Assert.notNull(identityVerification.getUserId(), "审核人ID不能为空");
		Assert.notNull(identityVerification.getRealName(), "真实姓名不能为空");
		Assert.notNull(identityVerification.getIdNumber(), "身份证号不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能提交身份审核");
		}
		if (!currentUser.getId().equals(identityVerification.getUserId())) {
			logger.debug("current user id = {}", currentUser.getId());
			logger.debug("verify user id = {}", identityVerification.getUserId());
			throw new RestException("只能提交自己的身份审核信息");
		}

		identityVerificationService.submit(IdentityVerification.fromVO(identityVerification));
		return true;
	}

	/**
	 * 获取用户的身份验证信息
	 */
	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public IdentityVerificationVO get() {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能查看身份审核信息");
		}

		IdentityVerification identityVerification = identityVerificationService.getByUserId(currentUser.getId());
		return identityVerification == null ? null : identityVerification.toVO();
	}

}
