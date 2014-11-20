package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.ui.ReportClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.core.util.UserUtils;
import net.ipetty.ibang.android.login.LoginActivity;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private TextView number;
	private TextView phone;
	private TextView seekerTitle;
	private TextView additionalReward;
	private TextView serviceDate;
	private ImageView seek_approve;

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
	private View offerList_layout;
	private View closeBtn_layout;
	private View contact_layout;
	private View login_layout;
	private View additionalReward_layout;
	private View offerList_empty_layout;
	private View more;
	private ArrayList<ModDialogItem> popItems;
	private Dialog popDialog;

	List<DelegationVO> delegationList = new ArrayList<DelegationVO>();
	List<OfferVO> offerList = new ArrayList<OfferVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek);

		ActivityManager.getInstance().addActivity(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		filter.addAction(Constants.BROADCAST_INTENT_PUBLISH_OFFER);
		this.registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_seek));

		isLogin = ApiContext.getInstance(this).isAuthorized();
		user = ApiContext.getInstance(this).getCurrentUser();

		seekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);
		seekJSON = this.getIntent().getExtras().getString(Constants.INTENT_SEEK_JSON);

		// 界面元素绑定
		imageView = this.findViewById(R.id.imageView_layout);
		viewPager = (ViewPager) findViewById(R.id.vp);
		imageViewText = (TextView) this.findViewById(R.id.imageView_text);
		offerBtn = (TextView) this.findViewById(R.id.offerBtn);
		closeBtn = (TextView) this.findViewById(R.id.closeBtn);
		offerListView = (LinearLayout) this.findViewById(R.id.offerList);
		content = (TextView) this.findViewById(R.id.content);
		closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_created_at = (TextView) this.findViewById(R.id.seek_created_at);
		seek_username = (TextView) this.findViewById(R.id.seek_username);
		seek_avatar = (ImageView) this.findViewById(R.id.seek_avatar);
		number = (TextView) this.findViewById(R.id.number);
		seekerTitle = (TextView) this.findViewById(R.id.seekerTitle);
		additionalReward = (TextView) this.findViewById(R.id.additionalReward);
		serviceDate = (TextView) this.findViewById(R.id.serviceDate);
		seek_approve = (ImageView) this.findViewById(R.id.seek_approve);

		contact_layout = this.findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
		phone = (TextView) this.findViewById(R.id.phone);

		offerBtn_layout = this.findViewById(R.id.offerBtn_layout);
		offerBtn_layout.setVisibility(View.GONE);
		offerList_layout = this.findViewById(R.id.offerList_layout);
		offerList_empty_layout = this.findViewById(R.id.offerList_empty_layout);
		closeBtn_layout = this.findViewById(R.id.closeBtn_layout);
		closeBtn_layout.setVisibility(View.GONE);
		login_layout = this.findViewById(R.id.login_layout);
		additionalReward_layout = this.findViewById(R.id.additionalReward_layout);
		more = this.findViewById(R.id.more);

		popItems = new ArrayList<ModDialogItem>();

		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popItems.clear();
				popItems.add(new ModDialogItem(null, "举报", new ReportClickListener(SeekActivity.this,
						ReportClickListener.TYPE_SEEK, seekId, new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popDialog.cancel();
							}
						})));
				popDialog = DialogUtils.modPopupDialog(SeekActivity.this, popItems, popDialog);
			}
		});

		// 事件绑定
		offerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SeekActivity.this, PublishOfferActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, seekId);
				startActivity(intent);
			}
		});

		// 关闭求助
		closeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new CloseSeekTask(SeekActivity.this).setListener(new CloseSeekTaskListener(SeekActivity.this)).execute(
						seekId);
			}
		});

		// 触发到登陆界面
		login_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, LoginActivity.class);
				SeekActivity.this.startActivity(intent);
			}
		});

		// 加载数据
		if (seekJSON != null) {
			seekVO = JSONUtils.fromJSON(seekJSON, SeekVO.class);
			preLoad();
		}
		loadSeekFromService();
	}

	private void loadSeekFromService() {
		new GetSeekByIdTask(SeekActivity.this).setListener(new DefaultTaskListener<SeekVO>(SeekActivity.this) {

			@Override
			public void onSuccess(SeekVO result) {
				seekVO = result;
				preLoad();
				init();
			}
		}).execute(seekId);
	}

	private void loadSeekUserFromService() {
		if (isSeekOwner()) {
			seekUser = user;
		} else {
			seekUser = seekVO.getSeeker();
		}
	}

	private void preLoad() {
		initImageView();
		initContent();
	}

	private void reInit() {
		isLogin = ApiContext.getInstance(this).isAuthorized();
		user = ApiContext.getInstance(this).getCurrentUser();
		init();
	}

	// 登录后需要重新加载视图
	private void init() {
		initUser();
		// 数据重新加载后显示
		if (seekVO.getType().equals(net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK)) {
			initOfferView();
			initViewLayout();
		} else {
			initAssistance();
		}
	}

	private void initAssistance() {
		closeBtn_layout.setVisibility(View.GONE);
		offerBtn_layout.setVisibility(View.GONE);
		String status = seekVO.getStatus();
		if (isLogin) { // 只有已登录用户才有可能看到这两个按钮
			login_layout.setVisibility(View.GONE);
			if (!net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(status)
					&& !net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(status)) {
				if (isSeekOwner()) {
					closeBtn_layout.setVisibility(View.VISIBLE);
				}
			}
		} else {
			login_layout.setVisibility(View.VISIBLE);
		}
	}

	// 重新加载当前的用户及相关视图
	private void initUser() {
		loadSeekUserFromService();
		initSeekUserLayout();
	}

	private void initSeekUserLayout() {
		UserUtils.bindUser(seekUser, this, seek_avatar, seek_username, seek_approve);
		// 填充手机号
		phone.setText(seekUser.getPhone());
		seekerTitle.setText("等级:" + seekUser.getSeekerTitle());
	}

	private void initContent() {
		seek_avatar.setImageResource(R.drawable.default_avatar);
		content.setText(seekVO.getContent());

		if (seekVO.getExipireDate() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getExipireDate());
			String str = DateUtils.toDateString(c.getTime());
			if (Constants.MAX_EXIPIREDATE.equals(str)) {
				str = Constants.MAX_EXIPIREDATE_CONDITION;
			}
			closedOn.setText(str);
		}
		bindTime(seekVO.getCreatedOn(), seek_created_at);

		String str = seekVO.getAdditionalReward();
		additionalReward.setText("附加说明:" + str);
		if (!StringUtils.isNotEmpty(str)) {
			additionalReward_layout.setVisibility(View.GONE);
		} else {
			additionalReward_layout.setVisibility(View.VISIBLE);
		}
		String serviceDateStr = seekVO.getServiceDate();
		if (Constants.MAX_EXIPIREDATE.equals(serviceDateStr)) {
			serviceDateStr = Constants.MAX_EXIPIREDATE_CONDITION;
		}
		serviceDate.setText(serviceDateStr);
	}

	private void initViewLayout() {
		String status = seekVO.getStatus();

		closeBtn_layout.setVisibility(View.GONE);
		offerBtn_layout.setVisibility(View.GONE);
		if (isLogin) { // 只有已登录用户才有可能看到这两个按钮
			login_layout.setVisibility(View.GONE);
			if (seekVO.isEnable() && !net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(status)
					&& !net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(status)) {
				if (isSeekOwner()) {
					closeBtn_layout.setVisibility(View.VISIBLE);
				} else {
					offerBtn_layout.setVisibility(View.VISIBLE);
				}

				if (net.ipetty.ibang.vo.Constants.SEEK_STATUS_DELEGATED.equals(status) && !isSeekOwner()) {
					offerBtn_layout.setVisibility(View.VISIBLE);
				}
			} else if (!seekVO.isEnable()) {
				closeBtn.setText("已被屏蔽");
				closeBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
				closeBtn_layout.setVisibility(View.VISIBLE);
			}
		} else {
			login_layout.setVisibility(View.VISIBLE);
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
		imageViews.clear();
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
		String text = String.valueOf(currentItem + 1) + "/" + String.valueOf(imageViews.size());
		imageViewText.setText(text);
	}

	private class pageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
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
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			setImageViewText();
		}
	}

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
		ImageView approve;
		ImageView more;
	}

	private void initOfferView() {
		offerListView.removeAllViews();
		new ListOfferBySeekIdTask(SeekActivity.this).setListener(
				new DefaultTaskListener<List<OfferVO>>(SeekActivity.this) {

					@Override
					public void onSuccess(List<OfferVO> offers) {
						int delegationNum = 0;
						int offerNum = offers.size();

						if (offerNum == 0) {
							offerList_layout.setVisibility(View.GONE);
							offerList_empty_layout.setVisibility(View.VISIBLE);
						} else {
							offerList_layout.setVisibility(View.VISIBLE);
							offerList_empty_layout.setVisibility(View.GONE);
						}

						for (OfferVO offer : offers) {
							if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED.equals(offer.getStatus())
									|| net.ipetty.ibang.vo.Constants.OFFER_STATUS_FINISHED.equals(offer.getStatus())) {
								++delegationNum;
							}
							View view = getItemOfferView(offer);
							offerListView.addView(view);
						}

						// 显示(帮助数/委托总数)；
						number.setText(delegationNum + "/" + offerNum);
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
		holder.approve = (ImageView) view.findViewById(R.id.approve);
		holder.more = (ImageView) view.findViewById(R.id.more);

		holder.more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				popItems.clear();
				popItems.add(new ModDialogItem(null, "举报", new ReportClickListener(SeekActivity.this,
						ReportClickListener.TYPE_OFFER, offer.getId(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popDialog.cancel();
							}
						})));
				popDialog = DialogUtils.modPopupDialog(SeekActivity.this, popItems, popDialog);
			}
		});

		// 如果已帮助过，则帮助按钮不可见（不能重复帮助），求助者联系方式可见
		if (user != null && offer.getOffererId().equals(user.getId())) {
			offerBtn_layout.setVisibility(View.GONE);
			contact_layout.setVisibility(View.VISIBLE);
		}

		holder.status.setText(offer.getStatus());
		holder.status.setVisibility(View.VISIBLE);

		// 接受帮助按钮
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
								// 接受帮助后进行界面操作
								holder.accept_button.setVisibility(View.GONE);
								holder.delegation_info_btn.setVisibility(View.VISIBLE); // 查看委托按钮
								offer.setStatus(net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED);
							}
						}).execute(delegation);
			}
		});
		// 接受帮助按钮可见性
		if (isLogin && isSeekOwner() && net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED.equals(offer.getStatus())) {
			holder.accept_button.setVisibility(View.VISIBLE);
			holder.status.setVisibility(View.GONE);
		} else {
			holder.accept_button.setVisibility(View.GONE);
		}

		// 查看委托按钮
		holder.delegation_info_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SeekActivity.this, DelegationActivity.class);
				intent.putExtra(Constants.INTENT_OFFER_ID, offer.getId()); // 查看委托界面是通过offerId获取委托的
				intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offer).toString());
				intent.putExtra(Constants.INTENT_SEEK_ID, seekVO.getId());
				intent.putExtra(Constants.INTENT_SEEK_JSON, seekJSON);
				startActivity(intent);
			}
		});
		// 查看委托按钮可见性，求助者与委托人能看到
		if (user != null
				&& (isSeekOwner() || offer.getOffererId().equals(user.getId()))
				&& (net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED.equals(offer.getStatus()) || net.ipetty.ibang.vo.Constants.OFFER_STATUS_FINISHED
						.equals(offer.getStatus()))) {
			holder.delegation_info_btn.setVisibility(View.VISIBLE);

			if (offer.getDelegation().getStatus().equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED)
					|| offer.getDelegation().getStatus()
							.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_OFFERER_EVALUATED)
					|| offer.getDelegation().getStatus()
							.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_SEEKER_EVALUATED)
					|| offer.getDelegation().getStatus()
							.equals(net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_BI_EVALUATED)) {
				holder.delegation_info_btn.setText("查看评价");
			} else {
				holder.delegation_info_btn.setText("查看委托");
			}

			holder.status.setVisibility(View.GONE);
		} else {
			holder.delegation_info_btn.setVisibility(View.GONE);
		}

		bindTime(offer.getCreatedOn(), holder.created_at);
		holder.content.setText(offer.getContent());

		UserVO user = offer.getOfferer();
		UserUtils.bindUser(user, this, holder.avator, holder.nickname, holder.approve);
		holder.totalPoint.setText("等级:" + String.valueOf(user.getSeekerTitle()));
		return view;
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd").format(date);
		time.setText(creatAt);
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				reInit();
				// TODO 需要重新刷新界面组件状态
			}
			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}
			if (Constants.BROADCAST_INTENT_PUBLISH_OFFER.equals(action)) {
				reInit();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastreciver);
	}

}
