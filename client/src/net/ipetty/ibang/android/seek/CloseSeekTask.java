package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import android.app.Activity;
import android.util.Log;

/**
 * CloseSeekTask
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class CloseSeekTask extends Task<Long, Boolean> {

	private String TAG = getClass().getSimpleName();

	public CloseSeekTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Long... args) {
		Log.d(TAG, "close seek");
		return IbangApi.init(activity).create(SeekApi.class).close(args[0]);
	}

}
