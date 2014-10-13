package net.ipetty.ibang.android.boot;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.LoginResultVO;
import android.app.Activity;
import android.util.Log;

/**
 * 用户登录
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
		return IbangApi.init(activity).getUserApi().relogin(args[0], args[1]);
	}

}
