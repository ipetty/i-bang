package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * PublishSeekTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class PublishSeekTask extends Task<SeekVO, SeekVO> {

	private String TAG = getClass().getSimpleName();

	public PublishSeekTask(Activity activity) {
		super(activity);
	}

	@Override
	protected SeekVO myDoInBackground(SeekVO... args) {
		Log.d(TAG, "publish seek");
		return IbangApi.init(activity).create(SeekApi.class).publish(args[0]);
	}

}
