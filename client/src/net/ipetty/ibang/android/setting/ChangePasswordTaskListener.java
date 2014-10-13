package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * ChangePasswordTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class ChangePasswordTaskListener extends DefaultTaskListener<Boolean> {

	private String TAG = getClass().getSimpleName();

	public ChangePasswordTaskListener(Activity activity) {
		super(activity, "正在修改密码...");
	}

	@Override
	public void onSuccess(Boolean result) {
		Log.d(TAG, "change password success");
		Toast.makeText(activity, "修改密码成功！", Toast.LENGTH_SHORT).show();
		activity.finish();
	}

}
