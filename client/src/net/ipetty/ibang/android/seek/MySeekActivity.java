package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MySeekActivity extends Activity {

	private MyPullToRefreshListView listView;
	private SeekAdapter adapter;

	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;

	private ViewFlipper viewFlipper;

	private SeekAdapter adapter_for_help;
	private MyPullToRefreshListView listView_for_help;
	private Integer pageNumber_for_help = 0;
	private Long lastTimeMillis_for_help;
	private Boolean hasMore_for_help = true;

	private View to_help_layout;
	private View for_help_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_seek);
		ActivityManager.getInstance().addActivity(this);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_my_seek);

		to_help_layout = this.findViewById(R.id.to_help_layout);
		for_help_layout = this.findViewById(R.id.for_help_layout);
		to_help_layout.setOnClickListener(new TabClickListener(0));
		for_help_layout.setOnClickListener(new TabClickListener(1));

		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		adapter = new SeekAdapter(MySeekActivity.this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MySeekActivity.this, SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(parent.getAdapter().getItem(position))
						.toString());
				startActivity(intent);
			}
		});

		// listView.hideMoreView();
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MySeekActivity.this, getRefreshTime(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				loadSeek(true);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (hasMore) {
					loadSeek(false);
				}
			}
		});

		loadSeek(true);

		// for help
		listView_for_help = (MyPullToRefreshListView) this.findViewById(R.id.listView_for_help);
		adapter_for_help = new SeekAdapter(MySeekActivity.this);
		listView_for_help.setAdapter(adapter_for_help);
		listView_for_help.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MySeekActivity.this, SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(parent.getAdapter().getItem(position))
						.toString());
				startActivity(intent);
			}
		});

		// listView.hideMoreView();
		listView_for_help.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MySeekActivity.this, getRefreshTime_for_help(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				loadSeek_for_help(true);
			}
		});

		listView_for_help.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (hasMore_for_help) {
					loadSeek_for_help(false);
				}
			}
		});

		loadSeek_for_help(true);
	}

	public void loadSeek(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 我发布的求助
		new ListSeeksByUserIdTask(MySeekActivity.this).setListener(
				new ListSeeksByUserIdTaskListener(MySeekActivity.this, adapter, listView, isRefresh)).execute(
				net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK,
				String.valueOf(ApiContext.getInstance(MySeekActivity.this).getCurrentUserId()),
				String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(MySeekActivity.this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MySeekActivity.this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(MySeekActivity.this);
	}

	public void loadMoreForResult(List<SeekVO> result) {
		if (result.size() < pageSize) {
			hasMore = false;
			listView.hideMoreView();
		} else {
			hasMore = true;
			listView.showMoreView();
		}
	}

	// for help
	public void loadSeek_for_help(boolean isRefresh) {
		if (isRefresh) {
			pageNumber_for_help = 0;
		}
		// 我发布的帮忙
		new ListSeeksByUserIdTask(MySeekActivity.this).setListener(
				new ListSeeksByUserIdTaskListener(MySeekActivity.this, adapter_for_help, listView_for_help, isRefresh))
				.execute(net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE,
						String.valueOf(ApiContext.getInstance(MySeekActivity.this).getCurrentUserId()),
						String.valueOf(pageNumber_for_help++), String.valueOf(pageSize));
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime_for_help() {
		if (NetWorkUtils.isNetworkConnected(MySeekActivity.this)) {
			this.lastTimeMillis_for_help = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MySeekActivity.this, this.lastTimeMillis_for_help);
			return this.lastTimeMillis_for_help;
		}

		return MyAppStateManager.getLastRefrsh4Home(MySeekActivity.this);
	}

	public void loadMoreForResult(List<SeekVO> result, MyPullToRefreshListView listView) {
		if (listView == this.listView) {
			loadMoreForResult(result);
		} else {
			loadMoreForResult_for_help(result);
		}
	}

	public void loadMoreForResult_for_help(List<SeekVO> result) {
		if (result.size() < pageSize) {
			hasMore_for_help = false;
			listView_for_help.hideMoreView();
		} else {
			hasMore = true;
			listView_for_help.showMoreView();
		}
	}

	public class TabClickListener implements OnClickListener {
		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewFlipper.setDisplayedChild(index);
			if (index == 0) {
				to_help_layout.setBackgroundResource(R.drawable.news_tab_selected);
				for_help_layout.setBackgroundResource(R.drawable.trans);

			} else {
				to_help_layout.setBackgroundResource(R.drawable.trans);
				for_help_layout.setBackgroundResource(R.drawable.news_tab_selected);
			}
		}
	}

}
