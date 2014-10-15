package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.OfferApi;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetOfferByIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class GetOfferByIdTask extends Task<Long, OfferVO> {

	private String TAG = getClass().getSimpleName();

	public GetOfferByIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected OfferVO myDoInBackground(Long... args) {
		Log.d(TAG, "get offer");
		return IbangApi.init(activity).create(OfferApi.class).getById(args[0]);
	}

}
