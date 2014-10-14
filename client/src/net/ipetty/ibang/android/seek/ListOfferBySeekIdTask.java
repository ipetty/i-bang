package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.OfferApi;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListOfferBySeekIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class ListOfferBySeekIdTask extends Task<Long, List<OfferVO>> {

	private String TAG = getClass().getSimpleName();

	public ListOfferBySeekIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<OfferVO> myDoInBackground(Long... args) {
		Log.d(TAG, "list offers by seek id");
		// TODO 页数与单页数量
		return IbangApi.init(activity).create(OfferApi.class).listBySeekId(args[0], 0, 20);
	}

}
