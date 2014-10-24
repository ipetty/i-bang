/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.publish;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.MyPullToRefreshListView;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Administrator
 */
public class LocateActivity extends Activity {

        private String TAG = getClass().getSimpleName();
        private LocationClient mLocationClient;
        private MyPullToRefreshListView listView;
        private TextView textView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                Log.i(TAG, "onCreate");
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_locate);
                ActivityManager.getInstance().addActivity(this);
                /* action bar */
                ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
                btnBack.setOnClickListener(new BackClickListener(this));
                textView = (TextView) this.findViewById(R.id.textView);
                listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
                listView.setAdapter(new PoiAdapter());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv = (TextView) view;

                                //finish();
                        }
                });

                mLocationClient = ((MyApplication) getApplication()).mLocationClient;
                mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
                mLocationClient.start();
                Log.i(TAG, "onCreate OK");
        }

        @Override
        protected void onStop() {
                Log.i(TAG, "onStop");
                mLocationClient.stop();
                super.onStop();
        }

        /**
         * 实现实位回调监听
         */
        private class MyLocationListener implements BDLocationListener {

                @Override
                public void onReceiveLocation(final BDLocation location) {
                        Log.i(TAG, "onReceiveLocation");
                        if (location == null) {
                                return;
                        }
                        if (location == null || StringUtils.isBlank(location.getCity())) {//定位失败
                                Log.i(TAG, "定位失败");
                        } else {

                        }

                        //Receive Location
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
                        textView.setText(textView.getText() + sb.toString());
                        Log.i(TAG, sb.toString());
                }

        }

        public class PoiAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

                @Override
                public int getCount() {
                        return 10;
                }

                @Override
                public Object getItem(int position) {
                        return position;
                }

                @Override
                public long getItemId(int position) {
                        return 0;
                }

                private class ViewHolder {

                        public TextView name;
                }

                public ViewHolder holder;

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        View view;
                        if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(LocateActivity.this);
                                view = inflater.inflate(R.layout.list_poi_item, null);
                                holder = new ViewHolder();
                                holder.name = (TextView) view.findViewById(R.id.name);
                                convertView = view;
                                convertView.setTag(holder);
                        } else {
                                view = convertView;
                                holder = (ViewHolder) view.getTag();
                        }
                        String cat = String.valueOf(position); //this.getItem(position);
                        holder.name.setText(cat);
                        return view;
                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // TODO Auto-generated method stub
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        // TODO Auto-generated method stub
                }
        }

}
