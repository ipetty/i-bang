package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetSeekByIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class GetSeekByIdTask extends Task<Long, SeekVO> {

	private String TAG = getClass().getSimpleName();

	public GetSeekByIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected SeekVO myDoInBackground(Long... args) {
		Log.d(TAG, "get seek");
		return IbangApi.init(activity).create(SeekApi.class).getById(args[0]);
	}

}
