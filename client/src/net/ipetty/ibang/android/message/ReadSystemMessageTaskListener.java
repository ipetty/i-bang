package net.ipetty.ibang.android.message;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import android.app.Activity;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月22日
 */
public class ReadSystemMessageTaskListener extends DefaultTaskListener<Boolean> {

	private String TAG = getClass().getSimpleName();

	public ReadSystemMessageTaskListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onSuccess(Boolean result) {
		Log.d(TAG, "read system message success");
	}

}
