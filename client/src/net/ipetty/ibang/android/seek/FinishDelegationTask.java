package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import android.app.Activity;
import android.util.Log;

/**
 * FinishDelegationTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class FinishDelegationTask extends Task<Long, Boolean> {

	private String TAG = getClass().getSimpleName();

	public FinishDelegationTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Long... args) {
		Log.d(TAG, "finish delegation");
		return IbangApi.init(activity).create(DelegationApi.class).finish(args[0]);
	}

}
