package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * UpdateOfferRangeTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月28日
 */
public class UpdateOfferRangeTaskListener extends DefaultTaskListener<UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateOfferRangeTaskListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onSuccess(UserVO user) {
		Log.d(TAG, "update offer range success");

		// 通知用户更新
		Intent intent = new Intent(Constants.BROADCAST_INTENT_UPDATA_USER);
		activity.sendBroadcast(intent);

		activity.finish();
	}

}
