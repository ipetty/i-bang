/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.baidu.BaiduApiFactory;
import net.ipetty.ibang.android.baidu.RetVO;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 
 * @author Administrator
 */
public class LocateActivity extends Activity {

	private ImageView btn;
	private ImageView search_del;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		ActivityManager.getInstance().addActivity(this);
		/* action bar */

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		btn = (ImageView) this.findViewById(R.id.action_bar_right_image);
		btn.setOnClickListener(searchOnClickListener);
		search = (EditText) this.findViewById(R.id.search);
		search_del = (ImageView) this.findViewById(R.id.search_del);

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
		showDialog();
		Log.d(TAG, "onCreate OK");
	}

	private OnClickListener searchOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			key = search.getText().toString();

		}
	};

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		// 关闭定位组件
		mLocationClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onStop();
	}

	private void showDialog() {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(true);
		this.progressDialog.setMessage("正在确定您的位置");
		this.progressDialog.show();
	}

	private void dismissDialog() {
		this.progressDialog.dismiss();
	}

	// Geo查询
	private class MyGeoCoderResultListener implements OnGetGeoCoderResultListener {

		public void onGetGeoCodeResult(GeoCodeResult gcr) {

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

			// Receive Location
			StringBuilder sb = new StringBuilder(256);
			sb.append("时间 : ");
			sb.append(location.getTime());
			sb.append("\n定位方式 : ");
			sb.append(location.getLocType());
			sb.append("\n纬度 : ");
			sb.append(location.getLatitude());
			sb.append("\n经度 : ");
			sb.append(location.getLongitude());
			sb.append("\n精度 : ");
			sb.append(location.getRadius());
			sb.append("\n省 : ");
			sb.append(location.getProvince());
			sb.append("\n市 : ");
			sb.append(location.getCity());
			sb.append("\n市编码 : ");
			sb.append(location.getCityCode());
			sb.append("\n区/县 : ");
			sb.append(location.getDistrict());
			sb.append("\n街道 : ");
			sb.append(location.getStreet());
			sb.append("\n门牌号 : ");
			sb.append(location.getStreetNumber());
			sb.append("\n详细地址 : ");
			sb.append(location.getAddrStr());
			Log.d(TAG, sb.toString());
		}

	}

	private void testLBS() {
		String ak = "8691f5dfa58c9acccf7bb4c72e529382";
		String geotable_id = "82284";
		Integer coord_type = 3;
		Double longitude = 121.39842;
		Double latitude = 121.39842;
		String title = "";
		String tags = "";
		String bid = "0000";

		RetVO ret = BaiduApiFactory.getBaiduApi().createPoi(ak, geotable_id, coord_type, longitude, latitude, title,
				tags, bid);
	}

}
