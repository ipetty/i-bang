package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
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

	private TextView seekCount;
	private TextView offerCount;
	private TextView seekerTotalPoint;

	private TextView phoneView;
	private TextView jobView;
	private TextView gender;
	private String genderValue; // 性别传值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ActivityManager.getInstance().addActivity(this);
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
		seekCount = (TextView) this.findViewById(R.id.seekCount);
		offerCount = (TextView) this.findViewById(R.id.offerCount);
		seekerTotalPoint = (TextView) this.findViewById(R.id.seekerTotalPoint);

		phoneView = (TextView) this.findViewById(R.id.phone);
		gender = (TextView) this.findViewById(R.id.gender);
		jobView = (TextView) this.findViewById(R.id.job);

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
		signature.setText(user.getSignature());
		seekCount.setText(String.valueOf(user.getSeekCount()));
		offerCount.setText(String.valueOf(user.getOfferCount()));
		seekerTotalPoint.setText(String.valueOf(user.getSeekerTotalPoint()));

		phoneView.setText(user.getPhone());
		gender.setText(user.getGender());
		jobView.setText(user.getJob());

	}

	private void loadUser() {
		// TODO 异步加载后刷新数据
		initUser();
	}

}
