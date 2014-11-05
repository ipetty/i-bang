package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * PublishSeekTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class PublishSeekTaskListener extends DefaultTaskListener<SeekVO> {

	private String TAG = getClass().getSimpleName();
	private String seekType;

	public PublishSeekTaskListener(Activity activity, String seekType) {
		super(activity, "正在发布" + (net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE.equals(seekType) ? "帮忙" : "求助")
				+ "...");
		this.seekType = seekType;
	}

	@Override
	public void onSuccess(SeekVO seek) {
		Log.d(TAG, "publish seek success");
		Toast.makeText(activity,
				"发布" + (net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE.equals(seekType) ? "帮忙" : "求助") + "成功！",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Constants.BROADCAST_INTENT_PUBLISH_SEEK);
		activity.sendBroadcast(intent);
		activity.finish();
	}

}
