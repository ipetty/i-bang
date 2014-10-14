package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class ListLatestAvaliableSeeksTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLatestAvaliableSeeksTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest avaliable seeks");
		return IbangApi.init(activity).create(SeekApi.class)
				.listLatest(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]));
	}

}
