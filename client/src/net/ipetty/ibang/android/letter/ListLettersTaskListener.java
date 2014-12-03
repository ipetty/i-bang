package net.ipetty.ibang.android.letter;

import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.letter.LetterActivity.LetterAdapter;
import net.ipetty.ibang.vo.LetterVO;
import android.app.Activity;
import android.util.Log;

/**
 * SendLetterTaskListener
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class ListLettersTaskListener extends DefaultTaskListener<List<LetterVO>> {

	private String TAG = getClass().getSimpleName();

	private final LetterAdapter adapter;
	private final MyPullToRefreshListView listView;
	private boolean isRefresh;

	public ListLettersTaskListener(Activity activity, LetterAdapter adapter, MyPullToRefreshListView listView,
			boolean isRefresh) {
		super(activity);
		this.adapter = adapter;
		this.listView = listView;
		this.isRefresh = isRefresh;
	}

	@Override
	public void onSuccess(List<LetterVO> letters) {
		Log.d(TAG, "list letters success");
		adapter.loadData(letters);
		if (null != listView) {
			listView.onRefreshComplete();
		}

		if (isRefresh) {
			adapter.loadData(letters);
		} else {
			adapter.addData(letters);
		}
		listView.onRefreshComplete();
		if (activity instanceof LetterActivity) {
			((LetterActivity) activity).loadMoreForResult(letters);
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
