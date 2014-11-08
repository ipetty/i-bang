package net.ipetty.ibang.api;

import net.ipetty.ibang.vo.IdentityVerificationVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * 身份审核
 * @author luocanfeng
 * @date 2014年11月8日
 */
public interface IdentityVerificationApi {

	/**
	 * 提交审核/修改后重新提交审核
	 */
	@POST("/verification/submit")
	public boolean submit(@Body IdentityVerificationVO identityVerification);

	/**
	 * 获取用户的身份验证信息
	 */
	@GET("/verification")
	public IdentityVerificationVO get();

}
