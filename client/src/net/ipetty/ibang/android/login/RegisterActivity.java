package net.ipetty.ibang.android.login;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.vo.RegisterVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private AutoCompleteTextView accountView;
	private String account = null;
	private EditText passwordView;
	private String password = null;
	private EditText nicknameView;
	private String nickname = null;
	private EditText phoneView = null;
	private String phone = null;
	private TextView toggleView = null;
	private boolean psdDisplayFlg = false;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register));
		btnBack.setOnClickListener(new BackClickListener(this));

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getResources().getString(R.string.logining));

		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);

		accountView = (AutoCompleteTextView) this.findViewById(R.id.account);
		passwordView = (EditText) this.findViewById(R.id.password);
		nicknameView = (EditText) this.findViewById(R.id.nickname);
		phoneView = (EditText) this.findViewById(R.id.phone);

		// 注册
		View btnView = (View) this.findViewById(R.id.button);
		btnView.setOnClickListener(regOnClick);
	}

	// 注册
	private final OnClickListener regOnClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (!validateRegister()) {
				return;
			}

			new RegisterTask(RegisterActivity.this).setListener(new RegisterTaskListener(RegisterActivity.this))
					.execute(new RegisterVO(account, password, nickname, phone));
		}
	};

	// 密码可见
	private OnClickListener togglePasswordClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			int index = passwordView.getSelectionStart();
			if (!psdDisplayFlg) {
				passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_hide);
			} else {
				passwordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_show);
			}
			psdDisplayFlg = !psdDisplayFlg;
			Editable etable = passwordView.getText();
			Selection.setSelection(etable, index);
		}
	};

	// 注册前校验
	private boolean validateRegister() {
		this.account = accountView.getText().toString();
		this.password = passwordView.getText().toString();
		this.nickname = nicknameView.getText().toString();
		this.phone = phoneView.getText().toString();

		if (StringUtils.isBlank(this.account)) {
			accountView.requestFocus();
			Toast.makeText(RegisterActivity.this, R.string.login_empty_account, Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(this.password)) {
			passwordView.requestFocus();
			Toast.makeText(RegisterActivity.this, R.string.login_empty_password, Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(this.nickname)) {
			nicknameView.requestFocus();
			Toast.makeText(RegisterActivity.this, R.string.empty_nickname, Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(this.phone) || this.phone.length() != 11) {
			phoneView.requestFocus();
			Toast.makeText(RegisterActivity.this, R.string.empty_phone, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

}
