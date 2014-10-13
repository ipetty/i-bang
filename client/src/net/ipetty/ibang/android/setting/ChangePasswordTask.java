package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import android.app.Activity;
import android.util.Log;

/**
 * ChangePasswordTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class ChangePasswordTask extends Task<String, Boolean> {

	private String TAG = getClass().getSimpleName();

	public ChangePasswordTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(String... args) {
		Log.d(TAG, "change password");
		return IbangApi.init(activity).getUserApi().changePassword(args[0], args[1]);
	}

}
