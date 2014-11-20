package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.ui.ReportClickListener;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.core.util.UserUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MyOfferActivity extends Activity {

	private MyPullToRefreshListView listView;
	private OfferAdapter adapter;

	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;
	private UserVO user;

	private List<OfferVO> offerList = new ArrayList<OfferVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_offer);
		ActivityManager.getInstance().addActivity(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_OFFER_UPDATE);
		registerReceiver(broadcastreciver, filter);

		user = ApiContext.getInstance(MyOfferActivity.this).getCurrentUser();
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_my_delegation);

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		adapter = new OfferAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OfferVO offer = (OfferVO) parent.getAdapter().getItem(position);
				if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED.equals(offer.getStatus())
						|| net.ipetty.ibang.vo.Constants.OFFER_STATUS_FINISHED.equals(offer.getStatus())) {
					Intent intent = new Intent(MyOfferActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, offer.getId()); // 查看委托界面是通过offerId获取委托的
					intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offer).toString());
					startActivity(intent);
				} else {
					Intent intent = new Intent(MyOfferActivity.this, OfferActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, id);
					intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offer).toString());
					startActivity(intent);
				}

			}
		});

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MyOfferActivity.this, getRefreshTime(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				loadOffer(true);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (hasMore) {
					loadOffer(false);
				}
			}
		});

		loadOffer(true);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastreciver);
	}

	public void loadOffer(boolean isRefresh) {
		// TODO Auto-generated method stub
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据
		new ListOfferByUserIdTask(MyOfferActivity.this).setListener(
				new ListOfferByUserIdTaskListener(MyOfferActivity.this, adapter, listView, isRefresh)).execute(
				user.getId(), pageNumber++, pageSize);
	}

	public void loadMoreForResult(List<OfferVO> result) {
		// TODO Auto-generated method stub
		if (result.size() < pageSize) {
			hasMore = false;
			listView.hideMoreView();
		} else {
			hasMore = true;
			listView.showMoreView();
		}
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(MyOfferActivity.this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MyOfferActivity.this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(MyOfferActivity.this);
	}

	public class OfferAdapter extends BaseAdapter implements OnScrollListener {

		public void loadData(List<OfferVO> data) {
			offerList.clear();
			this.addData(data);
		}

		public void addData(List<OfferVO> data) {
			offerList.addAll(data);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return offerList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return offerList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return ((OfferVO) this.getItem(position)).getId();
		}

		public void updateOffer(final Long offerId) {
			new GetOfferByIdTask(MyOfferActivity.this).setListener(
					new DefaultTaskListener<OfferVO>(MyOfferActivity.this) {
						@Override
						public void onSuccess(OfferVO result) {
							for (OfferVO offer : offerList) {
								if (offer.getId().equals(offerId)) {
									int i = offerList.indexOf(offer);
									offerList.set(i, result);
									adapter.notifyDataSetChanged();
								}
							}
						}
					}).execute(offerId);
		}

		private class ViewHolder {
			View layout;
			ImageView avator;
			TextView nickname;
			TextView created_at;
			TextView accept_button;
			TextView status;
			TextView content;
			TextView totalPoint;
			TextView delegation_info_btn;
			ImageView approve;
			ImageView more;
		}

		public ViewHolder holder;
		private ArrayList<ModDialogItem> popItems = new ArrayList<ModDialogItem>(0);
		private Dialog popDialog;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(MyOfferActivity.this).inflate(R.layout.list_offer_item, null);
				holder = new ViewHolder();
				holder.layout = view.findViewById(R.id.layout);
				holder.avator = (ImageView) view.findViewById(R.id.avatar);
				holder.nickname = (TextView) view.findViewById(R.id.nickname);
				holder.created_at = (TextView) view.findViewById(R.id.created_at);
				holder.accept_button = (TextView) view.findViewById(R.id.accept_button);
				holder.status = (TextView) view.findViewById(R.id.status);
				holder.content = (TextView) view.findViewById(R.id.content);
				holder.totalPoint = (TextView) view.findViewById(R.id.totalPoint);
				holder.delegation_info_btn = (TextView) view.findViewById(R.id.delegation_info_btn);
				holder.approve = (ImageView) view.findViewById(R.id.approve);
				holder.more = (ImageView) view.findViewById(R.id.more);

				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			final OfferVO offer = (OfferVO) this.getItem(position);
			holder.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					popItems.clear();
					popItems.add(new ModDialogItem(null, "举报", new ReportClickListener(MyOfferActivity.this,
							ReportClickListener.TYPE_OFFER, offer.getId(), new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									popDialog.cancel();
								}
							})));
					popDialog = DialogUtils.modPopupDialog(MyOfferActivity.this, popItems, popDialog);
				}
			});

			holder.status.setText(offer.getStatus());
			holder.status.setVisibility(View.VISIBLE);

			// 接受帮助按钮可见性，我的帮助列表怎么可能接受帮助？
			holder.accept_button.setVisibility(View.GONE);

			// 查看委托按钮
			holder.delegation_info_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyOfferActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, offer.getId()); // 查看委托界面是通过offerId获取委托的
					intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offer).toString());
					startActivity(intent);
				}
			});
			// 查看委托按钮可见性，求助者与委托人能看到
			if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED.equals(offer.getStatus())
					|| net.ipetty.ibang.vo.Constants.OFFER_STATUS_FINISHED.equals(offer.getStatus())) {
				holder.delegation_info_btn.setVisibility(View.VISIBLE);
				holder.status.setVisibility(View.GONE);

				if (offer.getDelegation().getStatus().equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED)
						|| offer.getDelegation().getStatus()
								.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_OFFERER_EVALUATED)
						|| offer.getDelegation().getStatus()
								.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_SEEKER_EVALUATED)
						|| offer.getDelegation().getStatus()
								.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_BI_EVALUATED)) {
					holder.delegation_info_btn.setText("查看评价");
				} else {
					holder.delegation_info_btn.setText("查看委托");
				}

			} else {
				holder.delegation_info_btn.setVisibility(View.GONE);
			}

			bindTime(offer.getCreatedOn(), holder.created_at);
			holder.content.setText(offer.getContent());
			UserUtils.bindUser(user, MyOfferActivity.this, holder.avator, holder.nickname, holder.approve);
			holder.totalPoint.setText("等级:" + String.valueOf(user.getSeekerTitle()));
			return view;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

		}
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd").format(date);
		time.setText(creatAt);
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_OFFER_UPDATE.equals(action)) {
				Long offerId = intent.getExtras().getLong(Constants.INTENT_OFFER_ID);
				updateOffer(offerId);
			}
		}

	};

	private void updateOffer(Long offerId) {
		// TODO Auto-generated method stub
		adapter.updateOffer(offerId);
	}
}
