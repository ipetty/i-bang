package net.ipetty.ibang.api;

import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserVO;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * UserApi
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
public interface UserApi {

	/**
	 * 登录验证
	 */
	@FormUrlEncoded
	@POST("/login")
	public LoginResultVO login(@Field("loginName") String loginName, @Field("password") String password);

	/**
	 * 用户再次打开App时，使用本地存储的userToken 和refreshToken 登陆
	 */
	@FormUrlEncoded
	@POST("/relogin")
	public LoginResultVO relogin(@Field("userToken") String userToken, @Field("refreshToken") String refreshToken);

	/**
	 * 登出
	 */
	@GET("/logout")
	public boolean logout();

	/**
	 * 注册帐号
	 */
	@POST("/register")
	public LoginResultVO register(@Body RegisterVO register);

	/**
	 * 检查用户名是否可用，true表示可用，false表示不可用
	 */
	@GET("/user/checkUsernameAvailable")
	public boolean checkUsernameAvailable(@Query("username") String username);

	/**
	 * 根据ID获取用户帐号
	 */
	@GET("/user/id/{id}")
	public UserVO getById(@Path("id") Integer id);

}
