package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * PublishOfferTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class PublishOfferTaskListener extends DefaultTaskListener<OfferVO> {

	private String TAG = getClass().getSimpleName();

	public PublishOfferTaskListener(Activity activity) {
		super(activity, "正在发布应征...");
	}

	@Override
	public void onSuccess(OfferVO offer) {
		Log.d(TAG, "publish offer success");
		Toast.makeText(activity, "发布应征成功！", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(Constants.BROADCAST_INTENT_PUBLISH_OFFER);
		activity.sendBroadcast(intent);

		activity.finish();
	}

}
