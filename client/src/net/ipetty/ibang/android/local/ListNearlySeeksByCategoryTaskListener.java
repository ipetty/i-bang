package net.ipetty.ibang.android.local;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListNearlySeeksByCategoryTaskListener
 * @author luocanfeng
 * @date 2014年11月28日
 */
public class ListNearlySeeksByCategoryTaskListener extends DefaultTaskListener<List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	private final SeekAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListNearlySeeksByCategoryTaskListener(Activity activity, SeekAdapter adapter,
			MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = true;
	}

	public ListNearlySeeksByCategoryTaskListener(Activity activity, SeekAdapter adapter,
			MyPullToRefreshListView listView, boolean isRefresh) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<SeekVO> seeks) {
		Log.d(TAG, "list latest avaliable seeks success");
		if (isRefresh) {
			adapter.loadData(seeks);
		} else {
			adapter.addData(seeks);
		}
		listView.onRefreshComplete();

		((NearlyActivity) activity).loadMoreForResult(seeks);

	}

	@Override
	public void onError(Throwable ex) {
		super.onError(ex);
		if (null != listView) {
			listView.onRefreshComplete();
		}
	}

}
