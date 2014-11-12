package net.ipetty.ibang.android.boot;

import java.util.Random;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.main.MainActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AdActivity extends Activity {
	private int[] images = { R.drawable.ad1, R.drawable.ad2 };

	private String[] strs = { "广告1", "广告2" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);

		int imageIndex = new Random().nextInt(images.length);
		int strIndex = new Random().nextInt(strs.length);

		ImageView imageView = (ImageView) this.findViewById(R.id.ad);
		imageView.setImageResource(images[imageIndex]);

		TextView textView = (TextView) this.findViewById(R.id.title);
		textView.setText(strs[strIndex]);

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				goMain();
				finish();
			}
		}.start();
	}

	// 转向主界面
	public void goMain() {
		AppUtils.goTo(this, MainActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}
}
