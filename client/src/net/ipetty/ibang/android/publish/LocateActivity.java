/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.LocationVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * @author Administrator
 */
public class LocateActivity extends Activity {

	// 搜索按钮
	private ImageView search_btn;
	private ImageView search_del;
	// 搜索输入
	private EditText search;
	private String key;

	private String TAG = getClass().getSimpleName();
	private ProgressDialog progressDialog;
	// 定位相关
	private LocationClient mLocationClient;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private boolean isFirstLoc = true;// 是否首次定位
	private Marker mMarkerA;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private TextView address;
	private TextView okBtn;
	private TextView cityView;
	private String city;

	// 定位结果
	private ReverseGeoCodeResult locationResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		cityView = (TextView) this.findViewById(R.id.city);
		cityView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocateActivity.this, "请返回首页进行城市切换", Toast.LENGTH_LONG).show();
			}
		});

		city = ApiContext.getInstance(this).getLocationCity();
		cityView.setText(city);

		search_btn = (ImageView) this.findViewById(R.id.action_bar_right_image);
		search_btn.setOnClickListener(searchOnClickListener);
		search = (EditText) this.findViewById(R.id.search);
		search_del = (ImageView) this.findViewById(R.id.search_del);
		okBtn = (TextView) this.findViewById(R.id.button);
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				if (locationResult == null) {
					return;
				}
				Intent intent = new Intent(Constants.BROADCAST_INTENT_LOCATION);

				String localCity = locationResult.getAddressDetail().city;
				if (!localCity.equals(city)) {
					Toast.makeText(LocateActivity.this, "您当前位置与选择城市不一致，请返回首页进行城市切换", Toast.LENGTH_LONG).show();
					return;
				}

				LocationVO location = new LocationVO();
				location.setProvince(locationResult.getAddressDetail().province);
				location.setCity(locationResult.getAddressDetail().city);
				location.setDistrict(locationResult.getAddressDetail().district);
				location.setAddress(locationResult.getAddress());
				location.setLatitude(locationResult.getLocation().latitude);
				location.setLongitude(locationResult.getLocation().longitude);

				intent.putExtra(Constants.BROADCAST_INTENT_LOCATION_DATA, JSONUtils.toJson(location));
				LocateActivity.this.sendBroadcast(intent);
				LocateActivity.this.finish();
			}
		});

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					search_del.setVisibility(View.VISIBLE);
				} else {
					search_del.setVisibility(View.INVISIBLE);
				}
			}
		});

		search_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search.setText("");
			}
		});
		search_del.setVisibility(View.INVISIBLE);

		address = (TextView) this.findViewById(R.id.address);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 设置地图缩放
		MapStatusUpdate u2 = MapStatusUpdateFactory.zoomTo(16);
		mBaiduMap.animateMapStatus(u2);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStateChange());
		// 定位初始化
		mLocationClient = ((MyApplication) getApplication()).mLocationClient;
		// 位置监听函数
		mLocationClient.registerLocationListener(new MyLocationListener());
		mLocationClient.start();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new MyGeoCoderResultListener());
		showDialog("正在确定您的位置");
		Log.d(TAG, "onCreate OK");
	}

	private OnClickListener searchOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog("正在搜索");
			key = search.getText().toString();
			GeoCodeOption geoCodeOption = new GeoCodeOption();
			geoCodeOption.address(key).city("");
			mSearch.geocode(geoCodeOption);
		}
	};

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		// 关闭定位组件
		mLocationClient.stop();
		super.onStop();
	}

	private void showDialog(String msg) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(true);
		this.progressDialog.setMessage(msg);
		this.progressDialog.show();
	}

	private void dismissDialog() {
		this.progressDialog.dismiss();
	}

	// Geo查询
	private class MyGeoCoderResultListener implements OnGetGeoCoderResultListener {

		public void onGetGeoCodeResult(GeoCodeResult result) {
			dismissDialog();
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(LocateActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
				return;
			}

			LatLng location = result.getLocation();

			// 在地图中间增加Markerm BaiduMap.getMapStatus().target
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			if (mMarkerA == null) {
				OverlayOptions ooA = new MarkerOptions().position(location).icon(bd).zIndex(9).draggable(false);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			} else {
				mMarkerA.setPosition(location);
			}
			LatLng ll = new LatLng(location.latitude, location.longitude);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);

		}

		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// Toast.makeText(LocateActivity.this, "抱歉，未能找到结果",
				// Toast.LENGTH_LONG).show();
				address.setText("抱歉，未能找到结果");
				return;
			}
			// Toast.makeText(LocateActivity.this, result.getAddress(),
			// Toast.LENGTH_LONG).show();
			address.setText(result.getAddress());
			locationResult = result;
		}

	}

	// 地图状态变化
	private class OnMapStateChange implements OnMapStatusChangeListener {

		public void onMapStatusChangeStart(MapStatus ms) {
			Log.d(TAG, "onMapStatusChangeStart");
		}

		public void onMapStatusChange(MapStatus ms) {
			Log.d(TAG, "onMapStatusChange");
			// 在地图中间增加Markerm BaiduMap.getMapStatus().target
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			if (mMarkerA == null) {
				OverlayOptions ooA = new MarkerOptions().position(ms.target).icon(bd).zIndex(9).draggable(false);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			} else {
				mMarkerA.setPosition(ms.target);
			}
		}

		public void onMapStatusChangeFinish(MapStatus ms) {
			Log.d(TAG, "onMapStatusChangeFinish");
			// 在地图中间增加Markerm BaiduMap.getMapStatus().target
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			if (mMarkerA == null) {
				OverlayOptions ooA = new MarkerOptions().position(ms.target).icon(bd).zIndex(9).draggable(false);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			} else {
				mMarkerA.setPosition(ms.target);
			}
			mBaiduMap.setMyLocationEnabled(false);
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ms.target));
		}
	}

	/**
	 * 实现实位回调监听
	 */
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			Log.d(TAG, "onReceiveLocation");
			dismissDialog();
			if (location == null || StringUtils.isBlank(location.getCity())) {
				// Toast.makeText(LocateActivity.this, "定位失败，请返回再次重试",
				// Toast.LENGTH_LONG).show();
				address.setText("定位失败，请手动选择");
				Log.d(TAG, "定位失败");
				return;
			}
			// 设置当前位置
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

	}

}
