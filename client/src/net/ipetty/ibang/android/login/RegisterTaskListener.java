package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * RegisterTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class RegisterTaskListener extends DefaultTaskListener<LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public RegisterTaskListener(Activity activity) {
		super(activity, "正在注册...");
	}

	@Override
	public void onSuccess(LoginResultVO result) {
		Log.d(TAG, "register success");
		// AppUtils.goTo(activity, UserProfileActivity.class);
		Intent intent = new Intent(Constants.BROADCAST_INTENT_IS_LOGIN);
		activity.sendBroadcast(intent);

		ActivityManager.getInstance().removeActivity(activity);
		ActivityManager.getInstance().finishLoginAndRegister();
	}

}
