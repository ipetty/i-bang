package net.ipetty.ibang.api;

import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

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

	/**
	 * 根据用户帐号获取用户信息
	 */
	@GET("/user/{username}")
	public UserVO getByUsername(@Path("username") String username);

	/**
	 * 修改密码
	 */
	@FormUrlEncoded
	@POST("/changePassword")
	public boolean changePassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

	/**
	 * 更新用户头像
	 */
	@Multipart
	@POST("/user/updateAvatar")
	public UserVO updateAvatar(@Part("imageFile") TypedFile imageFile);

	/**
	 * 修改用户个人信息
	 */
	@POST("/user/update")
	public UserVO update(@Body UserFormVO userFormVo);

	/**
	 * 修改用户帮助范围
	 */
	@POST("/user/updateOfferRange")
	public UserVO updateOfferRange(@Body UserOfferRange userOfferRange);

}
