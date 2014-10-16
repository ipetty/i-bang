package net.ipetty.ibang.android.setting;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserProfileActivity;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends Activity {

	private Button logout;
	private ImageView avatar; // 头像
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityManager.getInstance().addActivity(this);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_setting));
		btnBack.setOnClickListener(new BackClickListener(this));

		UserVO user = ApiContext.getInstance(SettingActivity.this).getCurrentUser();
		TextView account = (TextView) this.findViewById(R.id.account);
		account.setText(user.getUsername());

		avatar = (ImageView) this.findViewById(R.id.avatar);
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, UserProfileActivity.class);
				startActivity(intent);
			}
		});
		if (StringUtils.isNotEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
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
	}

}
