package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * CloseSeekTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class CloseSeekTaskListener extends DefaultTaskListener<UserVO> {

	private String TAG = getClass().getSimpleName();

	public CloseSeekTaskListener(Activity activity) {
		super(activity, "正在关闭求助单...");
	}

	@Override
	public void onSuccess(UserVO user) {
		Log.d(TAG, "close seek success");
		Toast.makeText(activity, "关闭求助单成功！", Toast.LENGTH_SHORT).show();

		// TODO
	}

}
