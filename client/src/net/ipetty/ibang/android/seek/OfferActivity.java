package net.ipetty.ibang.android.seek;

import java.util.Calendar;
import java.util.Date;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.core.util.UserUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class OfferActivity extends Activity {

	private ImageView seek_avatar;
	private TextView seek_nickname;
	private TextView seek_created_at;
	private TextView seek_content;
	private TextView seek_closedOn;
	private TextView seek_phone;
	private View seek_contact_layout;
	private ImageView seek_approve;

	private ImageView offer_avatar;
	private TextView offer_nickname;
	private TextView offer_created_at;
	private TextView offer_content;
	private TextView offer_phone;
	private View offer_contact_layout;
	private TextView offer_totalPoint;
	private TextView offer_close;
	private View offer_close_layout;
	private ImageView offer_approve;
	// private TextView offer_status;
	private TextView seek_seekerTitle;

	private View delegation_layout;
	private TextView delegation;
	private TextView seek_additionalReward;
	private TextView seek_serviceDate;
	private View seek_additionalReward_layout;
	private View offer_wait_delegated_layout;
	private View offer_self_closed_layout;

	private SeekVO seekVO;
	private OfferVO offerVO;
	private UserVO seeker;
	private UserVO offerer;
	private UserVO user;
	private Long offerId;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_offer));

		seek_avatar = (ImageView) this.findViewById(R.id.avatar);
		seek_content = (TextView) this.findViewById(R.id.content);
		seek_created_at = (TextView) this.findViewById(R.id.created_at);
		seek_nickname = (TextView) this.findViewById(R.id.nickname);
		seek_closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_phone = (TextView) this.findViewById(R.id.phone);
		seek_contact_layout = this.findViewById(R.id.contact_layout);
		seek_seekerTitle = (TextView) this.findViewById(R.id.seekerTitle);
		seek_additionalReward = (TextView) this.findViewById(R.id.additionalReward);
		seek_serviceDate = (TextView) this.findViewById(R.id.serviceDate);
		seek_additionalReward_layout = this.findViewById(R.id.additionalReward_layout);
		seek_approve = (ImageView) this.findViewById(R.id.seek_approve);

		offer_avatar = (ImageView) this.findViewById(R.id.offer_avatar);
		offer_content = (TextView) this.findViewById(R.id.offer_content);
		offer_created_at = (TextView) this.findViewById(R.id.offer_created_at);
		offer_nickname = (TextView) this.findViewById(R.id.offer_nickname);
		offer_phone = (TextView) this.findViewById(R.id.offer_phone);
		offer_contact_layout = this.findViewById(R.id.offer_contact_layout);
		offer_totalPoint = (TextView) this.findViewById(R.id.offer_totalPoint);
		offer_wait_delegated_layout = this.findViewById(R.id.offer_wait_delegated_layout);
		offer_self_closed_layout = this.findViewById(R.id.offer_self_closed_layout);
		offer_approve = (ImageView) this.findViewById(R.id.offer_approve);
		// offer_status = (TextView) this.findViewById(R.id.offer_status);

		offer_close = (TextView) this.findViewById(R.id.offer_close);
		offer_close_layout = this.findViewById(R.id.offer_close_layout);

		delegation = (TextView) this.findViewById(R.id.delegation);
		delegation_layout = this.findViewById(R.id.delegation_layout);

		user = ApiContext.getInstance(OfferActivity.this).getCurrentUser();

		offerId = this.getIntent().getExtras().getLong(Constants.INTENT_OFFER_ID);
		loadData();
	}

	/**
	 * 加载数据并填充界面
	 */
	private void loadData() {
		new GetOfferByIdTask(OfferActivity.this).setListener(new DefaultTaskListener<OfferVO>(OfferActivity.this) {

			@Override
			public void onSuccess(OfferVO result) {
				offerVO = result;
				offerer = offerVO.getOfferer();
				UserUtils.bindUser(offerer, OfferActivity.this, offer_avatar, offer_nickname, offer_approve);
				bindTime(offerVO.getCreatedOn(), offer_created_at);
				offer_content.setText(offerVO.getContent());
				offer_totalPoint.setText("等级:" + String.valueOf(offerer.getSeekerTitle()));
				offer_phone.setText(offerer.getPhone());
				// offer_status.setText(offerVO.getStatus());

				new GetSeekByIdTask(OfferActivity.this).setListener(
						new DefaultTaskListener<SeekVO>(OfferActivity.this) {

							@Override
							public void onSuccess(SeekVO result) {
								seekVO = result;
								seeker = seekVO.getSeeker();

								// 填充界面
								initView();
							}
						}).execute(offerVO.getSeekId());
			}
		}).execute(offerId);
	}

	/**
	 * 填充界面
	 */
	private void initView() {
		seek_contact_layout.setVisibility(View.GONE);
		offer_contact_layout.setVisibility(View.GONE);
		offer_close_layout.setVisibility(View.GONE);
		delegation_layout.setVisibility(View.GONE);

		UserUtils.bindUser(seeker, this, seek_avatar, seek_nickname, seek_approve);
		bindTime(seekVO.getCreatedOn(), seek_created_at);
		seek_content.setText(seekVO.getContent());
		seek_phone.setText(seeker.getPhone());
		seek_seekerTitle.setText("等级:" + seeker.getSeekerTitle());

		String str = seekVO.getAdditionalReward();
		seek_additionalReward.setText("附加说明:" + str);
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

		if (isSeekOwner() || isOfferOwner()) {
			seek_contact_layout.setVisibility(View.VISIBLE);
			offer_contact_layout.setVisibility(View.VISIBLE);
		}

		// 接受帮助/查看委托按钮
		initDelegateButton();

		// 关闭帮助按钮
		initCloseButton();

		if (offerVO.isEnable() && isOfferOwner()
				&& (net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED.equals(offerVO.getStatus()))) {
			offer_wait_delegated_layout.setVisibility(View.VISIBLE);
		} else {
			offer_wait_delegated_layout.setVisibility(View.GONE);
		}

		if (offerVO.isEnable() && net.ipetty.ibang.vo.Constants.OFFER_STATUS_CLOSED.equals(offerVO.getStatus())
				&& offerVO.getDelegation() == null) {
			offer_self_closed_layout.setVisibility(View.VISIBLE);
		} else {
			offer_self_closed_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 接受帮助/查看委托按钮
	 */
	private void initDelegateButton() {
		if (!offerVO.isEnable()) {
			delegation.setText("已被屏蔽");
			delegation_layout.setVisibility(View.VISIBLE);
		} else if (net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED.equals(offerVO.getStatus())) {
			if (!isSeekFinishedOrClosed() && isSeekOwner()) {
				delegation.setText("接受帮助");
				delegation.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 接受帮助
						DelegationVO delegation = new DelegationVO();
						delegation.setSeekId(seekVO.getId());
						delegation.setOfferId(offerVO.getId());
						new AcceptOfferTask(OfferActivity.this).setListener(
								new DefaultTaskListener<DelegationVO>(OfferActivity.this) {

									@Override
									public void onSuccess(DelegationVO result) {
										// 接受帮助后进行界面操作
										initViewDelegationButton();
										broadCaseUpdate(offerId);
									}
								}).execute(delegation);
					}
				});
				delegation_layout.setVisibility(View.VISIBLE);
			}
		} else if (isSeekOwner() || isOfferOwner()) {
			initViewDelegationButton();
		}
	}

	/**
	 * 设置关闭帮助按钮
	 */
	private void initCloseButton() {
		if (offerVO.isEnable()
				&& !isSeekFinishedOrClosed()
				&& isOfferOwner()
				&& (net.ipetty.ibang.vo.Constants.OFFER_STATUS_OFFERED.equals(offerVO.getStatus()) || net.ipetty.ibang.vo.Constants.OFFER_STATUS_DELEGATED
						.equals(offerVO.getStatus()))) {
			offer_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new CloseOfferTask(OfferActivity.this).setListener(
							new DefaultTaskListener<Boolean>(OfferActivity.this) {

								@Override
								public void onSuccess(Boolean result) {
									// 关闭帮助后的界面操作
									offer_close_layout.setVisibility(View.GONE);
									offer_wait_delegated_layout.setVisibility(View.GONE);
									offer_self_closed_layout.setVisibility(View.VISIBLE);
									broadCaseUpdate(offerId);
								}
							}).execute(offerId);
				}
			});
			offer_close_layout.setVisibility(View.VISIBLE);
		} else {
			offer_close_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 求助单是否已完成或关闭
	 */
	private boolean isSeekFinishedOrClosed() {
		return net.ipetty.ibang.vo.Constants.SEEK_STATUS_FINISHED.equals(seekVO.getStatus())
				|| net.ipetty.ibang.vo.Constants.SEEK_STATUS_CLOSED.equals(seekVO.getStatus());
	}

	private void broadCaseUpdate(Long offerId) {
		Intent intent = new Intent(Constants.BROADCAST_INTENT_OFFER_UPDATE);
		intent.putExtra(Constants.INTENT_OFFER_ID, offerId);
		sendBroadcast(intent);
	}

	/**
	 * 接受帮助/查看委托按钮
	 */
	private void initViewDelegationButton() {
		if (offerVO.getDelegation() != null) {
			delegation.setText("查看委托");
			delegation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 查看委托单
					Intent intent = new Intent(OfferActivity.this, DelegationActivity.class);
					intent.putExtra(Constants.INTENT_OFFER_ID, offerVO.getId()); // 查看委托界面是通过offerId获取委托的
					intent.putExtra(Constants.INTENT_OFFER_JSON, JSONUtils.toJson(offerVO).toString());
					intent.putExtra(Constants.INTENT_SEEK_ID, seekVO.getId());
					intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(seekVO).toString());
					startActivity(intent);
				}
			});
			delegation_layout.setVisibility(View.VISIBLE);
		} else {
			delegation_layout.setVisibility(View.GONE);
		}
	}

	// 当前用户是否为当前求助单的求助者
	private boolean isSeekOwner() {
		return user != null && seeker != null && user.getId().equals(seeker.getId());
	}

	// 当前用户是否为当前帮助单的帮助者
	private boolean isOfferOwner() {
		return user != null && offerer != null && user.getId().equals(offerer.getId());
	}

	private void bindTime(Date date, TextView time) {
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd").format(date);
		time.setText(creatAt);
	}

}
