package net.ipetty.ibang.android.message;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SystemMessageApi;
import android.app.Activity;
import android.util.Log;

/**
 * ReadSystemMessageTask
 * 
 * @author luocanfeng
 * @date 2014年10月22日
 */
public class ReadSystemMessageTask extends Task<Long, Boolean> {

	private String TAG = getClass().getSimpleName();

	public ReadSystemMessageTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Long... args) {
		Log.d(TAG, "read system message");
		return IbangApi.init(activity).create(SystemMessageApi.class).read(args[0]);
	}

}
