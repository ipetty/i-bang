package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.evaluation.ListEvaluationByEvaluateTargetIdTask;
import net.ipetty.ibang.android.evaluation.ListEvaluationByEvaluateTargetIdTaskListener;
import net.ipetty.ibang.android.evaluation.ListEvaluationByEvaluatorIdTask;
import net.ipetty.ibang.android.evaluation.ListEvaluationByEvaluatorIdTaskListener;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.GetUserByIdSynchronously;
import net.ipetty.ibang.android.user.UserInfoActivity;
import net.ipetty.ibang.vo.EvaluationVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyEvaluationActivity extends Activity {
	private UserVO user;
	private MyPullToRefreshListView listView_to_me; // 评价我的
	private MyPullToRefreshListView listView_from_me; // 我评价的
	private EvaluationAdapter adapter_to_me;
	private EvaluationAdapter adapter_from_me;
	private Integer pageNumber_to_me = 0;
	private Integer pageNumber_from_me = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis_to_me;
	private Long lastTimeMillis_from_me;
	private Boolean hasMore_to_me = true;
	private Boolean hasMore_from_me = true;

	private ViewFlipper viewFlipper;

	private View elvaluation_from_me_layout;
	private View elvaluation_to_me_layout;

	private View from_me_layout;
	private View to_me_layout;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_evaluation);
		ActivityManager.getInstance().addActivity(this);

		user = ApiContext.getInstance(MyEvaluationActivity.this).getCurrentUser();
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_my_evaluation);

		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		from_me_layout = this.findViewById(R.id.from_me_layout);
		to_me_layout = this.findViewById(R.id.to_me_layout);
		from_me_layout.setOnClickListener(new TabClickListener(0));
		to_me_layout.setOnClickListener(new TabClickListener(1));

		elvaluation_from_me_layout = this.findViewById(R.id.elvaluation_from_me_layout);
		elvaluation_to_me_layout = this.findViewById(R.id.elvaluation_to_me_layout);

		// form_me_layout
		listView_from_me = (MyPullToRefreshListView) this.findViewById(R.id.listView_from_me);
		adapter_from_me = new EvaluationAdapter();
		listView_from_me.setAdapter(adapter_from_me);
		listView_from_me.setOnItemClickListener(itemClick);

		listView_from_me.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MyEvaluationActivity.this, getRefreshTime_from_me(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				load_from_me(true);
			}
		});

		listView_from_me.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (hasMore_from_me) {
					load_from_me(false);
				}
			}
		});

		load_from_me(true);

		// listView_to_me
		listView_to_me = (MyPullToRefreshListView) this.findViewById(R.id.listView_to_me);
		adapter_to_me = new EvaluationAdapter();
		listView_to_me.setAdapter(adapter_to_me);
		listView_to_me.setOnItemClickListener(itemClick);

		listView_to_me.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MyEvaluationActivity.this, getRefreshTime_to_me(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				load_to_me(true);
			}
		});

		listView_to_me.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (hasMore_to_me) {
					load_to_me(false);
				}
			}
		});
		load_to_me(true);
	}

	public void load_from_me(boolean isRefresh) {
		if (isRefresh) {
			pageNumber_from_me = 0;
		}
		// 加载数据
		new ListEvaluationByEvaluatorIdTask(MyEvaluationActivity.this).setListener(
				new ListEvaluationByEvaluatorIdTaskListener(MyEvaluationActivity.this, adapter_from_me,
						listView_from_me, isRefresh)).execute(user.getId(), pageNumber_from_me++, pageSize);
	}

	public void load_to_me(boolean isRefresh) {
		if (isRefresh) {
			pageNumber_to_me = 0;
		}
		// 加载数据
		new ListEvaluationByEvaluateTargetIdTask(MyEvaluationActivity.this).setListener(
				new ListEvaluationByEvaluateTargetIdTaskListener(MyEvaluationActivity.this, adapter_to_me,
						listView_to_me, isRefresh)).execute(user.getId(), pageNumber_to_me++, pageSize);
	}

	public void loadMoreForResult_from_me(List<EvaluationVO> result) {
		if (result.size() < pageSize) {
			hasMore_from_me = false;
			listView_from_me.hideMoreView();
		} else {
			hasMore_from_me = true;
			listView_from_me.showMoreView();
		}
	}

	public void loadMoreForResult_to_me(List<EvaluationVO> result) {
		if (result.size() < pageSize) {
			hasMore_to_me = false;
			listView_to_me.hideMoreView();
		} else {
			hasMore_to_me = true;
			listView_to_me.showMoreView();
		}
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime_from_me() {
		if (NetWorkUtils.isNetworkConnected(MyEvaluationActivity.this)) {
			this.lastTimeMillis_from_me = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MyEvaluationActivity.this, this.lastTimeMillis_from_me);
			return this.lastTimeMillis_from_me;
		}
		return MyAppStateManager.getLastRefrsh4Home(MyEvaluationActivity.this);
	}

	// TODO:这里保存与获取的实际好像不太对了 因为有2个时间
	private Long getRefreshTime_to_me() {
		if (NetWorkUtils.isNetworkConnected(MyEvaluationActivity.this)) {
			this.lastTimeMillis_to_me = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MyEvaluationActivity.this, this.lastTimeMillis_to_me);
			return this.lastTimeMillis_to_me;
		}
		return MyAppStateManager.getLastRefrsh4Home(MyEvaluationActivity.this);
	}

	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			EvaluationVO vo = (EvaluationVO) parent.getAdapter().getItem(position);

			Intent intent = new Intent(MyEvaluationActivity.this, DelegationActivity.class);
			// TODO: 这里貌似 DelegationActivity 接受的都是 offerId
			// intent.putExtra(Constants.INTENT_OFFER_ID, vo.getDelegationId());
			// // 查看委托界面是通过offerId获取委托的
			// startActivity(intent);
		}
	};

	public class EvaluationAdapter extends BaseAdapter implements OnScrollListener {
		private List<EvaluationVO> list = new ArrayList<EvaluationVO>();

		public void loadData(List<EvaluationVO> data) {
			list.clear();
			this.addData(data);
		}

		public void addData(List<EvaluationVO> data) {
			list.addAll(data);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return ((EvaluationVO) this.getItem(position)).getId();
		}

		private class ViewHolder {
			View layout;
			ImageView avator;
			TextView nickname;
			TextView created_at;
			TextView content;
		}

		public ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(MyEvaluationActivity.this).inflate(R.layout.list_evaluation_item, null);
				holder = new ViewHolder();
				holder.layout = view.findViewById(R.id.layout);
				holder.avator = (ImageView) view.findViewById(R.id.avatar);
				holder.nickname = (TextView) view.findViewById(R.id.nickname);
				holder.created_at = (TextView) view.findViewById(R.id.created_at);
				holder.content = (TextView) view.findViewById(R.id.content);
				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			EvaluationVO vo = (EvaluationVO) this.getItem(position);
			bindTime(vo.getCreatedOn(), holder.created_at);
			holder.content.setText("评分:" + vo.getPoint() + "分");
			UserVO evaluator;
			if (vo.getEvaluatorId().equals(user.getId())) {
				evaluator = user;
			} else {
				evaluator = GetUserByIdSynchronously.get(MyEvaluationActivity.this, vo.getEvaluatorId());
			}
			bindUser(evaluator, holder.avator, holder.nickname);
			return view;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(date);
		time.setText(creatAt);
	}

	private void bindUser(final UserVO user, ImageView avatar, TextView nickname) {
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		}
		nickname.setText(user.getNickname());
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyEvaluationActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyEvaluationActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
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
				from_me_layout.setBackgroundResource(R.drawable.news_tab_selected);
				to_me_layout.setBackgroundResource(R.drawable.trans);

			} else {
				from_me_layout.setBackgroundResource(R.drawable.trans);
				to_me_layout.setBackgroundResource(R.drawable.news_tab_selected);
			}
		}
	}

}
