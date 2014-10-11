package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.publish.PublishSubTypeActivity;
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
	private boolean isLogin = true;
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

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
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

		layout_jzfu.setOnClickListener(new MyOnClickListener("家政服务"));
		layout_it.setOnClickListener(new MyOnClickListener("IT服务"));
		layout_edu.setOnClickListener(new MyOnClickListener("教育培训"));
		layout_healthy.setOnClickListener(new MyOnClickListener("健康安全"));
		layout_repair.setOnClickListener(new MyOnClickListener("家电维修"));
		layout_car.setOnClickListener(new MyOnClickListener("汽车服务"));
		layout_travel.setOnClickListener(new MyOnClickListener("旅游休闲"));
		layout_express.setOnClickListener(new MyOnClickListener("物流运输"));

		if (isLogin) {
			init();
		}

	}

	public class MyOnClickListener implements OnClickListener {
		String type;

		public MyOnClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), PublishSubTypeActivity.class);
			intent.putExtra(Constants.INTENT_CATEGORY, this.type);
			startActivity(intent);
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

			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				init();
			}

		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.getActivity().unregisterReceiver(broadcastreciver);
	}
}
