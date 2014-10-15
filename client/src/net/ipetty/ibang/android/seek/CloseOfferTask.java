package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.OfferApi;
import android.app.Activity;
import android.util.Log;

/**
 * CloseOfferTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class CloseOfferTask extends Task<Long, Boolean> {

	private String TAG = getClass().getSimpleName();

	public CloseOfferTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Long... args) {
		Log.d(TAG, "close offer");
		return IbangApi.init(activity).create(OfferApi.class).close(args[0]);
	}

}
