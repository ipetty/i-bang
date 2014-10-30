package net.ipetty.ibang.android.city;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.util.Locations;
import org.apache.commons.lang3.StringUtils;

public class ProvinceActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private ListView listView;
    private String province;
    private String city;
    private String district;
    private String type;

    private String[] provinces = Locations.listProvinces();

    private LocationClient mLocationClient;
    private TextView locateMessageTextView;
    private LinearLayout locationLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        ActivityManager.getInstance().addActivity(this);

        ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
        btnBack.setOnClickListener(new BackClickListener(this));
        ((TextView) this.findViewById(R.id.action_bar_title)).setText(R.string.title_activity_city);

        type = this.getIntent().getExtras().getString(Constants.INTENT_LOCATION_TYPE);

        listView = (ListView) this.findViewById(R.id.listView);
        listView.setAdapter(new ProvincesAdapter());
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                province = (String) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
                intent.putExtra(Constants.INTENT_LOCATION_TYPE, type);
                intent.putExtra(Constants.INTENT_LOCATION_PROVINCE, province);
                startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
            }
        });

        mLocationClient = ((MyApplication) getApplication()).mLocationClient;
        mLocationClient.registerLocationListener(new MyLocationListener());
        locateMessageTextView = (TextView) this.findViewById(R.id.textView2);
        locationLayout = (LinearLayout) this.findViewById(R.id.locationLayout);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mLocationClient.start();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_CITY) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                Intent intent = data;
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            Log.d(TAG, "onReceiveLocation");
            if (location == null || StringUtils.isBlank(location.getCity())) {
                locateMessageTextView.setText("定位失败,请在下面列表中选择...");
                return;
            }
            locateMessageTextView.setText(location.getCity());
            Intent intent = new Intent();
            String province = location.getProvince();
            province = province.replace("市", "");
            intent.putExtra(Constants.INTENT_LOCATION_PROVINCE, province);
            intent.putExtra(Constants.INTENT_LOCATION_CITY, location.getCity());
            intent.putExtra(Constants.INTENT_LOCATION_DISTRICT, location.getDistrict());
            setResult(RESULT_OK, intent);
            Log.i(TAG, province + " " + location.getCity() + " " + location.getDistrict());
            finish();
        }

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        mLocationClient.stop();
        super.onStop();
    }

    public class ProvincesAdapter extends BaseAdapter implements OnScrollListener {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return provinces.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return provinces[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
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
                LayoutInflater inflater = LayoutInflater.from(ProvinceActivity.this);
                view = inflater.inflate(R.layout.list_sub_category_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) view.findViewById(R.id.name);
                convertView = view;
                convertView.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            String cat = (String) this.getItem(position);
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
