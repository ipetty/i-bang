package net.ipetty.ibang.android.boot;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.login.LoginActivity;
import net.ipetty.ibang.android.main.MainActivity;
import android.app.Activity;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				goMain();
				SplashActivity.this.finish();
			}
		}.start();

	}

	// 转向主界面
	public void goMain() {
		AppUtils.goTo(this, MainActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}

	// 转向登陆界面
	public void goLogin() {
		AppUtils.goTo(this, LoginActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}
}
