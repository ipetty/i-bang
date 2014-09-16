package net.ipetty.ibang.android.boot;

import net.ipetty.ibang.R;
import android.app.Activity;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

}
