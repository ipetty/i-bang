package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.city.CityActivity;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.message.MessageActivity;
import net.ipetty.ibang.android.type.TypeActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainHomeFragment extends Fragment {

	public UnLoginView unLoginView;
	private TextView city;
	private TextView search;
	private ImageView msg;

	private Button type, order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_NEW_MESSAGE);
		this.getActivity().registerReceiver(broadcastreciver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		city = (TextView) this.getView().findViewById(R.id.city);
		search = (TextView) this.getView().findViewById(R.id.search);
		msg = (ImageView) this.getView().findViewById(R.id.msg);

		city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CityActivity.class);
				startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
			}
		});

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		msg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msg.setImageResource(R.drawable.action_bar_msg);
				Intent intent = new Intent(getActivity(), MessageActivity.class);
				startActivity(intent);
			}
		});

		type = (Button) getView().findViewById(R.id.type);
		type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), TypeActivity.class);
				startActivity(intent);

			}
		});
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {

			}

			if (Constants.BROADCAST_INTENT_NEW_MESSAGE.equals(action)) {
				setNewMessage();
			}

		}

		private void setNewMessage() {
			// TODO Auto-generated method stub
			msg.setImageResource(R.drawable.action_bar_msg_new);
		}

	};
}
