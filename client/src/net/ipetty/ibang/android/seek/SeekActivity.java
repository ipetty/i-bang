package net.ipetty.ibang.android.seek;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserInfoActivity;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SeekActivity extends Activity {

	private boolean isLogin = false;
	private TextView offerBtn;
	private LinearLayout offerListView;
	private LinearLayout delegationListView;
	private View imageView;
	private ViewPager viewPager;
	private TextView content;
	private TextView closedOn;
	private TextView seek_created_at;
	private TextView seek_username;
	private ImageView seek_avatar;

	private List<ImageView> imageViews = new ArrayList<ImageView>(); // 滑动的图片
	private int currentItem = 0;// 当前图片的索引号
	private TextView imageViewText;
	private SeekVO seekVO = null;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private UserVO user;

	private Long seekId = null;
	private String seekJSON = null;
	private UserVO seekUser;

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek);

		user = ApiContext.getInstance(this).getCurrentUser();

		seekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);
		seekJSON = this.getIntent().getExtras().getString(Constants.INTENT_SEEK_JSON);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_seek));
		btnBack.setOnClickListener(new BackClickListener(this));

		// 界面元素绑定
		// image
		imageView = this.findViewById(R.id.imageView_layout);
		viewPager = (ViewPager) findViewById(R.id.vp);
		imageViewText = (TextView) this.findViewById(R.id.imageView_text);
		offerBtn = (TextView) this.findViewById(R.id.offer);
		delegationListView = (LinearLayout) this.findViewById(R.id.delegationList);
		offerListView = (LinearLayout) this.findViewById(R.id.offerList);
		content = (TextView) this.findViewById(R.id.content);
		closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_created_at = (TextView) this.findViewById(R.id.seek_created_at);
		seek_username = (TextView) this.findViewById(R.id.seek_username);
		seek_avatar = (ImageView) this.findViewById(R.id.seek_avatar);

		// 事件绑定
		offerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SeekActivity.this, PublishOfferActivity.class);
				startActivity(intent);
			}
		});

		// 加载数据
		if (seekJSON != null) {
			seekVO = JSONUtils.fromJSON(seekJSON, SeekVO.class);
		}

		// 模拟数据
		seekVO = new SeekVO();
		ImageVO img = new ImageVO();
		seekVO.getImages().add(img);
		seekVO.getImages().add(img);

		List<DelegationVO> delegationList = new ArrayList<DelegationVO>();
		DelegationVO tt = new DelegationVO();
		delegationList.add(tt);
		delegationList.add(tt);

		List<OfferVO> offerList = new ArrayList<OfferVO>();
		OfferVO t = new OfferVO();
		offerList.add(t);
		offerList.add(t);

		// 数据加载
		initImageView();
		initContent();

		// 需要加载用户
		initSeekUser();

		// 数据重新加载后显示
		initViewLayout();
		initDelegationView(delegationList);
		initOfferView(offerList);

	}

	private void initSeekUser() {
		// TODO Auto-generated method stub
		if (isOwner()) {
			seekUser = user;
			initSeekUserLayout();
		} else {
			// TODO: 异步加载获取
			seekUser = user;
			initSeekUserLayout();
		}

	}

	private void initSeekUserLayout() {
		if (StringUtils.isNotBlank(seekUser.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), seek_avatar, options);
		}
		seek_username.setText(seekUser.getNickname());

		seek_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, seekUser.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(seekUser).toString());
				startActivity(intent);
			}
		});
		seek_username.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, seekUser.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(seekUser).toString());
				startActivity(intent);
			}
		});
	}

	private void initContent() {
		// TODO Auto-generated method stub
		seek_avatar.setImageResource(R.drawable.default_avatar);
		content.setText(seekVO.getContent());
		Calendar c = Calendar.getInstance();
		c.setTime(seekVO.getClosedOn());
		String str = dateFormat.format(c.getTime());
		closedOn.setText(str);
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(seekVO.getCreatedOn());
		seek_created_at.setText(creatAt);
	}

	private void initViewLayout() {
		// TODO Auto-generated method stub
		String status = seekVO.getStatus();

		if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_CREATED.equals(status)) {

		}

		if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_OFFERED.equals(status)) {

		}

		if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_DELEGATED.equals(status)) {

		}
		if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(status)) {

		}

		if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(status)) {

		}

	}

	private boolean isOwner() {
		if (!isLogin) {
			return false;
		}
		return seekVO.getSeekerId().equals(user.getId());
	}

	private void initImageView() {
		List<ImageVO> imageList = seekVO.getImages();
		if (imageList.size() == 0) {
			imageView.setVisibility(View.GONE);
			return;
		}

		for (ImageVO imageVO : imageList) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			String url = imageVO.getSmallUrl();
			if (StringUtils.isNotEmpty(url)) {
				ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + url, imageView, options);
			} else {
				imageView.setImageResource(R.drawable.default_seek_images);
			}
			imageView.setOnClickListener(new ImageOnClickListener(imageVO));
			imageViews.add(imageView);
		}

		imageView.setVisibility(View.VISIBLE);
		// TODO Auto-generated method stub
		viewPager.setAdapter(new pageAdapter());// 设置填充Viewpager 页面的适配器
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		setImageViewText();
	}

	public class ImageOnClickListener implements OnClickListener {
		private ImageVO imageVO;

		public ImageOnClickListener(ImageVO imageVO) {
			this.imageVO = imageVO;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 展示大图
			if (StringUtils.isNotEmpty(this.imageVO.getSmallUrl())) {
				Intent intent = new Intent(SeekActivity.this, LargerImageActivity.class);
				intent.putExtra(Constants.INTENT_IMAGE_ORIGINAL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getOriginalUrl());
				intent.putExtra(Constants.INTENT_IMAGE_SAMILL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getSmallUrl());
				startActivity(intent);
			}
		}
	}

	private void setImageViewText() {
		// TODO Auto-generated method stub
		String text = String.valueOf(currentItem + 1) + "/" + String.valueOf(imageViews.size());
		imageViewText.setText(text);
	}

	private class pageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			currentItem = position;
			setImageViewText();
		}
	}

	private void initDelegationView(List<DelegationVO> delegationList) {
		// TODO Auto-generated method stub
		delegationListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (DelegationVO delegationVO : delegationList) {
			View view = inflater.inflate(R.layout.list_delegation_simple_item, null);
			delegationListView.addView(view);
		}
	}

	private void initOfferView(List<OfferVO> offerList) {
		// TODO Auto-generated method stub
		offerListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (OfferVO offer : offerList) {
			View view = inflater.inflate(R.layout.list_offer_item, null);
			offerListView.addView(view);
		}
	}

}
