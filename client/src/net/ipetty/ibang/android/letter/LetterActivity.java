package net.ipetty.ibang.android.letter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import m.framework.ui.widget.pulltorefresh.OnScrollListener;
import m.framework.ui.widget.pulltorefresh.Scrollable;
import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.GetUserByIdTask;
import net.ipetty.ibang.vo.LetterVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LetterActivity extends Activity {

	private LetterAdapter adapter; // 定义适配器
	private MyPullToRefreshListView listView;
	private Integer pageNumber = 0;
	private final Integer pageSize = 5;
	private Boolean hasMore = true;

	private EditText contentView;
	private int cooperatorId;
	private UserVO cooperator;
	private UserVO user;
	TextView text;

	public List<LetterVO> list = new ArrayList<LetterVO>();
	private static DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_avatar).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_letter);

		user = ApiContext.getInstance(this).getCurrentUser();
		cooperatorId = this.getIntent().getExtras().getInt(Constants.INTENT_LETTER_USER_ID);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		text = (TextView) this.findViewById(R.id.action_bar_title);

		// 获取聊天对象信息
		new GetUserByIdTask(LetterActivity.this).setListener(new DefaultTaskListener<UserVO>(LetterActivity.this) {

			@Override
			public void onSuccess(UserVO result) {
				cooperator = result;
				text.setText(cooperator.getNickname());
			}
		}).execute(cooperatorId);

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		contentView = (EditText) this.findViewById(R.id.editText);

		View button = this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击发送消息
				String content = contentView.getText().toString();
				new SendLetterTask(LetterActivity.this).setListener(
						new SendLetterTaskListener(LetterActivity.this, adapter)).execute(String.valueOf(cooperatorId),
						content);
			}
		});

		// list加载显示
		adapter = new LetterAdapter();
		listView.setAdapter(adapter);
		loadData(true);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				loadData(false);
			}

		});

	}

	public void publicSuccess() {
		listView.getRefreshableView().setSelection(list.size() - 1);
		contentView.setText("");
		Toast.makeText(this, "消息发送成功", Toast.LENGTH_SHORT).show();
		finish();
	}

	// 获取聊天信息
	public void loadData(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载聊天信息
		new ListLettersTask(LetterActivity.this).setListener(
				new ListLettersTaskListener(LetterActivity.this, adapter, listView, isRefresh)).execute(cooperatorId,
				pageNumber++, pageSize);
	}

	public class LetterAdapter extends BaseAdapter implements OnScrollListener {

		// 第一次加载刷新
		public void loadData(List<LetterVO> data) {
			list.clear();
			// TODO 这个看看是否需要修复
			Collections.reverse(data);
			this.addData(data);
		}

		// 认为发布完成后增加的
		public void addData(List<LetterVO> data) {
			list.addAll(data);
			this.notifyDataSetChanged();
		}

		// 加载更多你是消息
		public void addBeforeData(List<LetterVO> data) {
			Collections.reverse(data);
			list.addAll(0, data);
			this.notifyDataSetChanged();
			// listView.getRefreshableView().setSelection(data.size() - 1);

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
			return ((LetterVO) getItem(position)).getId();
		}

		private class ViewHolder {

			View other_send_layout;
			View my_send_layout;
			ImageView my_avatar;
			ImageView other_avatar;
			TextView other_content;
			TextView my_content;
		}

		public ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(LetterActivity.this).inflate(R.layout.list_letter_item, null);
				holder = new ViewHolder();
				holder.other_send_layout = view.findViewById(R.id.other_send_layout);
				holder.other_content = (TextView) holder.other_send_layout.findViewById(R.id.content);
				holder.other_avatar = (ImageView) holder.other_send_layout.findViewById(R.id.avatar);
				holder.my_send_layout = view.findViewById(R.id.my_send_layout);
				holder.my_content = (TextView) holder.my_send_layout.findViewById(R.id.content);
				holder.my_avatar = (ImageView) holder.my_send_layout.findViewById(R.id.avatar);
				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			LetterVO vo = (LetterVO) this.getItem(position);
			if (vo.isInbox()) {
				// 收到的
				holder.other_send_layout.setVisibility(View.VISIBLE);
				holder.my_send_layout.setVisibility(View.GONE);

				holder.other_content.setText(vo.getContent());
				if (StringUtils.isNotBlank(vo.getCooperatorAvatar())) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + vo.getCooperatorAvatar(),
							holder.other_avatar, options);
				}
			} else {
				// 发出的
				holder.other_send_layout.setVisibility(View.GONE);
				holder.my_send_layout.setVisibility(View.VISIBLE);
				holder.my_content.setText(vo.getContent());
				if (StringUtils.isNotBlank(user.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(),
							holder.my_avatar, options);
				}
			}

			return view;
		}

		@Override
		public void onScrollChanged(Scrollable arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub

		}

	}

	public void loadMoreForResult(List<LetterVO> result) {
		if (result.size() < pageSize) {
			hasMore = false;
			listView.hideMoreView();

		} else {
			hasMore = true;
			listView.hideMoreView();
		}
	}

}
