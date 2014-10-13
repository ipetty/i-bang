package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends Activity {
	private UserVO user;
	private UserVO seekUser;
	private String seekUserJSON;
	private int seekUserId;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private ImageView avatar;
	private TextView nickname;
	private TextView signature;

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

		avatar = (ImageView) this.findViewById(R.id.avatar);
		nickname = (TextView) this.findViewById(R.id.nickname);
		signature = (TextView) this.findViewById(R.id.signature);

		// 先从传递的参数进行加载
		if (seekUserId == user.getId()) {
			seekUser = user;
		} else {
			seekUser = JSONUtils.fromJSON(seekUserJSON, UserVO.class);
			initUser();
			loadUser();
		}

	}

	private void initUser() {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(seekUser.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + seekUser.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
		nickname.setText(seekUser.getNickname());
		// TODO : 待完善
	}

	private void loadUser() {
		// TODO 异步加载后刷新数据
		initUser();
	}

}
