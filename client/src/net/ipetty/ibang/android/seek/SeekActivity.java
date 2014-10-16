package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.GetUserByIdSynchronously;
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
	private TextView closeBtn;
	private LinearLayout offerListView;
	// private LinearLayout delegationListView;
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

	private View offerBtn_layout;
	// private View delegationList_layout;
	private View offerList_layout;
	private View closeBtn_layout;

	List<DelegationVO> delegationList = new ArrayList<DelegationVO>();
	List<OfferVO> offerList = new ArrayList<OfferVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek);

		isLogin = ApiContext.getInstance(this).isAuthorized();
		user = ApiContext.getInstance(this).getCurrentUser();

		seekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);
		seekJSON = this.getIntent().getExtras().getString(Constants.INTENT_SEEK_JSON);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_seek));

		// 界面元素绑定
		// image
		imageView = this.findViewById(R.id.imageView_layout);
		viewPager = (ViewPager) findViewById(R.id.vp);
		imageViewText = (TextView) this.findViewById(R.id.imageView_text);
		offerBtn = (TextView) this.findViewById(R.id.offerBtn);
		closeBtn = (TextView) this.findViewById(R.id.closeBtn);
		// delegationListView = (LinearLayout)
		// this.findViewById(R.id.delegationList);
		offerListView = (LinearLayout) this.findViewById(R.id.offerList);
		content = (TextView) this.findViewById(R.id.content);
		closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_created_at = (TextView) this.findViewById(R.id.seek_created_at);
		seek_username = (TextView) this.findViewById(R.id.seek_username);
		seek_avatar = (ImageView) this.findViewById(R.id.seek_avatar);

		offerBtn_layout = this.findViewById(R.id.offerBtn_layout);
		// delegationList_layout =
		// this.findViewById(R.id.delegationList_layout);
		offerList_layout = this.findViewById(R.id.offerList_layout);
		closeBtn_layout = this.findViewById(R.id.closeBtn_layout);

		// 事件绑定
		offerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SeekActivity.this, PublishOfferActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, seekId);
				startActivity(intent);
			}
		});

		// 关闭
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 加载数据
		if (seekJSON != null) {
			seekVO = JSONUtils.fromJSON(seekJSON, SeekVO.class);
		}

		// 可以本地加载
		initImageView();
		initContent();

		// 需要从服务器加载
		initSeekUser();

		// 数据重新加载后显示
		initViewLayout();
		// initDelegationView();
		initOfferView();
	}

	private void initSeekUser() {
		if (isSeekOwner()) {
			seekUser = user;
			initSeekUserLayout();
		} else {
			seekUser = GetUserByIdSynchronously.get(SeekActivity.this, seekVO.getSeekerId());
			initSeekUserLayout();
		}
	}

	private void initSeekUserLayout() {
		bindUser(seekUser, seek_avatar, seek_username);
	}

	private void initContent() {
		// TODO Auto-generated method stub
		seek_avatar.setImageResource(R.drawable.default_avatar);
		content.setText(seekVO.getContent());
		if (seekVO.getClosedOn() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getClosedOn());
			closedOn.setText(DateUtils.toDateString(c.getTime()));
		}
		bindTime(seekVO.getCreatedOn(), seek_created_at);
	}

	private void initViewLayout() {
		// TODO Auto-generated method stub
		String status = seekVO.getStatus();
		offerBtn_layout.setVisibility(View.GONE);
		closeBtn_layout.setVisibility(View.GONE);

		if (isLogin) { // 只有已登录用户才有可能看到这两个按钮
			if (!net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(status)
					&& !net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(status)) {
				if (isSeekOwner()) {
					closeBtn_layout.setVisibility(View.VISIBLE);
				} else {
					offerBtn_layout.setVisibility(View.VISIBLE);
				}

				if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_DELEGATED.equals(status) && !isSeekOwner()) {
					offerBtn_layout.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	private boolean isSeekOwner() {
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

	/*
	 * private class DelegationHolder { View layout; ImageView avator; TextView
	 * nickname; TextView created_at; TextView more; TextView phone; }
	 * 
	 * private void initDelegationView() { // TODO: 从服务端加载 delegationList
	 * DelegationVO tt = new DelegationVO(); delegationList.add(tt);
	 * delegationList.add(tt);
	 * 
	 * if (delegationList.size() == 0) {
	 * delegationList_layout.setVisibility(View.GONE); } else {
	 * delegationList_layout.setVisibility(View.VISIBLE); }
	 * 
	 * // TODO Auto-generated method stub delegationListView.removeAllViews();
	 * for (DelegationVO delegationVO : delegationList) {
	 * delegationListView.addView(getItemDelegationView(delegationVO)); } }
	 * 
	 * public View getItemDelegationView(DelegationVO delegationVO) { View view
	 * = LayoutInflater.from(this).inflate(R.layout.list_delegation_simple_item,
	 * null); DelegationHolder holder = new DelegationHolder(); holder.layout =
	 * view.findViewById(R.id.layout); holder.avator = (ImageView)
	 * view.findViewById(R.id.avatar); holder.nickname = (TextView)
	 * view.findViewById(R.id.nickname); holder.created_at = (TextView)
	 * view.findViewById(R.id.created_at); holder.more = (TextView)
	 * view.findViewById(R.id.more); holder.phone = (TextView)
	 * view.findViewById(R.id.phone);
	 * 
	 * holder.phone.setVisibility(View.GONE);
	 * 
	 * final Long id = delegationVO.getId(); if (isSeekOwner()) {
	 * holder.phone.setVisibility(View.VISIBLE);
	 * holder.more.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent intent = new
	 * Intent(SeekActivity.this, DelegationActivity.class);
	 * intent.putExtra(Constants.INTENT_OFFER_ID, id); startActivity(intent); }
	 * }); }
	 * 
	 * return view; }
	 */

	private class OfferHolder {
		View layout;
		ImageView avator;
		TextView nickname;
		TextView created_at;
		TextView accept_button;
		TextView status;
		TextView content;
		TextView totalPoint;
		TextView delegation_info_btn;
	}

	private void initOfferView() {
		offerListView.removeAllViews();
		new ListOfferBySeekIdTask(SeekActivity.this).setListener(
				new DefaultTaskListener<List<OfferVO>>(SeekActivity.this) {
					@Override
					public void onSuccess(List<OfferVO> offers) {
						if (offers.size() == 0) {
							offerList_layout.setVisibility(View.GONE);
						} else {
							offerList_layout.setVisibility(View.VISIBLE);
						}

						for (OfferVO offer : offers) {
							View view = getItemOfferView(offer);
							offerListView.addView(view);
						}
					}
				}).execute(seekId);
	}

	public View getItemOfferView(final OfferVO offer) {
		View view = LayoutInflater.from(this).inflate(R.layout.list_offer_item, null);
		final OfferHolder holder = new OfferHolder();
		holder.layout = view.findViewById(R.id.layout);
		holder.avator = (ImageView) view.findViewById(R.id.avatar);
		holder.nickname = (TextView) view.findViewById(R.id.nickname);
		holder.created_at = (TextView) view.findViewById(R.id.created_at);
		holder.accept_button = (TextView) view.findViewById(R.id.accept_button);
		holder.status = (TextView) view.findViewById(R.id.status);
		holder.content = (TextView) view.findViewById(R.id.content);
		holder.totalPoint = (TextView) view.findViewById(R.id.totalPoint);
		holder.delegation_info_btn = (TextView) view.findViewById(R.id.delegation_info_btn);

		holder.accept_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DelegationVO delegation = new DelegationVO();
				delegation.setSeekId(seekId);
				delegation.setOfferId(offer.getId());
				new AcceptOfferTask(SeekActivity.this).setListener(
						new DefaultTaskListener<DelegationVO>(SeekActivity.this) {
							@Override
							public void onSuccess(DelegationVO result) {
								// 接受应征后进行界面操作
								// holder.status.setText("已委托");
								holder.delegation_info_btn.setVisibility(View.VISIBLE); // 查看委托按钮
								holder.accept_button.setVisibility(View.GONE);
								holder.status.setVisibility(View.VISIBLE);
							}
						}).execute(delegation);
			}
		});

		holder.delegation_info_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 查看委托按钮
			}
		});

		String status = "";

		if (offer.getStatus().equals(net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED)) {
			status = "应征中";
		} else {
			status = "已委托";
		}
		bindTime(offer.getCreatedOn(), holder.created_at);
		holder.status.setText(status);
		holder.content.setText(offer.getContent());

		holder.delegation_info_btn.setVisibility(View.GONE); // 查看委托按钮

		// TODO: 如果是应征人员 应该和 seekOwner权限一致 待修改逻辑
		if (isSeekOwner()) {
			holder.status.setVisibility(View.GONE);
			if (offer.getStatus().equals(net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED)) {
				holder.accept_button.setVisibility(View.VISIBLE);
			} else {
				holder.delegation_info_btn.setVisibility(View.VISIBLE);
				// TODO: 看到的 offer的内容是否变成电话号码？？？？
			}
		} else {
			holder.accept_button.setVisibility(View.GONE);
			holder.status.setVisibility(View.VISIBLE);
		}

		int userId = offer.getOffererId();
		UserVO user = GetUserByIdSynchronously.get(SeekActivity.this, userId);
		bindUser(user, holder.avator, holder.nickname);
		holder.totalPoint.setText("积分" + String.valueOf(user.getSeekerTotalPoint()));
		return view;
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(date);
		time.setText(creatAt);
	}

	private void bindUser(final UserVO user, ImageView avatar, TextView nickname) {
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		}
		nickname.setText(user.getNickname());
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
	}
}
