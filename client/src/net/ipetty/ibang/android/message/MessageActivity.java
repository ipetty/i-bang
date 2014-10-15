package net.ipetty.ibang.android.message;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.seek.DelegationActivity;
import net.ipetty.ibang.android.seek.OfferActivity;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.vo.SystemMessageVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageActivity extends Activity {
	private boolean isLogin = true;
	public UnLoginView unLoginView;
	public ListView listView;
	public MessageAdapter adapter;
	public List<SystemMessageVO> list = new ArrayList<SystemMessageVO>();
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	public String SYS_MSG_TYPE_NEW_OFFER = "应征您的求助"; // 您的求助单有了新的应征
	public String SYS_MSG_TYPE_NEW_DELEGATION = "接受了您的应征"; // 您的应征已被求助者接受
	public String SYS_MSG_TYPE_DELEGATION_FINISHED = "完成了委托"; // 您（求助者）的一个委托已被帮助者完成
	public String SYS_MSG_TYPE_DELEGATION_CLOSED = "关闭的您的委托"; // 您（求助者）的一个委托已被帮助者关闭
	public String SYS_MSG_TYPE_NEW_EVALUATION = "评价了您"; // 您收到了新的评价
	public String SYS_MSG_TYPE_SEEK_FINISHED = "您的求助已完成"; // 您（求助者）的一个求助单已完成
	public String SYS_MSG_TYPE_SEEK_CLOSED = "帮助已关闭"; // 您（帮助者）应征的一个求助单已被求助者关闭

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
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_message);
		btnBack.setOnClickListener(new BackClickListener(this));

		unLoginView = new UnLoginView(this, null, R.string.un_login_message);

		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new MessageAdapter();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SystemMessageVO msg = (SystemMessageVO) adapter.getItem(position);
				String type = msg.getType();
				Intent intent = new Intent();
				if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_OFFER.equals(type)) {
					intent = new Intent(MessageActivity.this, OfferActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, msg.getOfferId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_DELEGATION.equals(type)) {
					intent = new Intent(MessageActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_DELEGATION_ID, msg.getDelegationId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_FINISHED.equals(type)) {
					intent = new Intent(MessageActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_DELEGATION_ID, msg.getDelegationId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_CLOSED.equals(type)) {
					intent = new Intent(MessageActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_DELEGATION_ID, msg.getDelegationId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_EVALUATION.equals(type)) {
					intent = new Intent(MessageActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_DELEGATION_ID, msg.getDelegationId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_FINISHED.equals(type)) {
					intent = new Intent(MessageActivity.this, SeekActivity.class);
					intent.putExtra(Constants.INTENT_SEEK_ID, msg.getSeekId());
				} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_CLOSED.equals(type)) {
					intent = new Intent(MessageActivity.this, SeekActivity.class);
					intent.putExtra(Constants.INTENT_SEEK_ID, msg.getSeekId());
				}
				startActivity(intent);
			}

		});

		if (isLogin) {
			init();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		unLoginView.hide();

	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}

		}
	};

	public void loadDate(List<SystemMessageVO> list) {
		list.clear();
		list.addAll(list);
		adapter.notifyDataSetChanged();
	}

	public class MessageAdapter extends BaseAdapter implements OnScrollListener {
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
			String type = msg.getType();
			String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(msg.getCreatedOn());
			holder.timestamp.setText(creatAt);

			if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_OFFER.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_NEW_OFFER);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_DELEGATION.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_NEW_OFFER);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_FINISHED.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_DELEGATION_FINISHED);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_DELEGATION_CLOSED.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_DELEGATION_CLOSED);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_NEW_EVALUATION.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_NEW_EVALUATION);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_FINISHED.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_SEEK_FINISHED);
			} else if (net.ipetty.ibang.vo.Constants.SYS_MSG_TYPE_SEEK_CLOSED.equals(type)) {
				holder.message.setText(SYS_MSG_TYPE_SEEK_CLOSED);
			}

			// 异步加载来源用户
			int userId = msg.getFromUserId();
			final UserVO user = new UserVO();
			holder.nickname.setText(user.getNickname());
			if (StringUtils.isNotEmpty(user.getAvatar())) {
				ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), holder.avatar,
						options);
			} else {
				holder.avatar.setImageResource(R.drawable.default_avatar);
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
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(broadcastreciver);
	}
}
