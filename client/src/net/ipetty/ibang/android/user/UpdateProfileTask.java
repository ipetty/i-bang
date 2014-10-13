package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * UpdateProfileTask
 * 
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class UpdateProfileTask extends Task<UserFormVO, UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateProfileTask(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(UserFormVO... args) {
		Log.d(TAG, "update profile");
		return IbangApi.init(activity).getUserApi().update(args[0]);
	}

}
