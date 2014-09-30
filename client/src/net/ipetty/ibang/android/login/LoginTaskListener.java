/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.main.MainActivity;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.util.Log;

/**
 * LoginTaskListener
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class LoginTaskListener extends DefaultTaskListener<LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public LoginTaskListener(Activity activity) {
		super(activity, "正在登录...");
	}

	@Override
	public void onSuccess(LoginResultVO result) {
		Log.d(TAG, "login success");
		AppUtils.goTo(activity, MainActivity.class);
		activity.finish();
	}

}
