package net.ipetty.ibang.android.city;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.util.Locations;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class CityActivity extends Activity {
	private ListView listView;
	private String[] citys = {};

	private String province;
	private String city;
	private String district;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		ActivityManager.getInstance().addActivity(this);

		type = this.getIntent().getExtras().getString(Constants.INTENT_LOCATION_TYPE);
		province = this.getIntent().getExtras().getString(Constants.INTENT_LOCATION_PROVINCE);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		((TextView) this.findViewById(R.id.action_bar_title)).setText(province);

		citys = Locations.listCities(province);

		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new CityAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				city = (String) parent.getAdapter().getItem(position);
				if (Constants.INTENT_LOCATION_CITY.equals(type)) {
					Intent intent = new Intent();
					intent.putExtra(Constants.INTENT_LOCATION_PROVINCE, province);
					intent.putExtra(Constants.INTENT_LOCATION_CITY, city);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent(CityActivity.this, DistrictActivity.class);
					intent.putExtra(Constants.INTENT_LOCATION_PROVINCE, province);
					intent.putExtra(Constants.INTENT_LOCATION_CITY, city);
					startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
				}

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

	public class CityAdapter extends BaseAdapter implements OnScrollListener {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return citys.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return citys[position];
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
				LayoutInflater inflater = LayoutInflater.from(CityActivity.this);
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
