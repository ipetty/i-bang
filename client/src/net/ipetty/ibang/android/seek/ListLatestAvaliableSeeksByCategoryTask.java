package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksByCategoryTask
 * 
 * @author luocanfeng
 * @date 2014年10月20日
 */
public class ListLatestAvaliableSeeksByCategoryTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLatestAvaliableSeeksByCategoryTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest avaliable seeks by category");
		return IbangApi.init(activity).create(SeekApi.class)
				.listLatestByCategory(args[0], args[1], args[2], Integer.valueOf(args[3]), Integer.valueOf(args[4]));
	}

}
