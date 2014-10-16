package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.main.MainHomeFragment;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * ListLatestAvaliableSeeksTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class ListLatestAvaliableSeeksTaskListener extends DefaultTaskListener<List<SeekVO>> {

	private String TAG = getClass().getSimpleName();

	private final SeekAdapter adapter;
	private final MyPullToRefreshListView listView;
	private Fragment fragment;
	private boolean isRefresh;

	public ListLatestAvaliableSeeksTaskListener(Activity activity, SeekAdapter adapter, MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = true;
	}

	public ListLatestAvaliableSeeksTaskListener(Fragment fragment, SeekAdapter adapter, MyPullToRefreshListView listView) {
		super(fragment);
		this.fragment = fragment;
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = true;
	}

	public ListLatestAvaliableSeeksTaskListener(Activity activity, SeekAdapter adapter,
			MyPullToRefreshListView listView, boolean isRefresh) {
		// TODO Auto-generated constructor stub
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	public ListLatestAvaliableSeeksTaskListener(Fragment fragment, SeekAdapter adapter,
			MyPullToRefreshListView listView, boolean isRefresh) {
		// TODO Auto-generated constructor stub
		super(fragment);
		this.fragment = fragment;
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
		if (fragment instanceof MainHomeFragment) {
			((MainHomeFragment) fragment).loadMoreForResult(seeks);
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
