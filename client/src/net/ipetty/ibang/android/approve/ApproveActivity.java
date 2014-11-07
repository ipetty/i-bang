package net.ipetty.ibang.android.approve;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ApproveActivity extends Activity {
	private UserVO user;
	private UserVO seekUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approve);
		ActivityManager.getInstance().addActivity(this);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_approve));

		user = ApiContext.getInstance(ApproveActivity.this).getCurrentUser();

	}

}
