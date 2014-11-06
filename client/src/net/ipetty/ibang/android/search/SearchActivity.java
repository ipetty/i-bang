package net.ipetty.ibang.android.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.main.SeekAdapter;
import net.ipetty.ibang.android.seek.ListLatestAvaliableSeeksByKeywordTask;
import net.ipetty.ibang.android.seek.ListLatestAvaliableSeeksTaskListener;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;

public class SearchActivity extends Activity {
	private ImageView btn;
	private ImageView search_del;
	private EditText search;
	private String key;
	private View search_result;
	private MyPullToRefreshListView listView;
	private SeekAdapter adapter;
	private Boolean hasMore = true;
	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;

	private TextView typeView;
	private String type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		btn = (ImageView) this.findViewById(R.id.action_bar_right_image);
		btn.setOnClickListener(searchOnClickListener);

		search = (EditText) this.findViewById(R.id.search);
		search_del = (ImageView) this.findViewById(R.id.search_del);
		search_result = this.findViewById(R.id.search_result);

		typeView = (TextView) this.findViewById(R.id.type);
		typeView.setText(type);
		View type_layout = this.findViewById(R.id.type_layout);
		type_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK.equals(type)) {
					type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE;
				} else {
					type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;
				}
				typeView.setText(type);
			}
		});

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		adapter = new SeekAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SearchActivity.this, SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(parent.getAdapter().getItem(position))
						.toString());
				startActivity(intent);
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

		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					search_del.setVisibility(View.VISIBLE);
				} else {
					search_del.setVisibility(View.INVISIBLE);
				}
			}
		});

		search_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search.setText("");
				search_result.setVisibility(View.GONE);
			}
		});

		search_del.setVisibility(View.INVISIBLE);
	}

	private OnClickListener searchOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			key = search.getText().toString();
			adapter.loadData(new ArrayList<SeekVO>());
			loadSeek(true);
			search_result.setVisibility(View.VISIBLE);
		}
	};

	public void loadSeek(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据
		new ListLatestAvaliableSeeksByKeywordTask(SearchActivity.this).setListener(
				new ListLatestAvaliableSeeksTaskListener(SearchActivity.this, adapter, listView, isRefresh)).execute(
				type, key, net.ipetty.ibang.android.core.util.DateUtils.toDatetimeString(new Date(getRefreshTime())),
				String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(this);
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
