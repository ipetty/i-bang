package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksByKeywordTask
 * 
 * @author luocanfeng
 * @date 2014年10月21日
 */
public class ListLatestAvaliableSeeksByKeywordTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLatestAvaliableSeeksByKeywordTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest avaliable seeks by keyword");
		return IbangApi.init(activity).create(SeekApi.class)
				.listLatestByKeyword(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}

}
