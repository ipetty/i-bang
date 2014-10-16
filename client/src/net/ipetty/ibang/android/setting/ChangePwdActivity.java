package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePwdActivity extends Activity {

	private boolean psdDisplayFlg = false;
	private TextView toggleView = null;
	private EditText oldPasswordEditor;
	private EditText passwordEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_change_pwd));
		btnBack.setOnClickListener(new BackClickListener(this));

		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);

		oldPasswordEditor = (EditText) this.findViewById(R.id.oldPassword);
		passwordEditor = (EditText) this.findViewById(R.id.password);

		View btnView = (View) this.findViewById(R.id.button);
		btnView.setOnClickListener(btnOnClick);
	}

	// 密码可见
	private OnClickListener togglePasswordClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int index = passwordEditor.getSelectionStart();
			if (!psdDisplayFlg) {
				// display password text, for example "123456"
				// passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_hide);
			} else {
				// hide password, display "."
				// passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_show);
			}
			psdDisplayFlg = !psdDisplayFlg;
			Editable etable = passwordEditor.getText();
			Selection.setSelection(etable, index);
		}
	};

	private OnClickListener btnOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String oldPassword = ChangePwdActivity.this.oldPasswordEditor.getText().toString();
			String password = ChangePwdActivity.this.passwordEditor.getText().toString();

			if (StringUtils.isEmpty(oldPassword)) {
				ChangePwdActivity.this.oldPasswordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringUtils.isEmpty(password)) {
				ChangePwdActivity.this.passwordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			} else if (password.length() < 6) {
				ChangePwdActivity.this.passwordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, "密码不得少于6位", Toast.LENGTH_SHORT).show();
				return;
			}

			new ChangePasswordTask(ChangePwdActivity.this).setListener(
					new ChangePasswordTaskListener(ChangePwdActivity.this)).execute(oldPassword, password);
		}
	};

}
