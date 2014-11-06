package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListSeeksByUserIdTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class ListSeeksByUserIdTaskListener extends DefaultTaskListener<List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	private final SeekAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListSeeksByUserIdTaskListener(Activity activity, SeekAdapter adapter, MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
	}

	public ListSeeksByUserIdTaskListener(Activity activity, SeekAdapter adapter, MyPullToRefreshListView listView,
			boolean isRefresh) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<SeekVO> seeks) {
		Log.d(TAG, "list seeks by user id success");
		adapter.loadData(seeks);
		if (null != listView) {
			listView.onRefreshComplete();
		}

		if (isRefresh) {
			adapter.loadData(seeks);
		} else {
			adapter.addData(seeks);
		}
		listView.onRefreshComplete();
		if (activity instanceof MySeekActivity) {
			((MySeekActivity) activity).loadMoreForResult(seeks, listView);
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
