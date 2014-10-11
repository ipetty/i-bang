package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * 用户登录
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class LoginTask extends Task<String, LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public LoginTask(Activity activity) {
		super(activity);
	}

	@Override
	protected LoginResultVO myDoInBackground(String... args) {
		Log.d(TAG, "login");
		String loginName = args[0];
		String password = args[1];

		LoginResultVO result = IbangApi.init(activity).getUserApi().login(loginName, password);

		// process api context after login
		ApiContext.getInstance(activity).setUserToken(result.getUserToken());
		ApiContext.getInstance(activity).setRefreshToken(result.getRefreshToken());
		ApiContext.getInstance(activity).setAuthorized(true);
		ApiContext.getInstance(activity).setCurrentUserId(result.getUserVo().getId());
		ApiContext.getInstance(activity).setCurrentUser(result.getUserVo());

		Intent intent = new Intent(Constants.BROADCAST_INTENT_IS_LOGIN);
		activity.sendBroadcast(intent);

		return result;
	}

}
