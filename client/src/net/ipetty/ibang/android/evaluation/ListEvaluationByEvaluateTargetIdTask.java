package net.ipetty.ibang.android.evaluation;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.EvaluationApi;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.util.Log;

/**
 * 我得到的评价
 * 
 * @author luocanfeng
 * @date 2014年11月4日
 */
public class ListEvaluationByEvaluateTargetIdTask extends Task<Integer, List<EvaluationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListEvaluationByEvaluateTargetIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<EvaluationVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list evaluations by evaluate target id");
		return IbangApi.init(activity).create(EvaluationApi.class).listByEvaluateTargetId(args[0], args[1], args[2]);
	}

}
