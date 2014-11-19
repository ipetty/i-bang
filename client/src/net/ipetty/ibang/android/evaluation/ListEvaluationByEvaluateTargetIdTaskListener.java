package net.ipetty.ibang.android.evaluation;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.seek.MyEvaluationActivity;
import net.ipetty.ibang.android.seek.MyEvaluationActivity.EvaluationAdapter;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.util.Log;

/**
 * 我得到的评价
 * 
 * @author luocanfeng
 * @date 2014年11月4日
 */
public class ListEvaluationByEvaluateTargetIdTaskListener extends DefaultTaskListener<List<EvaluationVO>> {

	private String TAG = getClass().getSimpleName();

	private final EvaluationAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListEvaluationByEvaluateTargetIdTaskListener(Activity activity, EvaluationAdapter adapter,
			MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
	}

	public ListEvaluationByEvaluateTargetIdTaskListener(Activity activity, EvaluationAdapter adapter,
			MyPullToRefreshListView listView, boolean isRefresh) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<EvaluationVO> evaluations) {
		Log.d(TAG, "list evaluations by evaluate target id success");
		if (isRefresh) {
			adapter.loadData(evaluations);
		} else {
			adapter.addData(evaluations);
		}
		listView.onRefreshComplete();
		if (activity instanceof MyEvaluationActivity) {
			((MyEvaluationActivity) activity).loadMoreForResult_to_me(evaluations);
		}
	}

	@Override
	public void onError(Throwable ex) {
		super.onError(ex);
		if (null != listView) {
			listView.onRefreshComplete();
		}
	}

}
