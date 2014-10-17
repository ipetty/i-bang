package net.ipetty.ibang.android.evaluation;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * EvaluateTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class EvaluateTaskListener extends DefaultTaskListener<EvaluationVO> {

	private String TAG = getClass().getSimpleName();

	public EvaluateTaskListener(Activity activity) {
		super(activity, "保存评价中...");
	}

	@Override
	public void onSuccess(EvaluationVO evaluation) {
		Log.d(TAG, "evaluate success");
		Toast.makeText(activity, "评价成功！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Constants.BROADCAST_INTENT_EVALUATE);
		activity.sendBroadcast(intent);
		activity.finish();
	}

}
