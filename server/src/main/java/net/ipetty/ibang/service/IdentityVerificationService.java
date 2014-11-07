package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.repository.IdentityVerificationDao;
import net.ipetty.ibang.vo.Constants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 身份审核
 * @author luocanfeng
 * @date 2014年11月7日
 */
@Service
@Transactional
public class IdentityVerificationService extends BaseService {

	@Resource
	private IdentityVerificationDao identityVerificationDao;

	/**
	 * 提交审核/修改后重新提交审核
	 */
	public void submit(IdentityVerification identityVerification) {
		identityVerification.setSubmittedOn(new Date());
		identityVerification.setVerifierId(1);
		identityVerification.setStatus(Constants.ID_VERIFICATION_VERIFYING);
		identityVerificationDao.save(identityVerification);
	}

	/**
	 * 获取指定用户的身份验证信息
	 */
	public IdentityVerification getByUserId(Integer userId) {
		return identityVerificationDao.getByUserId(userId);
	}

	/**
	 * 审核
	 */
	public void verify(IdentityVerification identityVerification) {
		identityVerification.setVerifiedOn(new Date());
		identityVerificationDao.verify(identityVerification);
		if (Constants.ID_VERIFICATION_APPROVED.equals(identityVerification.getStatus())) {
			// 审核通过
			identityVerificationDao.setVerified(identityVerification.getUserId());
		}
	}

	/**
	 * 获取待审核列表
	 */
	public List<IdentityVerification> listVerifying(int pageNumber, int pageSize) {
		List<Integer> users = identityVerificationDao.listVerifying(pageNumber, pageSize);
		return this.userIds2IdentityVerifications(users);
	}

	private List<IdentityVerification> userIds2IdentityVerifications(List<Integer> userIds) {
		List<IdentityVerification> identityVerifications = new ArrayList<IdentityVerification>();
		for (Integer userId : userIds) {
			identityVerifications.add(this.getByUserId(userId));
		}
		return identityVerifications;
	}

	/**
	 * 获取身份审核列表
	 */
	public List<IdentityVerification> list(int pageNumber, int pageSize) {
		List<Integer> users = identityVerificationDao.list(pageNumber, pageSize);
		return this.userIds2IdentityVerifications(users);
	}

}
