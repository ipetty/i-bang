package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.SeekVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksByCityAndOfferRangeTask
 * 
 * @author luocanfeng
 * @date 2014年10月29日
 */
public class ListLatestAvaliableSeeksByCityAndOfferRangeTask extends Task<String, List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLatestAvaliableSeeksByCityAndOfferRangeTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SeekVO> myDoInBackground(String... args) {
		Log.d(TAG, "list latest avaliable seeks by city and offer range");
		String userIdStr = args[2];
		Integer userId = StringUtils.isNotBlank(userIdStr) ? Integer.valueOf(userIdStr) : null;
		return IbangApi
				.init(activity)
				.create(SeekApi.class)
				.listLatestByCityAndOfferRange(args[0], args[1], userId, args[3], Integer.valueOf(args[4]),
						Integer.valueOf(args[5]));
	}

}
