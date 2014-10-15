package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.main.MainActivity;
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
		ActivityManager.getInstance().finish();
		AppUtils.goTo(activity, MainActivity.class);
		AnimUtils.fadeInToOut(activity);
	}

}
