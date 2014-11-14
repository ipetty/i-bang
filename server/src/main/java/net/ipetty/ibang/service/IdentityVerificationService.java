package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.model.SystemMessage;
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

	private static final Integer VERIFIER_ID = 1;

	@Resource
	private IdentityVerificationDao identityVerificationDao;

	@Resource
	private SystemMessageService systemMessageService;

	/**
	 * 提交审核/修改后重新提交审核
	 */
	public void submit(IdentityVerification identityVerification) {
		identityVerification.setSubmittedOn(new Date());
		identityVerification.setVerifierId(VERIFIER_ID);
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
	public void verify(Integer userId, boolean approved, String verifyInfo) {
		identityVerificationDao.verify(userId, approved, verifyInfo);
		if (approved) {
			// 审核通过
			identityVerificationDao.setVerified(userId);
		}

		// 保存系统消息
		String title = approved ? "您的身份审核已通过。" : "您的身份审核未通过。";
		SystemMessage systemMessage = new SystemMessage(VERIFIER_ID, userId,
				approved ? Constants.SYS_MSG_TYPE_ID_VERIFICATION_APPROVED
						: Constants.SYS_MSG_TYPE_ID_VERIFICATION_UNAPPROVED, title, title);
		systemMessageService.save(systemMessage);
	}

	/**
	 * 获取待审核列表
	 */
	public List<IdentityVerification> listVerifying(int pageNumber, int pageSize) {
		List<Integer> users = identityVerificationDao.listVerifying(pageNumber, pageSize);
		return this.userIds2IdentityVerifications(users);
	}

	/**
	 * 获取待审核数目
	 */
	public int getVerifyingTotalNum() {
		return identityVerificationDao.getVerifyingTotalNum();
	}

	/**
	 * 获取已审核列表
	 */
	public List<IdentityVerification> listVerified(int pageNumber, int pageSize) {
		List<Integer> users = identityVerificationDao.listVerified(pageNumber, pageSize);
		return this.userIds2IdentityVerifications(users);
	}

	/**
	 * 获取已审核数目
	 */
	public int getVerifiedTotalNum() {
		return identityVerificationDao.getVerifiedTotalNum();
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

	/**
	 * 获取总审核数目
	 */
	public int getTotalNum() {
		return identityVerificationDao.getTotalNum();
	}

}
