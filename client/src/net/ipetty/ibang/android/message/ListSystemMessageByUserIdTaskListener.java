package net.ipetty.ibang.android.message;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.message.MessageActivity.MessageAdapter;
import net.ipetty.ibang.vo.SystemMessageVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListSystemMessageByUserIdTaskListener
 * 
 * @author luocanfeng
 * @date 2014年10月22日
 */
public class ListSystemMessageByUserIdTaskListener extends DefaultTaskListener<List<SystemMessageVO>> {

	private String TAG = getClass().getSimpleName();

	private final MessageAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListSystemMessageByUserIdTaskListener(Activity activity, MessageAdapter adapter,
			MyPullToRefreshListView listView) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
	}

	public ListSystemMessageByUserIdTaskListener(Activity activity, MessageAdapter adapter,
			MyPullToRefreshListView listView, boolean isRefresh) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<SystemMessageVO> systemMessages) {
		Log.d(TAG, "list system messages by user id success");
		adapter.loadData(systemMessages);
		if (null != listView) {
			listView.onRefreshComplete();
		}

		if (isRefresh) {
			adapter.loadData(systemMessages);
		} else {
			adapter.addData(systemMessages);
		}
		listView.onRefreshComplete();
		if (activity instanceof MessageActivity) {
			((MessageActivity) activity).loadMoreForResult(systemMessages);
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
