package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserProfileActivity;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends Activity {
	private boolean isLogin = false;
	private Button logout;
	private ImageView avatar; // 头像
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private UnLoginView unLoginView;
	private TextView account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityManager.getInstance().addActivity(this);

		isLogin = ApiContext.getInstance(this).isAuthorized();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_setting));
		btnBack.setOnClickListener(new BackClickListener(this));

		unLoginView = new UnLoginView(this, null, R.string.un_login_setting);

		account = (TextView) this.findViewById(R.id.account);

		avatar = (ImageView) this.findViewById(R.id.avatar);
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, UserProfileActivity.class);
				startActivity(intent);
			}
		});

		/* password */
		View password = this.findViewById(R.id.password_layout);
		password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this, ChangePwdActivity.class);
				startActivity(intent);
			}
		});

		/* logout */
		logout = (Button) this.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new LogoutTask(SettingActivity.this).setListener(new LogoutTaskListener(SettingActivity.this))
						.execute();
			}
		});

		if (isLogin) {
			init();
		}
	}

	private void init() {
		unLoginView.hide();
		initUser();
	}

	private void initUser() {
		// TODO Auto-generated method stub
		UserVO user = ApiContext.getInstance(SettingActivity.this).getCurrentUser();
		account.setText(user.getUsername());
		if (StringUtils.isNotEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}
			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastreciver);
	}
}
