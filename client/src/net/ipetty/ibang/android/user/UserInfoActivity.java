package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends Activity {
	private UserVO user;
	private UserVO seekUser;
	private String seekUserJSON;
	private int seekUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		user = ApiContext.getInstance(UserInfoActivity.this).getCurrentUser();

		seekUserId = this.getIntent().getExtras().getInt(Constants.INTENT_USER_ID);
		seekUserJSON = this.getIntent().getExtras().getString(Constants.INTENT_USER_JSON);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_user_info));
	}

}
