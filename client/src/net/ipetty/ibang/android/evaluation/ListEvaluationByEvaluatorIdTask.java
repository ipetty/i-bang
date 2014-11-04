package net.ipetty.ibang.android.evaluation;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.EvaluationApi;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.util.Log;

/**
 * 我给出的评价
 * 
 * @author luocanfeng
 * @date 2014年11月4日
 */
public class ListEvaluationByEvaluatorIdTask extends Task<Integer, List<EvaluationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListEvaluationByEvaluatorIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<EvaluationVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list evaluations by evaluator id");
		return IbangApi.init(activity).create(EvaluationApi.class).listByEvaluatorId(args[0], args[1], args[2]);
	}

}
