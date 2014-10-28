package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * UpdateOfferRangeTask
 * 
 * @author luocanfeng
 * @date 2014年10月28日
 */
public class UpdateOfferRangeTask extends Task<UserOfferRange, UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateOfferRangeTask(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(UserOfferRange... args) {
		Log.d(TAG, "update offer range");
		return IbangApi.init(activity).getUserApi().updateOfferRange(args[0]);
	}

}
