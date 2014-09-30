package net.ipetty.ibang.android.login;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.UserApi;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import android.app.Activity;
import android.util.Log;

/**
 * 注册
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class RegisterTask extends Task<RegisterVO, LoginResultVO> {

	private String TAG = getClass().getSimpleName();

	public RegisterTask(Activity activity) {
		super(activity);
	}

	@Override
	protected LoginResultVO myDoInBackground(RegisterVO... args) {
		Log.d(TAG, "register");
		RegisterVO register = args[0];

		LoginResultVO result = IbangApi.init(activity).create(UserApi.class).register(register);

		// process api context after register
		ApiContext.getInstance(activity).setUserToken(result.getUserToken());
		ApiContext.getInstance(activity).setRefreshToken(result.getRefreshToken());
		ApiContext.getInstance(activity).setAuthorized(true);
		ApiContext.getInstance(activity).setCurrentUser(result.getUserVo());
		ApiContext.getInstance(activity).setCurrentUserId(result.getUserVo().getId());

		return result;
	}

}
