package net.ipetty.ibang.api;

import net.ipetty.ibang.api.context.ApiContext;
import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.Unread;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;
import retrofit.http.Body;
import retrofit.mime.TypedFile;

/**
 * UserApiImpl<br />
 * 由于Retrofit 没法进行Post 拦截进行ApiContext 操作，故这里进行了一下ApiContext 的操作；<br />
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class UserApiImpl implements UserApi {

	private UserApi userApi = IbangApi.init().create(UserApi.class);

	@Override
	public LoginResultVO login(String loginName, String password) {
		LoginResultVO result = userApi.login(loginName, password);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());
		return result;
	}

	@Override
	public LoginResultVO relogin(String userToken, String refreshToken) {
		LoginResultVO result = userApi.relogin(userToken, refreshToken);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());
		return result;
	}

	@Override
	public boolean logout() {
		if (userApi.logout()) {
			ApiContext.getInstance().setUserToken(null);
			ApiContext.getInstance().setRefreshToken(null);
			return true;
		}
		return false;
	}

	@Override
	public LoginResultVO register(RegisterVO register) {
		LoginResultVO result = userApi.register(register);
		ApiContext.getInstance().setUserToken(result.getUserToken());
		ApiContext.getInstance().setRefreshToken(result.getRefreshToken());
		return result;
	}

	@Override
	public boolean checkUsernameAvailable(String username) {
		return userApi.checkUsernameAvailable(username);
	}

	@Override
	public UserVO getById(Integer id) {
		return userApi.getById(id);
	}

	@Override
	public UserVO getByUsername(String username) {
		return userApi.getByUsername(username);
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		return userApi.changePassword(oldPassword, newPassword);
	}

	@Override
	public UserVO updateAvatar(TypedFile imageFile) {
		return userApi.updateAvatar(imageFile);
	}

	@Override
	public UserVO update(UserFormVO userFormVo) {
		return userApi.update(userFormVo);
	}

	@Override
	public UserVO updateOfferRange(@Body UserOfferRange userOfferRange) {
		return userApi.updateOfferRange(userOfferRange);
	}

	@Override
	public Unread getUnread() {
		return userApi.getUnread();
	}

}
