package net.ipetty.ibang.android.evaluation;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.EvaluationApi;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListEvaluationByDelegationIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月16日
 */
public class ListEvaluationByDelegationIdTask extends Task<Long, List<EvaluationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListEvaluationByDelegationIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<EvaluationVO> myDoInBackground(Long... args) {
		Log.d(TAG, "list evaluations by delegation id");
		return IbangApi.init(activity).create(EvaluationApi.class).listByDelegationId(args[0]);
	}

}
