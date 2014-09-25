package net.ipetty.ibang.android.publish;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constant;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.UploadView;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.user.UserEditActivity;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PublishActivity extends Activity {
	private String title = null;
	private String categoryL1;
	private String categoryL2;
	private EditText contentView;
	private EditText exipireDateView;
	private TextView nicknameView;
	private TextView phoneView;
	private UserVO user;

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private UploadView uploadView;

	private Dialog exipireDateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		ActivityManager.getInstance().addActivity(this);

		user = new UserVO();

		categoryL1 = this.getIntent().getExtras().getString(Constant.INTENT_CATEGORY);
		categoryL2 = this.getIntent().getExtras().getString(Constant.INTENT_SUB_CATEGORY);
		title = categoryL1 + "-" + categoryL2;

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(title);
		btnBack.setOnClickListener(new BackClickListener(this));

		contentView = (EditText) this.findViewById(R.id.content);
		exipireDateView = (EditText) this.findViewById(R.id.exipireDate);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, 3);
		String str = dateFormat.format(c.getTime());
		exipireDateView.setText(str);
		exipireDateView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exipireDateDialog = DialogUtils.datePopupDialog(PublishActivity.this, exipireDateClick, exipireDateView
						.getText().toString(), exipireDateDialog);
			}
		});

		nicknameView = (TextView) this.findViewById(R.id.nickname);
		phoneView = (TextView) this.findViewById(R.id.phone);
		nicknameView.setOnClickListener(new EditOnClickListener(Constant.INTENT_USER_EDIT_TYPE_NICKNAME));
		phoneView.setOnClickListener(new EditOnClickListener(Constant.INTENT_USER_EDIT_TYPE_PHONE));

		initFileUpload();

		initUserInfo(user);
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
			Intent intent = new Intent(PublishActivity.this, UserEditActivity.class);
			intent.putExtra(Constant.INTENT_USER_EDIT_TYPE, this.type);
			PublishActivity.this.startActivityForResult(intent, Constant.REQUEST_CODE_USER_EDIT);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uploadView.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constant.REQUEST_CODE_USER_EDIT) {
			// 从上下文重新获取用户信息
			initUserInfo(user);
		}

	}

	private OnDateSetListener exipireDateClick = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			String str = dateFormat.format(c.getTime());
			exipireDateView.setText(str);
		}
	};

	private void initFileUpload() {
		uploadView = new UploadView(this);
	}

}
