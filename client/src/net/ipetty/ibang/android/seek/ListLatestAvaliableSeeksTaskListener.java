package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
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

	public ListLatestAvaliableSeeksTaskListener(Activity activity, SeekAdapter adapter, MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
	}

	@Override
	public void onSuccess(List<SeekVO> seeks) {
		Log.d(TAG, "list latest avaliable seek success");
		adapter.loadData(seeks);
		if (null != listView) {
			listView.onRefreshComplete();
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
