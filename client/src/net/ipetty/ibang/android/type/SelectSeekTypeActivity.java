package net.ipetty.ibang.android.type;

import java.util.ArrayList;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;

import org.apache.commons.lang3.StringUtils;

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
import android.widget.ListView;
import android.widget.TextView;

public class SelectSeekTypeActivity extends Activity {
	public static String TYPE_ALL_STRING = "全部";
	public static String TYPE_TO_HELP_STRING = net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE;
	public static String TYPE_HELP_ME_STRING = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;

	private ListView list;
	private String type;

	private ArrayList<String> typeList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_seek_type);

		ActivityManager.getInstance().addActivity(this);

		typeList.add(TYPE_ALL_STRING);
		typeList.add(TYPE_HELP_ME_STRING);
		typeList.add(TYPE_TO_HELP_STRING);

		Intent intent = getIntent();
		type = intent.getStringExtra(Constants.INTENT_SEEK_TYPE);
		if (StringUtils.isEmpty(type)) {
			type = TYPE_ALL_STRING;
		}

		list = (ListView) this.findViewById(R.id.listView);
		list.setAdapter(new TypeAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TypeAdapter adapter = (TypeAdapter) parent.getAdapter();
				adapter.setSelectedPosition(position);
				adapter.notifyDataSetInvalidated();
				type = (String) adapter.getItem(position);

				if (type.equals(TYPE_ALL_STRING)) {
					type = "";
				}

				Intent intent = new Intent();
				intent.putExtra(Constants.INTENT_SEEK_TYPE, type);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		((TypeAdapter) list.getAdapter()).setSelected(type);

	}

	public class TypeAdapter extends BaseAdapter implements OnScrollListener {

		private int selectedPosition = 0;

		public void setSelected(String str) {
			selectedPosition = typeList.indexOf(str);
			notifyDataSetInvalidated();
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return typeList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return typeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		private class ViewHolder {
			public View layout;
			public TextView name;
		}

		public ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(SelectSeekTypeActivity.this);
				view = inflater.inflate(R.layout.list_pop_sub_category_item, null);
				holder = new ViewHolder();
				holder.layout = view.findViewById(R.id.layout);
				holder.name = (TextView) view.findViewById(R.id.name);
				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			String cat = (String) this.getItem(position);
			holder.name.setText(cat);
			if (selectedPosition == position) {
				holder.name.setTextColor(getResources().getColor(R.color.base_color));
			} else {
				holder.name.setTextColor(getResources().getColor(R.color.gray_color));
			}

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
