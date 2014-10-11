package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import android.app.Activity;
import android.util.Log;

/**
 * 登出
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class LogoutTask extends Task<Void, Boolean> {

	private String TAG = getClass().getSimpleName();

	public LogoutTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Void... args) {
		Log.d(TAG, "logout");
		boolean result = IbangApi.init(activity).getUserApi().logout();
		if (result) {
			// process api context after logout
			ApiContext.getInstance(activity).setUserToken(null);
			ApiContext.getInstance(activity).setRefreshToken(null);
			ApiContext.getInstance(activity).setAuthorized(false);
			ApiContext.getInstance(activity).setCurrentUser(null);
			ApiContext.getInstance(activity).setCurrentUserId(null);
		}
		return result;
	}

}
