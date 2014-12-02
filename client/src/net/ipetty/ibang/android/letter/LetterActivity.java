package net.ipetty.ibang.android.letter;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.LetterVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LetterActivity extends Activity {
	private LetterAdapter adapter; // 定义适配器
	private MyPullToRefreshListView listView;
	private EditText contentView;
	private int cooperatorId;
	private UserVO cooperator;
	private UserVO user;

	public List<LetterVO> list = new ArrayList<LetterVO>();
	private static DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_avatar).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_letter);

		// TODO:根据传递进来的ID 获取用户~ 或者在之前界面就传递 聊天对象的JSON格式过来也可以
		user = ApiContext.getInstance(this).getCurrentUser();
		cooperatorId = this.getIntent().getExtras().getInt(Constants.INTENT_LETTER_USER_ID);

		cooperator = new UserVO();

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(cooperator.getUsername());
		btnBack.setOnClickListener(new BackClickListener(this));

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		contentView = (EditText) this.findViewById(R.id.editText);

		View button = this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 点击发送消息
				String content = contentView.getText().toString();

			}

		});

		// list加载显示
		adapter = new LetterAdapter();
		listView.setAdapter(adapter);

	}

	public class LetterAdapter extends BaseAdapter implements OnScrollListener {

		public void loadData(List<LetterVO> data) {
			list.clear();
			this.addData(data);
		}

		public void addData(List<LetterVO> data) {
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
				if (StringUtils.isNotBlank(vo.getCooperatorAvatar())) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + vo.getCooperatorAvatar(),
							holder.my_avatar, options);
				}
			}

			return view;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}
}
