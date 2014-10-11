package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.util.Log;

/**
 * 用户再次打开App时，使用本地存储的userToken 和refreshToken 登陆
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class ReloginTask extends Task<String, LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public ReloginTask(Activity activity) {
		super(activity);
	}

	@Override
	protected LoginResultVO myDoInBackground(String... args) {
		Log.d(TAG, "relogin");
		String userToken = args[0];
		String refreshToken = args[1];

		LoginResultVO result = IbangApi.init(activity).getUserApi().login(userToken, refreshToken);

		// process api context after login
		ApiContext.getInstance(activity).setUserToken(result.getUserToken());
		ApiContext.getInstance(activity).setRefreshToken(result.getRefreshToken());
		ApiContext.getInstance(activity).setAuthorized(true);
		ApiContext.getInstance(activity).setCurrentUser(result.getUserVo());
		ApiContext.getInstance(activity).setCurrentUserId(result.getUserVo().getId());

		return result;
	}

}
