package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListSeeksByUserIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class ListSeeksByUserIdTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListSeeksByUserIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list seeks by user id");
		return IbangApi.init(activity).create(SeekApi.class)
				.listByUserId(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}

}
