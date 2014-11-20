package net.ipetty.ibang.android.core.util;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.user.UserInfoActivity;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserUtils {
	private static DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_avatar).build();

	public static String getIdentityVerified(UserVO user) {
		String text = "未认证";
		if (user.isIdentityVerified()) {
			text = "已认证";
		}
		return text;
	}

	public static void bindIdentityVerified(UserVO user, TextView tv) {
		String text = getIdentityVerified(user);
		tv.setText(text);
	}

	public static void bindIdentityVerified(UserVO user, ImageView image) {
		if (user.isIdentityVerified()) {
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.GONE);
		}
	}

	public static void bindUser(final UserVO user, final Activity activity, ImageView avatar, TextView nickname,
			ImageView approve) {
		bindUser(user, activity, avatar, nickname);
		bindIdentityVerified(user, approve);
	}

	public static void bindUser(final UserVO user, final Activity activity, ImageView avatar, TextView nickname) {
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		}
		nickname.setText(user.getNickname());
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				activity.startActivity(intent);
			}
		});
		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, UserInfoActivity.class);
				intent.putExtra(Constants.INTENT_USER_ID, user.getId());
				intent.putExtra(Constants.INTENT_USER_JSON, JSONUtils.toJson(user).toString());
				activity.startActivity(intent);
			}
		});
	}
}
