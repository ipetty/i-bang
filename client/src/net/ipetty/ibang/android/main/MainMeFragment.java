package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.seek.MyEvaluationActivity;
import net.ipetty.ibang.android.seek.MyOfferActivity;
import net.ipetty.ibang.android.seek.MySeekActivity;
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

	private boolean isLogin = false;
	public UnLoginView unLoginView;
	public View user_layout;
	private UserVO user;

	private ImageView avatar;
	private TextView nickname;
	private TextView signature;
	private View setting;
	private View seek_layout;
	private View delegation_layout;

	private TextView seekCount;
	private TextView offerCount;
	private TextView seekerTotalPoint;

	private View evaluation_layout;

	private DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_avatar).build();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isLogin = ApiContext.getInstance(MainMeFragment.this.getActivity()).isAuthorized();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		this.getActivity().registerReceiver(broadcastreciver, filter);
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
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent);
			}
		});

		avatar = (ImageView) getView().findViewById(R.id.avatar);
		nickname = (TextView) getView().findViewById(R.id.nickname);
		signature = (TextView) getView().findViewById(R.id.signature);
		seekCount = (TextView) getView().findViewById(R.id.seekCount);
		offerCount = (TextView) getView().findViewById(R.id.offerCount);
		seekerTotalPoint = (TextView) getView().findViewById(R.id.seekerTotalPoint);

		View seekerTotalPoint_layout = getView().findViewById(R.id.seekerTotalPoint_layout);
		seekerTotalPoint_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyEvaluationActivity.class);
				startActivity(intent);
			}
		});

		View seekCount_layout = getView().findViewById(R.id.seekCount_layout);
		seekCount_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MySeekActivity.class);
				startActivity(intent);
			}
		});

		seek_layout = getView().findViewById(R.id.seek_layout);
		seek_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MySeekActivity.class);
				startActivity(intent);
			}
		});

		View offerCount_layout = getView().findViewById(R.id.offerCount_layout);
		offerCount_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyOfferActivity.class);
				startActivity(intent);
			}
		});

		delegation_layout = getView().findViewById(R.id.delegation_layout);
		delegation_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyOfferActivity.class);
				startActivity(intent);
			}
		});

		// TODO：评价列表
		evaluation_layout = getView().findViewById(R.id.evaluation_layout);
		evaluation_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyEvaluationActivity.class);
				startActivity(intent);
			}
		});

		isLogin = ApiContext.getInstance(MainMeFragment.this.getActivity()).isAuthorized();
		if (isLogin) {
			init();
		}
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}
			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}
		}
	};

	private void init() {
		unLoginView.hide();
		initUser();
	}

	private void initUser() {
		user = ApiContext.getInstance(getActivity()).getCurrentUser();
		nickname.setText(user.getNickname());
		signature.setText(user.getSignature());
		seekCount.setText(String.valueOf(user.getSeekCount()));
		offerCount.setText(String.valueOf(user.getOfferCount()));
		seekerTotalPoint.setText(String.valueOf(user.getSeekerTotalPoint()));

		if (StringUtils.isNotBlank(user.getAvatar())) {
			String str = Constants.FILE_SERVER_BASE + user.getAvatar();
			ImageLoader.getInstance().displayImage(str, avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		this.getActivity().unregisterReceiver(broadcastreciver);
	}

}
