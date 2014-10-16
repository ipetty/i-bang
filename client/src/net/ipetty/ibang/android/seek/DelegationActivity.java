package net.ipetty.ibang.android.seek;

import java.util.Calendar;
import java.util.Date;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserInfoActivity;
import net.ipetty.ibang.vo.DelegationVO;
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

public class DelegationActivity extends Activity {

	private ImageView seek_avatar;
	private TextView seek_nickname;
	private TextView seek_created_at;
	private TextView seek_content;
	private TextView seek_closedOn;
	private TextView seek_phone;
	private View seek_contact_layout;

	private ImageView delegation_avatar;
	private TextView delegation_nickname;
	private TextView delegation_created_at;
	private View delegation_contact_layout;
	private TextView delegation_btn;
	private View delegation_btn_layout;
	private TextView close_delegation_btn;
	private View close_delegation_btn_layout;

	private View evaluation_layout;
	private TextView evaluation;

	private SeekVO seekVO;
	private DelegationVO delegationVO;
	private UserVO seekUser;
	private UserVO delegationUser;
	private UserVO user;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delegation);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_delegation));
		btnBack.setOnClickListener(new BackClickListener(this));

		seek_avatar = (ImageView) this.findViewById(R.id.avatar);
		seek_content = (TextView) this.findViewById(R.id.content);
		seek_created_at = (TextView) this.findViewById(R.id.created_at);
		seek_nickname = (TextView) this.findViewById(R.id.nickname);
		seek_closedOn = (TextView) this.findViewById(R.id.closedOn);
		seek_contact_layout = this.findViewById(R.id.contact_layout);

		delegation_avatar = (ImageView) this.findViewById(R.id.delegation_avatar);
		delegation_nickname = (TextView) this.findViewById(R.id.delegation_nickname);
		delegation_created_at = (TextView) this.findViewById(R.id.delegation_created_at);
		delegation_contact_layout = this.findViewById(R.id.delegation_contact_layout);
		delegation_btn = (TextView) this.findViewById(R.id.delegation_btn);
		delegation_btn_layout = this.findViewById(R.id.delegation_btn_layout); // 完成帮助按钮
		close_delegation_btn = (TextView) this.findViewById(R.id.close_delegation_btn);
		close_delegation_btn_layout = this.findViewById(R.id.close_delegation_btn_layout); // 完成帮助按钮

		evaluation_layout = this.findViewById(R.id.evaluation_layout); // 评价按钮根据权限显示不同的评价
		evaluation = (TextView) this.findViewById(R.id.evaluation);

		evaluation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 根据不同的人进入评价界面
			}
		});

		// 开始加载
		user = ApiContext.getInstance(this).getCurrentUser();

		// TODO：根据传递的参数 获取到 delegationVO seekVO seekUser delegationUser

		seekVO = new SeekVO();
		seekUser = new UserVO();
		bindUser(seekUser, seek_avatar, seek_nickname);
		bindTime(seekVO.getCreatedOn(), seek_created_at);
		seek_content.setText(seekVO.getContent());
		if (seekVO.getClosedOn() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(seekVO.getClosedOn());
			seek_closedOn.setText(DateUtils.toDateString(c.getTime()));
		}

		delegationVO = new DelegationVO();
		delegationUser = new UserVO();
		bindUser(delegationUser, delegation_avatar, delegation_nickname);
		bindTime(delegationVO.getCreatedOn(), delegation_created_at);

		delegation_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 完成委托

			}
		});

		close_delegation_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 关闭委托

			}
		});

	}

	// TODO: 是否seek的创建者
	private boolean isSeekOwner() {
		return true;
	}

	// TODO: 是否委托的创建者
	private boolean isDelegationOwner() {
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
				Intent intent = new Intent(DelegationActivity.this, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				startActivity(intent);
			}
		});
	}
}
