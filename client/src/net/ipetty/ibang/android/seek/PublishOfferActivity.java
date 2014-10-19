package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserEditActivity;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PublishOfferActivity extends Activity {

	private EditText content;
	private TextView button;
	private TextView nicknameView;
	private TextView phoneView;
	private UserVO user;
	private Long seekId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_offer);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_publish_offer));

		seekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);

		content = (EditText) this.findViewById(R.id.content);

		nicknameView = (TextView) this.findViewById(R.id.nickname);
		phoneView = (TextView) this.findViewById(R.id.phone);
		nicknameView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_NICKNAME));
		phoneView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_PHONE));

		user = ApiContext.getInstance(PublishOfferActivity.this).getCurrentUser();
		initUserInfo(user);

		button = (TextView) this.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isBlank(content.getText().toString())) {
					Toast.makeText(PublishOfferActivity.this, "应征内容不能为空", Toast.LENGTH_SHORT).show();
					content.requestFocus();
					return;
				}

				OfferVO offer = new OfferVO();
				offer.setContent(content.getText().toString());
				offer.setSeekId(seekId);
				offer.setOffererId(user.getId());
				new PublishOfferTask(PublishOfferActivity.this).setListener(
						new PublishOfferTaskListener(PublishOfferActivity.this)).execute(offer);
			}
		});
	}

	private void initUserInfo(UserVO user) {
		// TODO Auto-generated method stub
		nicknameView.setText(user.getNickname());
		phoneView.setText(user.getPhone());
	}

	private class EditOnClickListener implements OnClickListener {
		private String type = null;

		public EditOnClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PublishOfferActivity.this, UserEditActivity.class);
			intent.putExtra(Constants.INTENT_USER_EDIT_TYPE, this.type);
			PublishOfferActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_USER_EDIT);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQUEST_CODE_USER_EDIT) {
			// 从上下文重新获取用户信息
			user = ApiContext.getInstance(PublishOfferActivity.this).getCurrentUser();
			initUserInfo(user);
		}
	}

}
