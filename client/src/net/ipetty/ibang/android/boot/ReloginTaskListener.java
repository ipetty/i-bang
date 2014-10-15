/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.boot;

import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.main.MainActivity;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * LoginTaskListener
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class ReloginTaskListener extends DefaultTaskListener<LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public ReloginTaskListener(Activity activity) {
		super(activity, "正在登录...");
	}

	@Override
	public void onSuccess(LoginResultVO result) {
		if (result == null) { // token失效
			Log.d(TAG, "token失效");
		} else { // 使用token重新登录成功
			Log.d(TAG, "relogin success");
			Intent intent = new Intent(Constants.BROADCAST_INTENT_IS_LOGIN);
			activity.sendBroadcast(intent);
		}

		AppUtils.goTo(activity, MainActivity.class);
		AnimUtils.fadeInToOut(activity);
		ActivityManager.getInstance().removeActivity(activity);
		ActivityManager.getInstance().finishLoginAndRegister();
	}

}
