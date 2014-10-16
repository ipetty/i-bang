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
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserInfoActivity;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDelegationActivity extends Activity {
	private MyPullToRefreshListView listView;
	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;
	private UserVO user;

	private List<OfferVO> list = new ArrayList<OfferVO>();
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_delegation);
		ActivityManager.getInstance().addActivity(this);

		user = ApiContext.getInstance(MyDelegationActivity.this).getCurrentUser();
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_my_delegation);
		btnBack.setOnClickListener(new BackClickListener(this));

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		OfferAdapter adapter = new OfferAdapter();
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MyDelegationActivity.this, getRefreshTime(),
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
	}

	public void loadOffer(boolean isRefresh) {
		// TODO Auto-generated method stub
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据

	}

	public void loadMoreForResult(List<SeekVO> result) {
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
		if (NetWorkUtils.isNetworkConnected(MyDelegationActivity.this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MyDelegationActivity.this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(MyDelegationActivity.this);
	}

	public class OfferAdapter extends BaseAdapter implements OnScrollListener {

		public void loadData(List<OfferVO> data) {
			list.clear();
			this.addData(data);
		}

		public void addData(List<OfferVO> data) {
			list.addAll(data);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return ((OfferVO) this.getItem(position)).getId();
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
		}

		public ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(MyDelegationActivity.this).inflate(R.layout.list_offer_item, null);
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

				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			final OfferVO offer = (OfferVO) this.getItem(position);
			holder.status.setText(offer.getStatus());
			holder.status.setVisibility(View.VISIBLE);

			// 接受应征按钮
			holder.accept_button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DelegationVO delegation = new DelegationVO();
					delegation.setSeekId(offer.getSeekId());
					delegation.setOfferId(offer.getId());
					new AcceptOfferTask(MyDelegationActivity.this).setListener(
							new DefaultTaskListener<DelegationVO>(MyDelegationActivity.this) {
								@Override
								public void onSuccess(DelegationVO result) {
									// 接受应征后进行界面操作
									holder.accept_button.setVisibility(View.GONE);
									holder.delegation_info_btn.setVisibility(View.VISIBLE); // 查看委托按钮
									offer.setStatus(net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED);
								}
							}).execute(delegation);
				}
			});

			// 接受应征按钮可见性
			if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED.equals(offer.getStatus())) {
				holder.accept_button.setVisibility(View.VISIBLE);
				holder.status.setVisibility(View.GONE);
			} else {
				holder.accept_button.setVisibility(View.GONE);
			}

			// 查看委托按钮
			holder.delegation_info_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyDelegationActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, offer.getId()); // 查看委托界面是通过offerId获取委托的
					intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offer).toString());
					intent.putExtra(Constants.INTENT_SEEK_ID, offer.getSeekId());
					// TODO: FIX
					// intent.putExtra(Constants.INTENT_SEEK_JSON, seekJSON);
					startActivity(intent);
				}
			});
			// 查看委托按钮可见性，求助者与委托人能看到
			if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED.equals(offer.getStatus())
					|| net.ipetty.ibang.vo.Constants.OFFER_STATUS_FINISHED.equals(offer.getStatus())) {
				holder.delegation_info_btn.setVisibility(View.VISIBLE);
				holder.status.setVisibility(View.GONE);
			} else {
				holder.delegation_info_btn.setVisibility(View.GONE);
			}

			bindTime(offer.getCreatedOn(), holder.created_at);
			holder.content.setText(offer.getContent());
			bindUser(user, holder.avator, holder.nickname);
			holder.totalPoint.setText("积分" + String.valueOf(user.getSeekerTotalPoint()));
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
				Intent intent = new Intent(MyDelegationActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyDelegationActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
	}
}
