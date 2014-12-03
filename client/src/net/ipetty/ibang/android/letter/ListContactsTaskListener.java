package net.ipetty.ibang.android.letter;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.message.MessageActivity;
import net.ipetty.ibang.android.message.MessageActivity.LetterContactsAdapter;
import net.ipetty.ibang.vo.LetterContacts;
import android.app.Activity;
import android.util.Log;

/**
 * ListContactsTaskListener
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class ListContactsTaskListener extends DefaultTaskListener<List<LetterContacts>> {

	private String TAG = getClass().getSimpleName();

	private final LetterContactsAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListContactsTaskListener(Activity activity, LetterContactsAdapter adapter, MyPullToRefreshListView listView,
			boolean isRefresh) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<LetterContacts> contacts) {
		Log.d(TAG, "list contacts success");
		adapter.loadData(contacts);
		if (null != listView) {
			listView.onRefreshComplete();
		}

		if (isRefresh) {
			adapter.loadData(contacts);
		} else {
			adapter.addData(contacts);
		}
		listView.onRefreshComplete();
		if (activity instanceof LetterActivity) {
			((MessageActivity) activity).loadMoreContactsForResult(contacts);
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
