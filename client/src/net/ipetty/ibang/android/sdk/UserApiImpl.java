package net.ipetty.ibang.android.sdk;

import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.UserApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserVO;
import retrofit.mime.TypedFile;
import android.content.Context;

/**
 * UserApiImpl<br />
 * 由于Retrofit 没法进行Post 拦截进行ApiContext 操作，故这里进行了一下ApiContext 的操作；<br />
 * 
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class UserApiImpl implements UserApi {

	private Context context;
	private UserApi userApi;

	public UserApiImpl(Context context) {
		this.context = context;
		userApi = IbangApi.init(context).create(UserApi.class);
	}

	@Override
	public LoginResultVO login(String loginName, String password) {
		LoginResultVO result = userApi.login(loginName, password);
		ApiContext.getInstance(context).setUserToken(result.getUserToken());
		ApiContext.getInstance(context).setRefreshToken(result.getRefreshToken());
		return result;
	}

	@Override
	public LoginResultVO relogin(String userToken, String refreshToken) {
		LoginResultVO result = userApi.relogin(userToken, refreshToken);
		ApiContext.getInstance(context).setUserToken(result.getUserToken());
		ApiContext.getInstance(context).setRefreshToken(result.getRefreshToken());
		return result;
	}

	@Override
	public boolean logout() {
		if (userApi.logout()) {
			ApiContext.getInstance(context).setUserToken(null);
			ApiContext.getInstance(context).setRefreshToken(null);
			return true;
		}
		return false;
	}

	@Override
	public LoginResultVO register(RegisterVO register) {
		LoginResultVO result = userApi.register(register);
		ApiContext.getInstance(context).setUserToken(result.getUserToken());
		ApiContext.getInstance(context).setRefreshToken(result.getRefreshToken());
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

}
