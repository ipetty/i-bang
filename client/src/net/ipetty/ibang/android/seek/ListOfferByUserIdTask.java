package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.OfferApi;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListOfferByUserIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class ListOfferByUserIdTask extends Task<Integer, List<OfferVO>> {

	private String TAG = getClass().getSimpleName();

	public ListOfferByUserIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<OfferVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list offers by user id");
		return IbangApi.init(activity).create(OfferApi.class).listByUserId(args[0], args[1], args[2]);
	}

}
