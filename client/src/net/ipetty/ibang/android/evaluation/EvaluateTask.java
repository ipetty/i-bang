package net.ipetty.ibang.android.evaluation;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.EvaluationApi;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.util.Log;

/**
 * EvaluateTask
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class EvaluateTask extends Task<EvaluationVO, EvaluationVO> {

	private String TAG = getClass().getSimpleName();

	public EvaluateTask(Activity activity) {
		super(activity);
	}

	@Override
	protected EvaluationVO myDoInBackground(EvaluationVO... args) {
		Log.d(TAG, "evaluate");
		return IbangApi.init(activity).create(EvaluationApi.class).evaluate(args[0]);
	}

}
