package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.OfferApi;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.util.Log;

/**
 * PublishOfferTask
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class PublishOfferTask extends Task<OfferVO, OfferVO> {

	private String TAG = getClass().getSimpleName();

	public PublishOfferTask(Activity activity) {
		super(activity);
	}

	@Override
	protected OfferVO myDoInBackground(OfferVO... args) {
		Log.d(TAG, "publish offer");
		return IbangApi.init(activity).create(OfferApi.class).offer(args[0]);
	}

}
