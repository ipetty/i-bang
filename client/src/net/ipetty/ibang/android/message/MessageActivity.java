package net.ipetty.ibang.android.message;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.seek.DelegationActivity;
import net.ipetty.ibang.android.seek.OfferActivity;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.android.user.GetUserByIdSynchronously;
import net.ipetty.ibang.vo.SystemMessageVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageActivity extends Activity {

	private boolean isLogin = false;
	public UnLoginView unLoginView;
	public MyPullToRefreshListView listView;
	public MessageAdapter adapter;
	public List<SystemMessageVO> list = new ArrayList<SystemMessageVO>();
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;

	public String SYS_MSG_TYPE_NEW_OFFER = "帮助您的求助"; // 您的求助单有了新的帮助
	public String SYS_MSG_TYPE_NEW_DELEGATION = "接受了您的帮助"; // 您的帮助已被求助者接受
	public String SYS_MSG_TYPE_DELEGATION_FINISHED = "完成了委托"; // 您（求助者）的一个委托已被帮助者完成
	public String SYS_MSG_TYPE_DELEGATION_CLOSED = "关闭的您的委托"; // 您（求助者）的一个委托已被帮助者关闭
	public String SYS_MSG_TYPE_NEW_EVALUATION = "评价了您"; // 您收到了新的评价
	public String SYS_MSG_TYPE_SEEK_FINISHED = "您的求助已完成"; // 您（求助者）的一个求助单已完成
	public String SYS_MSG_TYPE_SEEK_CLOSED = "帮助已关闭"; // 您（帮助者）帮助的一个求助单已被求助者关闭

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_message);

		isLogin = ApiContext.getInstance(MessageActivity.this).isAuthorized();

		unLoginView = new UnLoginView(this, null, R.string.un_login_message);

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		adapter = new MessageAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SystemMessageVO msg = (SystemMessageVO) parent.getAdapter().getItem(position);
				String type = msg.getType();
				Intent intent = new Intent();
				if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_OFFER.equals(type)) {
					intent = new Intent(MessageActivity.this, OfferActivity.class);
					intent.putExtra(Constants.INTENT_SEEK_ID, msg.getSeekId());
					intent.putExtra(Constants.INTENT_OFFER_ID, msg.getOfferId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_DELEGATION.equals(type)
						|| net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_FINISHED.equals(type)
						|| net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_CLOSED.equals(type)
						|| net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_EVALUATION.equals(type)) {
					intent = new Intent(MessageActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_SEEK_ID, msg.getSeekId());
					intent.putExtra(Constants.INTENT_OFFER_ID, msg.getOfferId());
					intent.putExtra(Constants.INTENT_DELEGATION_ID, msg.getDelegationId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_FINISHED.equals(type)
						|| net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_CLOSED.equals(type)) {
					intent = new Intent(MessageActivity.this, SeekActivity.class);
					intent.putExtra(Constants.INTENT_SEEK_ID, msg.getSeekId());
				}
				startActivity(intent);

				if (!msg.isRead()) {
					final long tId = msg.getId();
					new ReadSystemMessageTask(MessageActivity.this).setListener(
							new DefaultTaskListener<Boolean>(MessageActivity.this) {
								@Override
								public void onSuccess(Boolean result) {
									// TODO Auto-generated method stub
									((MessageActivity) activity).changeToRead(tId);
								}
							}).execute(msg.getId());
				}
			}
		});

		// listView.hideMoreView();
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MessageActivity.this, getRefreshTime(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				loadSystemMessage(true);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (hasMore) {
					loadSystemMessage(false);
				}
			}
		});

		if (isLogin) {
			init();
		}
	}

	public void changeToRead(Long id) {
		adapter.changeToRead(id);
	}

	private void init() {
		unLoginView.hide();
		loadSystemMessage(true);
	}

	public void loadSystemMessage(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据
		new ListSystemMessageByUserIdTask(MessageActivity.this).setListener(
				new ListSystemMessageByUserIdTaskListener(MessageActivity.this, adapter, listView, isRefresh)).execute(
				ApiContext.getInstance(MessageActivity.this).getCurrentUserId(), pageNumber++, pageSize);
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(MessageActivity.this)) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(MessageActivity.this, this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(MessageActivity.this);
	}

	public void loadMoreForResult(List<SystemMessageVO> result) {
		if (result.size() < pageSize) {
			hasMore = false;
			listView.hideMoreView();
		} else {
			hasMore = true;
			listView.showMoreView();
		}
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}
		}
	};

	public class MessageAdapter extends BaseAdapter implements OnScrollListener {
		public void changeToRead(Long id) {
			for (SystemMessageVO msg : list) {
				if (msg.getId().equals(id)) {
					msg.setRead(true);
					this.notifyDataSetChanged();
				}
			}
		}

		public void loadData(List<SystemMessageVO> data) {
			list.clear();
			this.addData(data);
		}

		public void addData(List<SystemMessageVO> data) {
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
			return ((SystemMessageVO) getItem(position)).getId();
		}

		private class ViewHolder {
			public View item;
			public TextView nickname;
			public ImageView avatar;
			public TextView message;
			public TextView timestamp;
		}

		public ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(MessageActivity.this);
				view = inflater.inflate(R.layout.list_message_item, null);
				holder = new ViewHolder();
				holder.item = view.findViewById(R.id.item);
				holder.nickname = (TextView) view.findViewById(R.id.nickname);
				holder.avatar = (ImageView) view.findViewById(R.id.avatar);
				holder.message = (TextView) view.findViewById(R.id.message);
				holder.timestamp = (TextView) view.findViewById(R.id.timestamp);

				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			SystemMessageVO msg = (SystemMessageVO) this.getItem(position);
			holder.message.setText(msg.getTitle());
			holder.timestamp.setText(new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(msg.getCreatedOn()));

			if (msg.isRead()) {
				holder.item.setBackgroundResource(R.drawable.list_color_item_bg_white);
			} else {
				holder.item.setBackgroundResource(R.color.news_item_bg_news);
			}

			// 异步加载来源用户
			Integer fromUserId = msg.getFromUserId();
			if (fromUserId != null) {
				final UserVO user = GetUserByIdSynchronously.get(MessageActivity.this, fromUserId);
				holder.nickname.setText(user.getNickname());
				if (StringUtils.isNotEmpty(user.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(),
							holder.avatar, options);
				} else {
					holder.avatar.setImageResource(R.drawable.default_avatar);
				}
			}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(broadcastreciver);
	}

}
