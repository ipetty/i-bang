package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.seek.MyOfferActivity.OfferAdapter;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListOfferByUserIdTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class ListOfferByUserIdTaskListener extends DefaultTaskListener<List<OfferVO>> {

	private String TAG = getClass().getSimpleName();

	private final OfferAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListOfferByUserIdTaskListener(Activity activity, OfferAdapter adapter, MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
	}

	public ListOfferByUserIdTaskListener(Activity activity, OfferAdapter adapter, MyPullToRefreshListView listView,
			boolean isRefresh) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<OfferVO> offers) {
		Log.d(TAG, "list offers by user id success");
		adapter.loadData(offers);
		if (null != listView) {
			listView.onRefreshComplete();
		}

		if (isRefresh) {
			adapter.loadData(offers);
		} else {
			adapter.addData(offers);
		}
		listView.onRefreshComplete();
		if (activity instanceof MySeekActivity) {
			((MyOfferActivity) activity).loadMoreForResult(offers);
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
