package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import android.app.Activity;
import android.util.Log;

/**
 * LogoutTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class LogoutTask extends Task<Void, Boolean> {

	private String TAG = getClass().getSimpleName();

	public LogoutTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Void... args) {
		Log.d(TAG, "logout");
		return IbangApi.init(activity).getUserApi().logout();
	}

}
