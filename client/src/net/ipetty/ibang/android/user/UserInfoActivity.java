package net.ipetty.ibang.android.user;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
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

	private TextView provinceView;
	private TextView cityView;
	private TextView districtView;
	private TextView seekerTitleView;
	private TextView categoryView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ActivityManager.getInstance().addActivity(this);
		user = ApiContext.getInstance(UserInfoActivity.this).getCurrentUser();

		seekUserId = this.getIntent().getExtras().getInt(Constants.INTENT_USER_ID);

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

		provinceView = (TextView) this.findViewById(R.id.province);
		cityView = (TextView) this.findViewById(R.id.city);
		districtView = (TextView) this.findViewById(R.id.district);
		categoryView = (TextView) this.findViewById(R.id.category);

		seekerTitleView = (TextView) this.findViewById(R.id.seekerTitle);

		// 先从传递的参数进行加载
		if (user != null && seekUserId == user.getId()) {
			seekUser = user;
		} else {
			seekUser = GetUserByIdSynchronously.get(UserInfoActivity.this, seekUserId);
		}

		initUser();

	}

	private void initUser() {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(seekUser.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + seekUser.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
		nickname.setText(seekUser.getNickname());
		signature.setText(seekUser.getSignature());
		seekCount.setText(String.valueOf(seekUser.getSeekCount()));
		offerCount.setText(String.valueOf(seekUser.getOfferCount()));
		seekerTotalPoint.setText(String.valueOf(seekUser.getSeekerTotalPoint()));
		seekerTitleView.setText(seekUser.getSeekerTitle());

		// phoneView.setText(seekUser.getPhone());
		gender.setText(seekUser.getGender());
		jobView.setText(seekUser.getJob());

		provinceView.setText(user.getProvince());
		cityView.setText(user.getCity());
		districtView.setText(user.getDistrict());

		StringBuffer str = new StringBuffer();
		for (SeekCategory category : user.getOfferRange()) {
			String l1 = category.getCategoryL1();
			String l2 = category.getCategoryL2();
			str.append("<br>");
			str.append(l1 + "-" + l2);
		}
		String cat = str.toString();
		if (cat.length() > 0) {
			cat = cat.substring(4);
		}
		categoryView.setText(Html.fromHtml(cat));
	}

	private void loadUser() {
		// TODO 异步加载后刷新数据
		initUser();
	}

}
