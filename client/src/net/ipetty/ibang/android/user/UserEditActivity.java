package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constant;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserEditActivity extends Activity {
	private String type;
	private View nickname_Layout;
	private View phone_Layout;
	private View signature_Layout;
	private EditText nickname;
	private EditText phone;
	private EditText signature;
	private View button;
	private UserVO user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(R.string.title_activity_user_edit);
		btnBack.setOnClickListener(new BackClickListener(this));

		nickname_Layout = this.findViewById(R.id.nickname_layout);
		phone_Layout = this.findViewById(R.id.phone_layout);
		signature_Layout = this.findViewById(R.id.signature_layout);

		nickname = (EditText) this.findViewById(R.id.nickname);
		phone = (EditText) this.findViewById(R.id.phone);
		signature = (EditText) this.findViewById(R.id.signature);

		button = this.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				user.setNickname(nickname.getText().toString());
				user.setPhone(phone.getText().toString());
				user.setSignature(signature.getText().toString());
				// TODO update user;

				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		user = ((MyApplication) getApplicationContext()).getUser();

		Intent intent = getIntent();
		type = intent.getStringExtra(Constant.INTENT_USER_EDIT_TYPE);
		initType();

		nickname.setText(user.getNickname());
		phone.setText(user.getPhone());
		signature.setText(user.getSignature());

	}

	private void initType() {
		// TODO Auto-generated method stub
		if (Constant.INTENT_USER_EDIT_TYPE_NICKNAME.equals(type)) {
			nickname_Layout.setVisibility(View.VISIBLE);
		} else {
			nickname_Layout.setVisibility(View.GONE);
		}

		if (Constant.INTENT_USER_EDIT_TYPE_PHONE.equals(type)) {
			phone_Layout.setVisibility(View.VISIBLE);
		} else {
			phone_Layout.setVisibility(View.GONE);
		}

		if (Constant.INTENT_USER_EDIT_TYPE_SIGNATURE.equals(type)) {
			signature_Layout.setVisibility(View.VISIBLE);
		} else {
			signature_Layout.setVisibility(View.GONE);
		}
	}

}
