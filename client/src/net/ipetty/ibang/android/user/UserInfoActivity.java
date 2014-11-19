package net.ipetty.ibang.android.user;

import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.seek.ListSeeksByUserIdTask;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

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

	private ViewFlipper viewFlipper;
	TextView to_help_tab;
	TextView for_help_tab;
	TextView info_tab;
	TextView seekerTotalPoint_tab;

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
			initUser();
		} else {
			new GetUserByIdTask(UserInfoActivity.this).setListener(
					new DefaultTaskListener<UserVO>(UserInfoActivity.this) {

						@Override
						public void onSuccess(UserVO result) {
							seekUser = result;
							initUser();
						}
					}).execute(seekUserId);
		}

		View to_help_tab_layout = this.findViewById(R.id.to_help_tab_layout);
		View for_help_tab_layout = this.findViewById(R.id.for_help_tab_layout);
		View info_tab_layout = this.findViewById(R.id.info_tab_layout);
		View seekerTotalPoint_tab_layout = this.findViewById(R.id.seekerTotalPoint_tab_layout);

		to_help_tab_layout.setOnClickListener(new TabClickListener(0));
		for_help_tab_layout.setOnClickListener(new TabClickListener(1));
		info_tab_layout.setOnClickListener(new TabClickListener(2));
		seekerTotalPoint_tab_layout.setOnClickListener(new TabClickListener(3));

		to_help_tab = (TextView) this.findViewById(R.id.to_help_tab);
		for_help_tab = (TextView) this.findViewById(R.id.for_help_tab);
		info_tab = (TextView) this.findViewById(R.id.info_tab);
		seekerTotalPoint_tab = (TextView) this.findViewById(R.id.seekerTotalPoint_tab);

		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		init_to_help();
		init_for_help();

		final ScrollView view = (ScrollView) findViewById(R.id.scrollView);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						if (v.getScrollY() <= 0) {
							Log.d("scroll view", "top");
						} else if (view.getChildAt(0).getMeasuredHeight() <= v.getHeight() + v.getScrollY()) {
							Log.d("scroll view", "bottom");
							switch (viewFlipper.getDisplayedChild()) {
								case 0: {
									loadSeek_to_help();
									break;
								}
								case 1: {
									loadSeek_for_help();
									break;
								}
							}

						}
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

	private UserInfoRender to_help_render;
	private LoadFooter to_help_load;

	private void init_to_help() {
		// TODO Auto-generated method stub
		to_help_render = new UserInfoRender(this, this.findViewById(R.id.to_help_layout));
		LinearLayout view = (LinearLayout) this.findViewById(R.id.to_help_layout);
		to_help_load = new LoadFooter(this, view);
		loadSeek_to_help();
	}

	private int pageNumber_to_help = 0;
	private int pageSize = 10;

	public void loadSeek_to_help() {
		if (!to_help_load.isShow()) {
			return;
		}
		// 我发布的求助
		new ListSeeksByUserIdTask(this).setListener(new DefaultTaskListener<List<SeekVO>>(this) {

			@Override
			public void onSuccess(List<SeekVO> result) {
				to_help_render.addData(result);
				if (result.size() < pageSize) {
					to_help_load.hideMoreView();
				} else {
					to_help_load.showMoreView();
				}
			}
		}).execute(net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK, String.valueOf(seekUserId),
				String.valueOf(pageNumber_to_help++), String.valueOf(pageSize));
	}

	private UserInfoRender for_help_render;
	private LoadFooter for_help_load;

	private void init_for_help() {
		// TODO Auto-generated method stub
		for_help_render = new UserInfoRender(this, this.findViewById(R.id.for_help_layout));
		LinearLayout view = (LinearLayout) this.findViewById(R.id.for_help_layout);
		for_help_load = new LoadFooter(this, view);
		loadSeek_for_help();
	}

	private int pageNumber_for_help = 0;

	public void loadSeek_for_help() {
		if (!for_help_load.isShow()) {
			return;
		}
		// 我发布的求助
		new ListSeeksByUserIdTask(this).setListener(new DefaultTaskListener<List<SeekVO>>(this) {

			@Override
			public void onSuccess(List<SeekVO> result) {
				for_help_render.addData(result);
				if (result.size() < pageSize) {
					for_help_load.hideMoreView();
				} else {
					for_help_load.showMoreView();
				}
			}
		}).execute(net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE, String.valueOf(seekUser.getId()),
				String.valueOf(pageNumber_for_help++), String.valueOf(pageSize));
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
		seekerTitleView.setText("等级:" + seekUser.getSeekerTitle());

		// phoneView.setText(seekUser.getPhone());

		gender.setText(seekUser.getGender());
		jobView.setText(seekUser.getJob());

		if (StringUtils.isEmpty(seekUser.getGender())) {
			gender.setVisibility(View.GONE);
		}
		if (StringUtils.isEmpty(seekUser.getJob())) {
			jobView.setVisibility(View.GONE);
		}

		if (StringUtils.isEmpty(seekUser.getCity())) {
			cityView.setVisibility(View.GONE);
			districtView.setVisibility(View.GONE);
		}

		provinceView.setText(seekUser.getProvince());
		cityView.setText(seekUser.getCity());
		districtView.setText(seekUser.getDistrict());

		StringBuffer str = new StringBuffer();
		for (SeekCategory category : seekUser.getOfferRange()) {
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

	public class TabClickListener implements OnClickListener {

		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewFlipper.setDisplayedChild(index);
			to_help_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.gray_color));
			for_help_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.gray_color));
			info_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.gray_color));
			seekerTotalPoint_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.gray_color));

			switch (index) {

				case 0: {
					to_help_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.base_color));
					break;
				}
				case 1: {
					for_help_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.base_color));
					break;
				}
				case 2: {
					info_tab.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.base_color));
					break;
				}
				case 3: {
					seekerTotalPoint_tab
							.setTextColor(UserInfoActivity.this.getResources().getColor(R.color.base_color));
				}
			}

		}
	}

	public class LoadFooter {

		private LayoutInflater mInflater;
		private RelativeLayout mMoreView;
		private TextView mText;
		protected ImageView mImage;
		protected ProgressBar mProgress;
		private LinearLayout content;

		public LoadFooter(Context context) {
			this.init(context, null);
		}

		public LoadFooter(Context context, LinearLayout view) {
			this.init(context, view);
		}

		public void init(Context context, LinearLayout view) {
			mInflater = (LayoutInflater) LayoutInflater.from(context);
			mMoreView = (RelativeLayout) mInflater.inflate(R.layout.pull_to_refreshing, null, false);
			mImage = (ImageView) mMoreView.findViewById(R.id.pull_to_refresh_image);
			RotateAnimation mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mRotateAnimation.setInterpolator(new LinearInterpolator());
			mRotateAnimation.setDuration(1200);
			mRotateAnimation.setRepeatCount(Animation.INFINITE);
			mRotateAnimation.setRepeatMode(Animation.RESTART);
			mImage.startAnimation(mRotateAnimation);
			if (view == null) {
				content.addView(mMoreView);
			} else {
				view.addView(mMoreView);
			}
		}

		public boolean isShow() {
			return mMoreView.getVisibility() == View.VISIBLE;
		}

		public void showMoreView() {
			// TODO Auto-generated method stub
			mMoreView.setVisibility(View.VISIBLE);
		}

		public void hideMoreView() {
			// TODO Auto-generated method stub
			mMoreView.setVisibility(View.GONE);
		}
	}

}
