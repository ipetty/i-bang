package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.IdentityVerification;

/**
 * 身份审核
 * @author luocanfeng
 * @date 2014年11月7日
 */
public interface IdentityVerificationDao {

	/**
	 * 保存/更新
	 */
	public void save(IdentityVerification identityVerification);

	/**
	 * 获取
	 */
	public IdentityVerification getByUserId(Integer userId);

	/**
	 * 审核
	 */
	public void verify(IdentityVerification identityVerification);

	/**
	 * 将用户标记为已通过身份验证状态
	 */
	public void setVerified(Integer userId);

	/**
	 * 获取待审核列表
	 */
	public List<Integer> listVerifying(int pageNumber, int pageSize);

	/**
	 * 获取身份审核列表
	 */
	public List<Integer> list(int pageNumber, int pageSize);

}
