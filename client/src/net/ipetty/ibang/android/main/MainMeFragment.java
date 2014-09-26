package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constant;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.user.UserProfileActivity;
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

public class MainMeFragment extends Fragment {
	private boolean isLogin = true;
	public UnLoginView unLoginView;
	public View user_layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constant.BROADCAST_INTENT_UPDATA_USER);
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

		if (isLogin) {
			init();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		unLoginView.hide();

	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constant.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}

			if (Constant.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}

		}

		private void initUser() {
			// TODO Auto-generated method stub

		}

	};
}
