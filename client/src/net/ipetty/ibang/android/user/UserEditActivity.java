package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.UserFormVO;
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
	private View job_Layout;
	private EditText nickname;
	private EditText phone;
	private EditText signature;
	private EditText job;
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
		job_Layout = this.findViewById(R.id.job_layout);

		nickname = (EditText) this.findViewById(R.id.nickname);
		phone = (EditText) this.findViewById(R.id.phone);
		signature = (EditText) this.findViewById(R.id.signature);
		job = (EditText) this.findViewById(R.id.job);

		button = this.findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserVO currentUser = ApiContext.getInstance(UserEditActivity.this).getCurrentUser();
				UserFormVO userForm = new UserFormVO();
				userForm.setId(currentUser.getId());
				userForm.setNickname(nickname.getText().toString());
				userForm.setJob(job.getText().toString());
				userForm.setPhone(phone.getText().toString());
				userForm.setSignature(signature.getText().toString());
				userForm.setGender(user.getGender());

				userForm.setTelephone(currentUser.getTelephone());
				userForm.setSpeciality(currentUser.getSpeciality());
				userForm.setPreference(currentUser.getPreference());
				userForm.setProvince(currentUser.getProvince());
				userForm.setCity(currentUser.getCity());
				userForm.setDistrict(currentUser.getDistrict());
				userForm.setAddress(currentUser.getAddress());

				// update user;
				new UpdateProfileTask(UserEditActivity.this).setListener(
						new UpdateProfileTaskListener(UserEditActivity.this)).execute(userForm);

			}
		});

		user = ApiContext.getInstance(UserEditActivity.this).getCurrentUser();

		Intent intent = getIntent();
		type = intent.getStringExtra(Constants.INTENT_USER_EDIT_TYPE);
		initType();
		nickname.setText(user.getNickname());
		phone.setText(user.getPhone());
		signature.setText(user.getSignature());
		job.setText(user.getJob());

	}

	public void updateUserSuceess() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	private void initType() {
		// TODO Auto-generated method stub
		if (Constants.INTENT_USER_EDIT_TYPE_NICKNAME.equals(type)) {
			nickname_Layout.setVisibility(View.VISIBLE);
		} else {
			nickname_Layout.setVisibility(View.GONE);
		}

		if (Constants.INTENT_USER_EDIT_TYPE_PHONE.equals(type)) {
			phone_Layout.setVisibility(View.VISIBLE);
		} else {
			phone_Layout.setVisibility(View.GONE);
		}

		if (Constants.INTENT_USER_EDIT_TYPE_SIGNATURE.equals(type)) {
			signature_Layout.setVisibility(View.VISIBLE);
		} else {
			signature_Layout.setVisibility(View.GONE);
		}

		if (Constants.INTENT_USER_EDIT_TYPE_JOB.equals(type)) {
			job_Layout.setVisibility(View.VISIBLE);
		} else {
			job_Layout.setVisibility(View.GONE);
		}
	}

}
