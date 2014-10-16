package net.ipetty.ibang.android.seek;

import java.util.Calendar;
import java.util.Date;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.user.UserInfoActivity;
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
import com.nostra13.universalimageloader.core.ImageLoader;

public class OfferActivity extends Activity {

	private ImageView seek_avatar;
	private TextView seek_nickname;
	private TextView seek_created_at;
	private TextView seek_content;
	private TextView seek_closedOn;
	private TextView seek_phone;
	private View seek_contact_layout;

	private ImageView offer_avatar;
	private TextView offer_nickname;
	private TextView offer_created_at;
	private TextView offer_content;
	private TextView offer_phone;
	private View offer_contact_layout;
	private TextView offer_totalPoint;
	private TextView offer_close;
	private View offer_close_layout;

	private View delegation_layout;
	private TextView delegation;

	private SeekVO seekVO;
	private OfferVO offerVO;
	private UserVO seekUser;
	private UserVO offerUser;
	private UserVO user;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_offer));
		btnBack.setOnClickListener(new BackClickListener(this));

		seek_avatar = (ImageView) this.findViewById(R.id.avatar);
		seek_content = (TextView) this.findViewById(R.id.content);
		seek_created_at = (TextView) this.findViewById(R.id.created_at);
		seek_nickname = (TextView) this.findViewById(R.id.nickname);
		seek_closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_phone = (TextView) this.findViewById(R.id.phone);
		seek_contact_layout = this.findViewById(R.id.contact_layout);

		offer_avatar = (ImageView) this.findViewById(R.id.offer_avatar);
		offer_content = (TextView) this.findViewById(R.id.offer_content);
		offer_created_at = (TextView) this.findViewById(R.id.offer_created_at);
		offer_nickname = (TextView) this.findViewById(R.id.offer_nickname);
		offer_phone = (TextView) this.findViewById(R.id.offer_phone);
		offer_contact_layout = this.findViewById(R.id.offer_contact_layout);
		offer_totalPoint = (TextView) this.findViewById(R.id.offer_totalPoint);

		offer_close = (TextView) this.findViewById(R.id.offer_close);
		offer_close_layout = this.findViewById(R.id.offer_close_layout);

		delegation = (TextView) this.findViewById(R.id.delegation);
		delegation_layout = this.findViewById(R.id.delegation_layout);

		seekVO = new SeekVO();
		seekUser = new UserVO();
		bindUser(seekUser, seek_avatar, seek_nickname);
		bindTime(seekVO.getCreatedOn(), seek_created_at);
		seek_content.setText(seekVO.getContent());
		seek_phone.setText(seekUser.getPhone());
		if (seekVO.getClosedOn() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getClosedOn());
			seek_closedOn.setText(DateUtils.toDateString(c.getTime()));
		}

		offerVO = new OfferVO();
		offerUser = new UserVO();
		bindUser(offerUser, offer_avatar, offer_nickname);
		bindTime(offerVO.getCreatedOn(), offer_created_at);
		offer_content.setText(offerVO.getContent());
		offer_totalPoint.setText("积分" + String.valueOf(offerUser.getSeekerTotalPoint()));
		offer_phone.setText(offerUser.getPhone());

		seek_contact_layout.setVisibility(View.GONE);
		offer_contact_layout.setVisibility(View.GONE);
		offer_close_layout.setVisibility(View.GONE);

		delegation.setText("接受应征");
		delegation.setText("查看委托");
		delegation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 应征 或者 查看委托单
			}
		});

		offer_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 关闭应征

			}
		});

	}

	// TODO: 是否seek的创建者
	private boolean isSeekOwner() {
		return true;
	}

	// TODO: 是否委托的创建者
	private boolean isOfferOwner() {
		return false;
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
				Intent intent = new Intent(OfferActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
	}
}
