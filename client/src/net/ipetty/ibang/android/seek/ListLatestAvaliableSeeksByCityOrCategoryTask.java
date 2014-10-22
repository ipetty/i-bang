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
public class ListLatestAvaliableSeeksByCityOrCategoryTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLatestAvaliableSeeksByCityOrCategoryTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest avaliable seeks by city or category");
		return IbangApi
				.init(activity)
				.create(SeekApi.class)
				.listLatestByCityOrCategory(args[0], args[1], args[2], args[3], args[4], Integer.valueOf(args[5]),
						Integer.valueOf(args[6]));
	}

}
