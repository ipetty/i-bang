package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import android.app.Activity;
import android.util.Log;

/**
 * CloseDelegationTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class CloseDelegationTask extends Task<Long, Boolean> {

	private String TAG = getClass().getSimpleName();

	public CloseDelegationTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Long... args) {
		Log.d(TAG, "close delegation");
		return IbangApi.init(activity).create(DelegationApi.class).close(args[0]);
	}

}
