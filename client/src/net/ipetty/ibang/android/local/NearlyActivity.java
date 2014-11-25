package net.ipetty.ibang.android.local;

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
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.android.type.SelectCategoryActivity;
import net.ipetty.ibang.android.type.SelectSeekTypeActivity;
import net.ipetty.ibang.vo.SeekVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class NearlyActivity extends Activity {

	private boolean isLogin = false;
	private TextView search;
	private String category = "";
	private String subCategory = "";
	private String type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;
	private MyPullToRefreshListView listView;
	private SeekAdapter adapter;

	private Button categoryView, typeView;
	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;

	private Integer currentUserId; // 当前用户ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearly);
		ActivityManager.getInstance().addActivity(this);

		// 获取当前用户
		currentUserId = ApiContext.getInstance(this).getCurrentUserId();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		((TextView) this.findViewById(R.id.action_bar_title)).setText(R.string.title_activity_nearly);

		categoryView = (Button) this.findViewById(R.id.category);
		View category_layout = this.findViewById(R.id.category_layout);
		categoryView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NearlyActivity.this, SelectCategoryActivity.class);
				intent.putExtra(Constants.INTENT_CATEGORY, category);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
			}
		});
		category_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NearlyActivity.this, SelectCategoryActivity.class);
				intent.putExtra(Constants.INTENT_CATEGORY, category);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
			}
		});

		typeView = (Button) this.findViewById(R.id.type);
		View type_layout = this.findViewById(R.id.type_layout);
		typeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NearlyActivity.this, SelectSeekTypeActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_TYPE, type);
				startActivityForResult(intent, Constants.REQUEST_CODE_TYPE);
			}
		});
		type_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NearlyActivity.this, SelectSeekTypeActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_TYPE, type);
				startActivityForResult(intent, Constants.REQUEST_CODE_TYPE);
			}
		});

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		adapter = new SeekAdapter(this, R.layout.list_seek_local_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(NearlyActivity.this, SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(parent.getAdapter().getItem(position))
						.toString());
				startActivity(intent);
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(NearlyActivity.this.getApplicationContext(), getRefreshTime(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				loadNearlySeeks(true);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (hasMore) {
					loadNearlySeeks(false);
				}
			}
		});

		// TODO 定位并加载附近数据
		// loadNearlySeeks(true);
		listView.hideMoreView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastreciver);
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				isLogin = true;
				init();
			}
			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}
			if (Constants.BROADCAST_INTENT_PUBLISH_SEEK.equals(action)) {
				loadNearlySeeks(true);
			}
		}

		private void init() {
			adapter.notifyDataSetChanged();
		}

		private void initUser() {
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQUEST_CODE_CATEGORY) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				category = intent.getStringExtra(Constants.INTENT_CATEGORY);
				subCategory = intent.getStringExtra(Constants.INTENT_SUB_CATEGORY);
				setCategoryText(category, subCategory);

				loadNearlySeeks(true);
			}
		}

		else if (requestCode == Constants.REQUEST_CODE_TYPE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				type = intent.getStringExtra(Constants.INTENT_SEEK_TYPE);
				typeView.setText(type);

				loadNearlySeeks(true);
			}
		}
	}

	private void loadNearlySeeks(boolean refresh) {
		if (SelectCategoryActivity.CATEGORY_MY_STRING.equals(subCategory)) {
			// 根据我的帮忙范围加载求助/帮忙列表
			loadNearlySeekByOfferRange(refresh);
		} else {
			// 根据城市与分类加载求助/帮忙列表
			loadNearlySeekCategory(refresh);
		}
	}

	public void loadNearlySeekCategory(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// TODO 加载数据
		// new ListLatestAvaliableSeeksByCityOrCategoryTask(this).setListener(
		// new ListLatestAvaliableSeeksTaskListener(NearlyActivity.this,
		// adapter, listView, isRefresh)).execute(
		// type, city, district, category, subCategory,
		// net.ipetty.ibang.android.core.util.DateUtils.toDatetimeString(new
		// Date(getRefreshTime())),
		// String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	public void loadNearlySeekByOfferRange(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// TODO 加载数据
		// new
		// ListLatestAvaliableSeeksByCityAndOfferRangeTask(this).setListener(
		// new ListLatestAvaliableSeeksTaskListener(NearlyActivity.this,
		// adapter, listView, isRefresh)).execute(
		// type, city, district, currentUserId == null ? null :
		// currentUserId.toString(),
		// net.ipetty.ibang.android.core.util.DateUtils.toDatetimeString(new
		// Date(getRefreshTime())),
		// String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(this);
	}

	private void setCategoryText(String category, String subCategory) {
		String str = subCategory;
		if (StringUtils.isEmpty(str)) {
			str = category;
		}
		if (StringUtils.isEmpty(str)) {
			str = "全部分类";
		}
		categoryView.setText(str);
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

}
