package net.ipetty.ibang.android.login;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.main.MainActivity;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
	private EditText passwordView;
	private String account = null;
	private String password = null;
	private TextView toggleView = null;
	private boolean psdDisplayFlg = false;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

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

		// 注册
		View BtnView = (View) this.findViewById(R.id.button);
		BtnView.setOnClickListener(regOnClick);
	}

	// 登录
	private final OnClickListener regOnClick = new OnClickListener() {
		@Override
		public void onClick(View loginBtnView) {
			if (!validateRegister()) {
				return;
			}

			Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
			startActivity(intent);
			RegisterActivity.this.finish();
		}
	};

	// 密码可见
	private OnClickListener togglePasswordClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
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

		return true;
	}

}
