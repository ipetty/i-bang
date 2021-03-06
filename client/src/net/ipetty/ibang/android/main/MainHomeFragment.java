package net.ipetty.ibang.android.main;

import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.city.ProvinceActivity;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import net.ipetty.ibang.android.core.ui.UnLoginView;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.message.MessageActivity;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.search.SearchActivity;
import net.ipetty.ibang.android.seek.ListLatestAvaliableSeeksByCityAndOfferRangeTask;
import net.ipetty.ibang.android.seek.ListLatestAvaliableSeeksByCityOrCategoryTask;
import net.ipetty.ibang.android.seek.ListLatestAvaliableSeeksTaskListener;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.android.type.SelectCategoryActivity;
import net.ipetty.ibang.android.type.SelectSeekTypeActivity;
import net.ipetty.ibang.vo.SeekVO;

import org.apache.commons.lang3.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MainHomeFragment extends Fragment {

	private boolean isLogin = false;
	public UnLoginView unLoginView;
	private TextView cityView;
	private TextView search;
	private ImageView msg;
	private String category = "";
	private String subCategory = "";
	private String type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;
	private MyPullToRefreshListView listView;
	private SeekAdapter adapter;

	private Button categoryView, typeView;
	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;
	private Boolean hasMore = true;

	private String province;
	private String city;
	private String district;

	private Integer currentUserId; // 当前用户ID

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取当前用户
		currentUserId = ApiContext.getInstance(getActivity()).getCurrentUserId();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_IS_LOGIN);
		filter.addAction(Constants.BROADCAST_INTENT_NEW_MESSAGE);
		filter.addAction(Constants.BROADCAST_INTENT_NEW_LETTER);
		filter.addAction(Constants.BROADCAST_INTENT_PUBLISH_SEEK);

		this.getActivity().registerReceiver(broadcastreciver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.getActivity().unregisterReceiver(broadcastreciver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get city from local store;
		province = ApiContext.getInstance(getActivity()).getLocationProvince();
		city = ApiContext.getInstance(getActivity()).getLocationCity();
		district = ApiContext.getInstance(getActivity()).getLocationDistrict();

		cityView = (TextView) this.getView().findViewById(R.id.city);
		search = (TextView) this.getView().findViewById(R.id.search);
		msg = (ImageView) this.getView().findViewById(R.id.msg);

		if (StringUtils.isNotBlank(city)) {
			// 用户之前已选择城市
			cityView.setText(city);
		} else {
			// 用户之前未选择城市
			// TODO 自动定位城市
			Intent intent = new Intent(getActivity(), ProvinceActivity.class);
			intent.putExtra(Constants.INTENT_LOCATION_TYPE, Constants.INTENT_LOCATION_CITY);
			startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
		}
		cityView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProvinceActivity.class);
				intent.putExtra(Constants.INTENT_LOCATION_TYPE, Constants.INTENT_LOCATION_CITY);
				startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});

		msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msg.setImageResource(R.drawable.action_bar_msg);
				Intent intent = new Intent(getActivity(), MessageActivity.class);
				startActivity(intent);
			}
		});

		categoryView = (Button) getView().findViewById(R.id.category);
		View category_layout = getView().findViewById(R.id.category_layout);
		categoryView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectCategoryActivity.class);
				intent.putExtra(Constants.INTENT_CATEGORY, category);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
			}
		});
		category_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectCategoryActivity.class);
				intent.putExtra(Constants.INTENT_CATEGORY, category);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
			}
		});

		typeView = (Button) getView().findViewById(R.id.type);
		View type_layout = getView().findViewById(R.id.type_layout);
		typeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectSeekTypeActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_TYPE, type);
				startActivityForResult(intent, Constants.REQUEST_CODE_TYPE);
			}
		});
		type_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SelectSeekTypeActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_TYPE, type);
				startActivityForResult(intent, Constants.REQUEST_CODE_TYPE);
			}
		});

		listView = (MyPullToRefreshListView) getView().findViewById(R.id.listView);
		adapter = new SeekAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(parent.getAdapter().getItem(position))
						.toString());
				startActivity(intent);
			}
		});

		// listView.hideMoreView();
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MainHomeFragment.this.getActivity().getApplicationContext(),
						getRefreshTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				loadSeeks(true);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (hasMore) {
					loadSeeks(false);
				}
			}
		});

		loadSeeks(true);
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_INTENT_IS_LOGIN.equals(action)) {
				isLogin = true;
				init();
			}
			if (Constants.BROADCAST_INTENT_NEW_MESSAGE.equals(action)) {
				setNewMessage();
			}
			if (Constants.BROADCAST_INTENT_NEW_LETTER.equals(action)) {
				setNewMessage();
			}
			if (Constants.BROADCAST_INTENT_UPDATA_USER.equals(action)) {
				initUser();
			}
			if (Constants.BROADCAST_INTENT_PUBLISH_SEEK.equals(action)) {
				loadSeeks(true);
			}
		}

		private void init() {
			adapter.notifyDataSetChanged();
		}

		private void initUser() {
			adapter.notifyDataSetChanged();
		}

		private void setNewMessage() {
			msg.setImageResource(R.drawable.action_bar_msg_new);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQUEST_CODE_CATEGORY) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				category = intent.getStringExtra(Constants.INTENT_CATEGORY);
				subCategory = intent.getStringExtra(Constants.INTENT_SUB_CATEGORY);
				setCategoryText(category, subCategory);

				loadSeeks(true);
			}
		}

		else if (requestCode == Constants.REQUEST_CODE_TYPE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				type = intent.getStringExtra(Constants.INTENT_SEEK_TYPE);
				typeView.setText(type);

				loadSeeks(true);
			}
		}

		else if (requestCode == Constants.REQUEST_CODE_CITY) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Intent intent = data;
				province = intent.getStringExtra(Constants.INTENT_LOCATION_PROVINCE);
				city = intent.getStringExtra(Constants.INTENT_LOCATION_CITY);
				district = intent.getStringExtra(Constants.INTENT_LOCATION_DISTRICT);
				cityView.setText(city);

				loadSeeks(true);

				// saveTOLocalStore;
				ApiContext.getInstance(getActivity()).setLocationProvince(province);
				ApiContext.getInstance(getActivity()).setLocationCity(city);
				ApiContext.getInstance(getActivity()).setLocationDistrict(district);

				/*
				 * UserVO user =
				 * ApiContext.getInstance(getActivity()).getCurrentUser(); if
				 * (user != null) { user.setProvince(province);
				 * user.setCity(city); user.setDistrict(district); UserFormVO
				 * userForm = new UserFormVO(); userForm.setId(user.getId());
				 * userForm.setNickname(user.getNickname());
				 * userForm.setGender(user.getGender());
				 * userForm.setJob(user.getJob());
				 * userForm.setPhone(user.getPhone());
				 * userForm.setTelephone(user.getTelephone());
				 * userForm.setSignature(user.getSignature());
				 * userForm.setAddress(user.getAddress());
				 * userForm.setCity(user.getCity());
				 * userForm.setProvince(user.getProvince());
				 * userForm.setDistrict(user.getDistrict()); // 更新用户所在地区 new
				 * UpdateProfileTask(getActivity()).setListener(new
				 * DefaultTaskListener<UserVO>(getActivity()) {
				 * 
				 * @Override public void onSuccess(UserVO result) { }
				 * }).execute(userForm); }
				 */
			}
		}
	}

	private void loadSeeks(boolean refresh) {
		if (SelectCategoryActivity.CATEGORY_MY_STRING.equals(subCategory)) {
			// 根据我的帮忙范围加载求助/帮忙列表
			loadSeekByCityAndOfferRange(refresh);
		} else {
			// 根据城市与分类加载求助/帮忙列表
			loadSeekByCityOrCategory(refresh);
		}
	}

	public void loadSeekByCityOrCategory(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据
		new ListLatestAvaliableSeeksByCityOrCategoryTask(getActivity()).setListener(
				new ListLatestAvaliableSeeksTaskListener(MainHomeFragment.this, adapter, listView, isRefresh)).execute(
				type, city, district, category, subCategory,
				net.ipetty.ibang.android.core.util.DateUtils.toDatetimeString(new Date(getRefreshTime())),
				String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	public void loadSeekByCityAndOfferRange(boolean isRefresh) {
		if (isRefresh) {
			pageNumber = 0;
		}
		// 加载数据
		new ListLatestAvaliableSeeksByCityAndOfferRangeTask(getActivity()).setListener(
				new ListLatestAvaliableSeeksTaskListener(MainHomeFragment.this, adapter, listView, isRefresh)).execute(
				type, city, district, currentUserId == null ? null : currentUserId.toString(),
				net.ipetty.ibang.android.core.util.DateUtils.toDatetimeString(new Date(getRefreshTime())),
				String.valueOf(pageNumber++), String.valueOf(pageSize));
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this.getActivity())) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(this.getActivity(), this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(this.getActivity());
	}

	private void setCategoryText(String category, String subCategory) {
		String str = subCategory;
		if (StringUtils.isEmpty(str)) {
			str = category;
		}
		if (StringUtils.isEmpty(str)) {
			str = "全部分类";
		}
		categoryView.setText(str);
	}

	public void loadMoreForResult(List<SeekVO> result) {
		if (result.size() < pageSize) {
			hasMore = false;
			listView.hideMoreView();
		} else {
			hasMore = true;
			listView.showMoreView();
		}
	}

}
