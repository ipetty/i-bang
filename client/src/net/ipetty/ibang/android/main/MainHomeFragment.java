package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.city.CityActivity;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.message.MessageActivity;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.android.type.TypeActivity;

import org.apache.commons.lang3.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainHomeFragment extends Fragment {

	public UnLoginView unLoginView;
	private TextView city;
	private TextView search;
	private ImageView msg;
	private String category ="";
	private String subCategory ="";
	private ListView list;
	private SeekAdapter adapter;

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
				intent.putExtra(Constants.INTENT_CATEGORY, category);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);

			}
		});
		
		list = (ListView) getView().findViewById(R.id.listView);
		adapter = new SeekAdapter(getActivity());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode ==  Constants.REQUEST_CODE_CATEGORY) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				category = intent.getStringExtra(Constants.INTENT_CATEGORY);
				subCategory = intent.getStringExtra(Constants.INTENT_SUB_CATEGORY);
				setCategoryText(category,subCategory);
				
			}
		}
	}

	private void setCategoryText(String category, String subCategory) {
		String str = subCategory;
		if(StringUtils.isEmpty(str)){
			str = category;
		}
		if(StringUtils.isEmpty(str)){
			str = "全部分类";
		}
		type.setText(str);
	}
}
