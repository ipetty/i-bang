package net.ipetty.ibang.android.city;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.util.Locations;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DistrictActivity extends Activity {
	private ListView listView;
	private String province;
	private String city;
	private String district;

	private String[] districts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_district);
		ActivityManager.getInstance().addActivity(this);

		province = this.getIntent().getExtras().getString(Constants.INTENT_LOCATION_PROVINCE);
		city = this.getIntent().getExtras().getString(Constants.INTENT_LOCATION_CITY);
		districts = Locations.listDistricts(city);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		((TextView) this.findViewById(R.id.action_bar_title)).setText(city);
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new CityAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				district = (String) parent.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra(Constants.INTENT_LOCATION_PROVINCE, province);
				intent.putExtra(Constants.INTENT_LOCATION_CITY, city);
				intent.putExtra(Constants.INTENT_LOCATION_DISTRICT, district);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	public class CityAdapter extends BaseAdapter implements OnScrollListener {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return districts.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return districts[position];
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
				LayoutInflater inflater = LayoutInflater.from(DistrictActivity.this);
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
