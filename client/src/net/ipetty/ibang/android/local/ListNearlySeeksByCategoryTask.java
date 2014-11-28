package net.ipetty.ibang.android.local;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListNearlySeeksByCategoryTask
 * @author luocanfeng
 * @date 2014年11月28日
 */
public class ListNearlySeeksByCategoryTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListNearlySeeksByCategoryTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest seeks by category");
		return IbangApi
				.init(activity)
				.create(SeekApi.class)
				.listNearlyByCategory(args[0], args[1], args[2], args[3], args[4], Integer.valueOf(args[5]),
						Integer.valueOf(args[6]), Integer.valueOf(args[7]));
	}

}
