package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * UpdateAvatarTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月12日
 */
public class UpdateAvatarTaskListener extends DefaultTaskListener<UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateAvatarTaskListener(Activity activity) {
		super(activity, "正在更新用户头像...");
	}

	@Override
	public void onSuccess(UserVO user) {
		Log.d(TAG, "update avatar success");
		Toast.makeText(activity, "更新用户头像成功！", Toast.LENGTH_SHORT).show();

		// 通知用户更新
		Intent intent = new Intent(Constants.BROADCAST_INTENT_UPDATA_USER);
		activity.sendBroadcast(intent);
	}

}
