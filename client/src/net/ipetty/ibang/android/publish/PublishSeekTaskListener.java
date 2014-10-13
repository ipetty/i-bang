package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
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

	public PublishSeekTaskListener(Activity activity) {
		super(activity, "正在发布求助...");
	}

	@Override
	public void onSuccess(SeekVO seek) {
		Log.d(TAG, "publish seek success");
		Toast.makeText(activity, "发布求助成功！", Toast.LENGTH_SHORT).show();
		activity.finish();
	}

}
