package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.setting.SettingActivity;
import net.ipetty.ibang.android.user.UserProfileActivity;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainMeFragment extends Fragment {
	private boolean isLogin = true;
	public UnLoginView unLoginView;
	public View user_layout;
	private UserVO user;

	private ImageView avatar;
	private TextView nickname;
	private TextView signature;
	private View setting;

	private DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_avatar).build();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		this.getActivity().registerReceiver(broadcastreciver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_me, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		unLoginView = new UnLoginView(getActivity(), getView(), R.string.un_login_me);
		user_layout = this.getActivity().findViewById(R.id.user_layout);
		user_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserProfileActivity.class);
				startActivity(intent);
			}
		});
		setting = getActivity().findViewById(R.id.action_bar_right_image);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent);
			}
		});

		avatar = (ImageView) getActivity().findViewById(R.id.avatar);
		nickname = (TextView) getActivity().findViewById(R.id.nickname);
		signature = (TextView) getActivity().findViewById(R.id.signature);

		if (isLogin) {
			init();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		unLoginView.hide();
		initUser();
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}

			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}

		}
	};

	private void initUser() {
		user = ((MyApplication) getActivity().getApplicationContext()).getUser();
		nickname.setText(user.getNickname());
		signature.setText(user.getSignature());

		if (StringUtils.isNotBlank(user.getAvatar())) {
			String str = Constants.FILE_SERVER_BASE + user.getAvatar();
			ImageLoader.getInstance().displayImage(str, avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
	}
}
