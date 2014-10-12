package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * UpdateProfileTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月12日
 */
public class UpdateProfileTaskListener extends DefaultTaskListener<UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateProfileTaskListener(Activity activity) {
		super(activity, "正在更新用户资料...");
	}

	@Override
	public void onSuccess(UserVO user) {
		Log.d(TAG, "update profile success");
		Toast.makeText(activity, "更新用户资料成功！", Toast.LENGTH_SHORT).show();
	}

}
