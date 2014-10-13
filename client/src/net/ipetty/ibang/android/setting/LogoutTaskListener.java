package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.login.LoginActivity;
import android.app.Activity;
import android.util.Log;

/**
 * LogoutTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class LogoutTaskListener extends DefaultTaskListener<Boolean> {

	private String TAG = getClass().getSimpleName();

	public LogoutTaskListener(Activity activity) {
		super(activity, "正在注销...");
	}

	@Override
	public void onSuccess(Boolean result) {
		Log.d(TAG, "logout success");

		AppUtils.goTo(activity, LoginActivity.class);
		AnimUtils.fadeInToOut(activity);
		activity.finish();
	}

}
