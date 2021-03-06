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
import net.ipetty.ibang.android.core.ui.SendMsgClickListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.core.util.UserUtils;
import net.ipetty.ibang.android.evaluation.EvaluationActivity;
import net.ipetty.ibang.android.evaluation.ListEvaluationByDelegationIdTask;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.EvaluationVO;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DelegationActivity extends Activity {

	// private String TAG = getClass().getSimpleName();
	private ImageView seek_avatar;
	private TextView seek_nickname;
	private TextView seek_created_at;
	private TextView seek_content;
	private TextView seek_closedOn;
	private TextView seek_phone;
	// private View seek_contact_layout;
	private View seek_evaluation_layout;
	private TextView seek_evaluation;
	private TextView seek_evaluation_content;
	private LinearLayout seek_evaluation_image_layout;
	private TextView seek_seekerTitle;
	private TextView seek_additionalReward;
	private TextView seek_serviceDate;
	private View seek_additionalReward_layout;
	private ImageView seek_approve;
	private ImageView seek_more;
	private TextView seek_address;
	private ImageView seek_msg;

	private ImageView delegation_avatar;
	private TextView delegation_nickname;
	private TextView delegation_created_on;
	private TextView delegation_content;
	private TextView delegation_phone;
	// private View delegation_contact_layout;
	private TextView finish_delegation_btn;
	private View finish_delegation_btn_layout;
	private TextView close_delegation_btn;
	private View close_delegation_btn_layout;
	private View delegation_evaluation_layout;
	private TextView delegation_evaluation;
	private TextView delegation_evaluation_content;
	private LinearLayout delegation_evaluation_image_layout;
	private TextView delegation_totalPoint;
	private ImageView delegation_approve;
	private ImageView delegation_more;
	private ImageView delegation_msg;

	private View seek_wait_finish_layout;
	private View evaluation_layout;
	private TextView evaluation;

	private Long seekId;
	private Long offerId;
	private Long delegationId;
	private SeekVO seekVO;
	private OfferVO offerVO;
	private DelegationVO delegationVO;
	private UserVO seeker;
	private UserVO offerer;
	private UserVO currentUser;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private ArrayList<ModDialogItem> popItems = new ArrayList<ModDialogItem>(0);
	private Dialog popDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delegation);
		ActivityManager.getInstance().addActivity(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_EVALUATE);
		this.registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_delegation));

		seek_avatar = (ImageView) this.findViewById(R.id.avatar);
		seek_content = (TextView) this.findViewById(R.id.content);
		seek_created_at = (TextView) this.findViewById(R.id.created_at);
		seek_nickname = (TextView) this.findViewById(R.id.nickname);
		seek_closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_phone = (TextView) this.findViewById(R.id.phone);
		// seek_contact_layout = this.findViewById(R.id.contact_layout);
		seek_evaluation_layout = this.findViewById(R.id.seek_evaluation_layout);// 评分布局，对方评分后显示
		seek_evaluation = (TextView) this.findViewById(R.id.seek_evaluation);
		seek_evaluation_content = (TextView) this.findViewById(R.id.seek_evaluation_content);
		seek_evaluation_image_layout = (LinearLayout) this.findViewById(R.id.seek_evaluation_image_layout);
		seek_seekerTitle = (TextView) this.findViewById(R.id.seekerTitle);
		seek_additionalReward = (TextView) this.findViewById(R.id.additionalReward);
		seek_serviceDate = (TextView) this.findViewById(R.id.serviceDate);
		seek_additionalReward_layout = this.findViewById(R.id.additionalReward_layout);
		seek_wait_finish_layout = this.findViewById(R.id.seek_wait_finish_layout);
		seek_approve = (ImageView) this.findViewById(R.id.seek_approve);
		seek_more = (ImageView) this.findViewById(R.id.seek_more);
		seek_address = (TextView) this.findViewById(R.id.address);
		seek_msg = (ImageView) this.findViewById(R.id.msg);

		delegation_avatar = (ImageView) this.findViewById(R.id.delegation_avatar);
		delegation_nickname = (TextView) this.findViewById(R.id.delegation_nickname);
		delegation_created_on = (TextView) this.findViewById(R.id.delegation_created_at);
		delegation_content = (TextView) this.findViewById(R.id.delegation_content);
		delegation_phone = (TextView) this.findViewById(R.id.delegation_phone);
		// delegation_contact_layout =
		// this.findViewById(R.id.delegation_contact_layout);
		finish_delegation_btn = (TextView) this.findViewById(R.id.delegation_btn);
		finish_delegation_btn_layout = this.findViewById(R.id.delegation_btn_layout); // 完成委托按钮
		close_delegation_btn = (TextView) this.findViewById(R.id.close_delegation_btn);
		close_delegation_btn_layout = this.findViewById(R.id.close_delegation_btn_layout); // 关闭/中止委托按钮

		delegation_evaluation_layout = this.findViewById(R.id.delegation_evaluation_layout);// 评分布局，对方评分后显示
		delegation_evaluation = (TextView) this.findViewById(R.id.delegation_evaluation);
		delegation_evaluation_content = (TextView) this.findViewById(R.id.delegation_evaluation_content);
		delegation_evaluation_image_layout = (LinearLayout) this.findViewById(R.id.delegation_evaluation_image_layout);
		delegation_totalPoint = (TextView) this.findViewById(R.id.delegation_totalPoint);
		delegation_approve = (ImageView) this.findViewById(R.id.delegation_approve);
		delegation_more = (ImageView) this.findViewById(R.id.delegation_more);
		delegation_msg = (ImageView) this.findViewById(R.id.delegation_msg);

		evaluation_layout = this.findViewById(R.id.evaluation_layout); // 评价按钮根据权限显示不同的评价
		evaluation = (TextView) this.findViewById(R.id.evaluation);

		// 获取当前用户
		currentUser = ApiContext.getInstance(this).getCurrentUser();

		delegationId = this.getIntent().getExtras().getLong(Constants.INTENT_DELEGATION_ID);
		offerId = this.getIntent().getExtras().getLong(Constants.INTENT_OFFER_ID);
		seekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);

		if (offerId != null && offerId != 0) {
			loadDataByOfferId();
		} else {
			loadDataByDelegationId();
		}
	}

	private void loadDataByDelegationId() {
		new GetDelegationByIdTask(DelegationActivity.this).setListener(
				new DefaultTaskListener<DelegationVO>(DelegationActivity.this) {

					@Override
					public void onSuccess(DelegationVO result) {
						delegationVO = result;
						offerId = delegationVO.getOfferId();
						seekId = delegationVO.getSeekId();
						loadDataByOfferId();
					}
				}).execute(delegationId);
	}

	/**
	 * 加载数据并填充界面
	 */
	private void loadDataByOfferId() {
		// 获取数据，offer
		new GetOfferByIdTask(DelegationActivity.this).setListener(
				new DefaultTaskListener<OfferVO>(DelegationActivity.this) {

					@Override
					public void onSuccess(OfferVO result) {
						offerVO = result;
						seekId = offerVO.getSeekId();

						// 获取数据，delegation
						delegationVO = offerVO.getDelegation();
						if (delegationVO == null) {
							Toast.makeText(DelegationActivity.this, "找不到对应的委托", Toast.LENGTH_SHORT).show();
							return;
						}

						delegationId = delegationVO.getId();
						offerer = offerVO.getOfferer();

						// 获取数据，seek
						new GetSeekByIdTask(DelegationActivity.this).setListener(
								new DefaultTaskListener<SeekVO>(DelegationActivity.this) {

									@Override
									public void onSuccess(SeekVO result) {
										seekVO = result;
										seeker = seekVO.getSeeker();
										initView();
									}
								}).execute(seekId);
					}
				}).execute(offerId);
	}

	/**
	 * 界面
	 */
	private void initView() {
		// 填充求助信息
		UserUtils.bindUser(seeker, this, seek_avatar, seek_nickname, seek_approve);
		bindTime(seekVO.getCreatedOn(), seek_created_at);
		seek_content.setText(seekVO.getContent());
		seek_phone.setText(seeker.getPhone());
		seek_seekerTitle.setText("等级:" + seeker.getSeekerTitle());
		String str = seekVO.getAdditionalReward();
		seek_additionalReward.setText("附加说明:" + str);
		if (StringUtils.isNotEmpty(seekVO.getAddress())) {
			seek_address.setText(seekVO.getAddress());
		}
		seek_msg.setOnClickListener(new SendMsgClickListener(this, seekVO.getSeekerId()));

		seek_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popItems.clear();
				popItems.add(new ModDialogItem(null, "举报", new ReportClickListener(DelegationActivity.this,
						ReportClickListener.TYPE_SEEK, seekVO.getId(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popDialog.cancel();
							}
						})));
				popDialog = DialogUtils.modPopupDialog(DelegationActivity.this, popItems, popDialog);
			}
		});

		if (!StringUtils.isNotEmpty(str)) {
			seek_additionalReward_layout.setVisibility(View.GONE);
		} else {
			seek_additionalReward_layout.setVisibility(View.VISIBLE);
		}
		String serviceDateStr = seekVO.getServiceDate();
		if (Constants.MAX_EXIPIREDATE.equals(serviceDateStr)) {
			serviceDateStr = Constants.MAX_EXIPIREDATE_CONDITION;
		}
		seek_serviceDate.setText(serviceDateStr);

		if (seekVO.getClosedOn() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getClosedOn());
			seek_closedOn.setText(DateUtils.toDateString(c.getTime()));
		}
		if (seekVO.getClosedOn() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getClosedOn());
			seek_closedOn.setText(DateUtils.toDateString(c.getTime()));
		}

		// 查看已评价内容
		initEvaluationContent();

		// 评价按钮
		initEvaluateButton();

		// 等待接受委托完成状态显示
		if (isSeekOwner() && net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_DELEGATED.equals(delegationVO.getStatus())) {
			seek_wait_finish_layout.setVisibility(View.VISIBLE);
		} else {
			seek_wait_finish_layout.setVisibility(View.GONE);
		}

		// 求助/委托信息
		UserUtils.bindUser(offerer, this, delegation_avatar, delegation_nickname, delegation_approve);
		bindTime(delegationVO.getCreatedOn(), delegation_created_on);
		delegation_content.setText(offerVO.getContent());
		delegation_phone.setText(offerer.getPhone());
		delegation_totalPoint.setText("等级:" + offerer.getSeekerTitle());
		delegation_msg.setOnClickListener(new SendMsgClickListener(this, delegationVO.getOffererId()));
		delegation_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				popItems.clear();
				popItems.add(new ModDialogItem(null, "举报", new ReportClickListener(DelegationActivity.this,
						ReportClickListener.TYPE_OFFER, delegationVO.getOffererId(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popDialog.cancel();
							}
						})));
				popDialog = DialogUtils.modPopupDialog(DelegationActivity.this, popItems, popDialog);
			}
		});

		// 完成委托按钮
		initFinishDelegationButton();

		// 关闭委托按钮
		initCloseDelegationButton();
	}

	/**
	 * 查看已评价内容
	 */
	private void initEvaluationContent() {
		// 双方已评价后，可以看到评分信息
		if (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_BI_EVALUATED.equals(delegationVO.getStatus())) {
			new ListEvaluationByDelegationIdTask(DelegationActivity.this).setListener(
					new DefaultTaskListener<List<EvaluationVO>>(DelegationActivity.this) {

						@Override
						public void onSuccess(List<EvaluationVO> evaluations) {
							seek_evaluation_layout.setVisibility(View.GONE);
							delegation_evaluation_layout.setVisibility(View.GONE);

							for (EvaluationVO evaluation : evaluations) {
								if (evaluation == null) {
									break;
								}
								if (net.ipetty.ibang.vo.Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER.equals(evaluation
										.getType())) {
									// 求助者对此次委托的评分信息
									delegation_evaluation.setText(String.valueOf(evaluation.getPoint()));
									delegation_evaluation.setVisibility(View.VISIBLE);
									if (StringUtils.isNotEmpty(evaluation.getContent())) {
										delegation_evaluation_content.setText(evaluation.getContent());
										delegation_evaluation_content.setVisibility(View.VISIBLE);
									} else {
										delegation_evaluation_content.setVisibility(View.GONE);
									}

									if (evaluation.getImages().size() > 0) {
										delegation_evaluation_image_layout.setVisibility(View.VISIBLE);
										initImage(delegation_evaluation_image_layout, evaluation.getImages());
									} else {
										delegation_evaluation_image_layout.setVisibility(View.GONE);
									}

									delegation_evaluation_layout.setVisibility(View.VISIBLE);
								} else if (net.ipetty.ibang.vo.Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER
										.equals(evaluation.getType())) {
									// 帮助者对此次委托的评分信息
									seek_evaluation.setText(String.valueOf(evaluation.getPoint()));
									seek_evaluation.setVisibility(View.VISIBLE);
									if (StringUtils.isNotEmpty(evaluation.getContent())) {
										seek_evaluation_content.setText(evaluation.getContent());
										seek_evaluation_content.setVisibility(View.VISIBLE);
									} else {
										seek_evaluation_content.setVisibility(View.GONE);
									}

									if (evaluation.getImages().size() > 0) {
										seek_evaluation_image_layout.setVisibility(View.VISIBLE);
										initImage(seek_evaluation_image_layout, evaluation.getImages());
									} else {
										seek_evaluation_image_layout.setVisibility(View.GONE);
									}

									seek_evaluation_layout.setVisibility(View.VISIBLE);
								}
							}

						}

					}).execute(delegationId);
		} else if (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_SEEKER_EVALUATED.equals(delegationVO.getStatus())) {
			// 求助者已评价
			delegation_evaluation.setVisibility(View.GONE);
			String text = isSeekOwner() ? "我已评价,双方评价后可见" : "对方已评价,双方评价后可见";
			delegation_evaluation_content.setText(text);
			delegation_evaluation_content.setVisibility(View.VISIBLE);
			delegation_evaluation_image_layout.setVisibility(View.GONE);
			delegation_evaluation_layout.setVisibility(View.VISIBLE);
			seek_evaluation_layout.setVisibility(View.GONE);
		} else if (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_OFFERER_EVALUATED.equals(delegationVO.getStatus())) {
			// 帮助者已评价
			seek_evaluation.setVisibility(View.GONE);
			String text = isSeekOwner() ? "对方已评价,双方评价后可见" : "我已评价,双方评价后可见";
			seek_evaluation_content.setText(text);
			seek_evaluation_content.setVisibility(View.VISIBLE);
			seek_evaluation_image_layout.setVisibility(View.GONE);
			seek_evaluation_layout.setVisibility(View.VISIBLE);
			delegation_evaluation_layout.setVisibility(View.GONE);
		} else {
			seek_evaluation_layout.setVisibility(View.GONE);
			delegation_evaluation_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置评价按钮
	 */
	private void initEvaluateButton() {
		if (!isSeekFinishedOrClosed() // 求助单未关闭
				&& ((isSeekOwner() && (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED.equals(delegationVO
						.getStatus()) || net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_OFFERER_EVALUATED
						.equals(delegationVO.getStatus()))) // 求助者评价
				|| (isDelegationOwner() && (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED.equals(delegationVO
						.getStatus()) || net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_SEEKER_EVALUATED
						.equals(delegationVO.getStatus()))) // 帮助者评价
				)) {
			evaluation_layout.setVisibility(View.VISIBLE);
			evaluation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DelegationActivity.this, EvaluationActivity.class);
					intent.putExtra(Constants.INTENT_DELEGATION_ID, delegationVO.getId());

					if (isSeekOwner()
							&& (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED.equals(delegationVO.getStatus()) || net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_OFFERER_EVALUATED
									.equals(delegationVO.getStatus()))) {
						// 求助者评价
						intent.putExtra(Constants.INTENT_EVALUATOR_TYPE,
								net.ipetty.ibang.vo.Constants.EVALUATION_TYPE_SEEKER_TO_OFFERER);
						intent.putExtra(Constants.INTENT_EVALUATOR_ID, seeker.getId());
						intent.putExtra(Constants.INTENT_EVALUATE_TARGET_ID, offerer.getId());
						startActivity(intent);
					} else if (isDelegationOwner()
							&& (net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_FINISHED.equals(delegationVO.getStatus()) || net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_SEEKER_EVALUATED
									.equals(delegationVO.getStatus()))) {
						// 帮助者评价
						intent.putExtra(Constants.INTENT_EVALUATOR_TYPE,
								net.ipetty.ibang.vo.Constants.EVALUATION_TYPE_OFFERER_TO_SEEKER);
						intent.putExtra(Constants.INTENT_EVALUATOR_ID, offerer.getId());
						intent.putExtra(Constants.INTENT_EVALUATE_TARGET_ID, seeker.getId());
						startActivity(intent);
					}
				}
			});
		} else {
			evaluation_layout.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置完成委托按钮
	 */
	private void initFinishDelegationButton() {
		if (!isSeekFinishedOrClosed() && isDelegationOwner()
				&& net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_DELEGATED.equals(delegationVO.getStatus())) {
			finish_delegation_btn_layout.setVisibility(View.VISIBLE);
			finish_delegation_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new FinishDelegationTask(DelegationActivity.this).setListener(
							new DefaultTaskListener<Boolean>(DelegationActivity.this) {

								@Override
								public void onSuccess(Boolean result) {
									// 刷新界面
									loadDataByOfferId();
								}
							}).execute(delegationId);
				}
			});
		} else {
			finish_delegation_btn_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置关闭委托按钮
	 */
	private void initCloseDelegationButton() {
		if (!isSeekFinishedOrClosed() && isDelegationOwner()
				&& net.ipetty.ibang.vo.Constants.DELEGATE_STATUS_DELEGATED.equals(delegationVO.getStatus())) {
			close_delegation_btn_layout.setVisibility(View.VISIBLE);
			close_delegation_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new CloseDelegationTask(DelegationActivity.this).setListener(
							new DefaultTaskListener<Boolean>(DelegationActivity.this) {

								@Override
								public void onSuccess(Boolean result) {
									// TODO 关闭委托后的界面操作
									finish_delegation_btn_layout.setVisibility(View.GONE);
									close_delegation_btn_layout.setVisibility(View.GONE);
								}
							}).execute(delegationId);
				}
			});
		} else {
			close_delegation_btn_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 求助单是否已完成或关闭
	 */
	private boolean isSeekFinishedOrClosed() {
		return net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(seekVO.getStatus())
				|| net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(seekVO.getStatus());
	}

	private void initImage(LinearLayout layout, List<ImageVO> images) {
		// TODO Auto-generated method stub
		for (ImageVO image : images) {

			LayoutParams ly = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ly.width = AnimUtils.dip2px(this, 62);
			ly.height = AnimUtils.dip2px(this, 62);
			ly.rightMargin = AnimUtils.dip2px(this, 10);
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			layout.addView(imageView, ly);
			String url = image.getSmallUrl();
			if (StringUtils.isNotEmpty(url)) {
				ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + url, imageView, options);
			} else {
				imageView.setImageResource(R.drawable.default_seek_images);
			}
			imageView.setOnClickListener(new ImageOnClickListener(image));
		}

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
				Intent intent = new Intent(DelegationActivity.this, LargerImageActivity.class);
				intent.putExtra(Constants.INTENT_IMAGE_ORIGINAL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getOriginalUrl());
				intent.putExtra(Constants.INTENT_IMAGE_SAMILL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getSmallUrl());
				startActivity(intent);
			}
		}
	}

	/**
	 * 当前用户是否为当前求助的求助者
	 */
	private boolean isSeekOwner() {
		return currentUser != null && seeker != null && currentUser.getId().equals(seeker.getId());
	}

	/**
	 * 当前用户是否为当前委托的帮助者
	 */
	private boolean isDelegationOwner() {
		return currentUser != null && offerer != null && currentUser.getId().equals(offerer.getId());
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd").format(date);
		time.setText(creatAt);
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_EVALUATE.equals(action)) {
				loadDataByOfferId();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastreciver);
	}

}
