package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.publish.PublishSubTypeActivity;
import net.ipetty.ibang.android.sdk.context.ApiContext;
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

public class MainPublishFragment extends Fragment {

	private boolean isLogin = false;
	private UnLoginView unLoginView;

	private View layout_jzfu;
	private View layout_edu;
	private View layout_it;
	private View layout_car;
	private View layout_express;
	private View layout_travel;
	private View layout_healthy;
	private View layout_repair;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLogin = ApiContext.getInstance(getActivity()).isAuthorized();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_UPDATA_USER);
		this.getActivity().registerReceiver(broadcastreciver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_publish, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		unLoginView = new UnLoginView(getActivity(), getView(), R.string.un_login_publish);

		layout_jzfu = getView().findViewById(R.id.layout_jzfw);
		layout_edu = getView().findViewById(R.id.layout_edu);
		layout_it = getView().findViewById(R.id.layout_it);
		layout_car = getView().findViewById(R.id.layout_car);
		layout_express = getView().findViewById(R.id.layout_express);
		layout_travel = getView().findViewById(R.id.layout_travel);
		layout_healthy = getView().findViewById(R.id.layout_healthy);
		layout_repair = getView().findViewById(R.id.layout_repair);

		layout_jzfu.setOnClickListener(new PublishSubTypeListener("家政服务"));
		layout_it.setOnClickListener(new PublishSubTypeListener("IT服务"));
		layout_edu.setOnClickListener(new PublishSubTypeListener("教育培训"));
		layout_healthy.setOnClickListener(new PublishSubTypeListener("健康安全"));
		layout_repair.setOnClickListener(new PublishSubTypeListener("家电维修"));
		layout_car.setOnClickListener(new PublishSubTypeListener("汽车服务"));
		layout_travel.setOnClickListener(new PublishSubTypeListener("旅游休闲"));
		layout_express.setOnClickListener(new PublishSubTypeListener("物流运输"));

		isLogin = ApiContext.getInstance(getActivity()).isAuthorized();
		if (isLogin) {
			init();
		}
	}

	public class PublishSubTypeListener implements OnClickListener {
		String type;

		public PublishSubTypeListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), PublishSubTypeActivity.class);
			intent.putExtra(Constants.INTENT_CATEGORY, this.type);
			startActivity(intent);
		}
	}

	private void init() {
		unLoginView.hide();
	}

	private void initUser() {
		// TODO Auto-generated method stub
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.getActivity().unregisterReceiver(broadcastreciver);
	}

}
