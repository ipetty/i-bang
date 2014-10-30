package net.ipetty.ibang.android.type;

import java.util.ArrayList;
import java.util.Arrays;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.util.SeekCategoryUtils;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TypeActivity extends Activity {
	public static String CATEGORY_ALL_STRING = "全部";
	public static String CATEGORY_MY_STRING = "我的特长";
	private ArrayList<String> categoryL1 = new ArrayList<String>();

	private String category = CATEGORY_ALL_STRING;
	private String subCategory = CATEGORY_ALL_STRING;

	private ListView list1;
	private ListView list2;

	private SubCategoryAdapter adapter2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type);
		ActivityManager.getInstance().addActivity(this);
		Intent intent = getIntent();
		category = intent.getStringExtra(Constants.INTENT_CATEGORY);
		subCategory = intent.getStringExtra(Constants.INTENT_SUB_CATEGORY);
		if (StringUtils.isEmpty(category)) {
			category = CATEGORY_ALL_STRING;
		}
		if (StringUtils.isEmpty(subCategory)) {
			subCategory = CATEGORY_ALL_STRING;
		}

		View other = this.findViewById(R.id.other);
		other.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		list1 = (ListView) this.findViewById(R.id.list1);
		list2 = (ListView) this.findViewById(R.id.list2);

		categoryL1.add(CATEGORY_ALL_STRING);
		categoryL1.addAll(1, Arrays.asList(SeekCategoryUtils.listL1Categories()));
		list1.setAdapter(new CategoryAdapter());

		list1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				CategoryAdapter adapter = (CategoryAdapter) parent.getAdapter();
				adapter.setSelectedPosition(position);
				adapter.notifyDataSetInvalidated();
				category = (String) adapter.getItem(position);
				adapter.notifyDataSetInvalidated();
				adapter2.setSelectedPosition(-1);
				adapter2.load(getList2(category));
			}
		});

		((CategoryAdapter) list1.getAdapter()).setSelected(category);

		adapter2 = new SubCategoryAdapter();
		list2.setAdapter(adapter2);
		adapter2.load(getList2(category));
		adapter2.setSelected(subCategory);

		list2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				subCategory = (String) adapter2.getItem(position);
				adapter2.setSelectedPosition(position);
				adapter2.notifyDataSetInvalidated();

				String category1 = category;
				String subCategory1 = subCategory;

				if (category == CATEGORY_ALL_STRING) {
					category1 = "";
				}

				if (subCategory == CATEGORY_ALL_STRING) {
					subCategory1 = "";
				}

				Intent intent = new Intent();
				intent.putExtra(Constants.INTENT_CATEGORY, category1);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, subCategory1);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	private ArrayList<String> getList2(String str) {
		ArrayList<String> categoryL2 = new ArrayList<String>();
		categoryL2.add(CATEGORY_ALL_STRING);
		if (str == CATEGORY_ALL_STRING) {
			categoryL2.add(CATEGORY_MY_STRING);
			return categoryL2;
		} else {
			categoryL2.addAll(1, Arrays.asList(SeekCategoryUtils.listL2Categories(str)));
			return categoryL2;
		}
	}

	public class CategoryAdapter extends BaseAdapter implements OnScrollListener {

		private int selectedPosition = 0;

		public void setSelected(String str) {
			selectedPosition = categoryL1.indexOf(str);
			notifyDataSetInvalidated();
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryL1.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryL1.get(position);
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
				LayoutInflater inflater = LayoutInflater.from(TypeActivity.this);
				view = inflater.inflate(R.layout.list_pop_category_item, null);
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
				holder.layout.setBackgroundColor(getResources().getColor(R.color.white_color));
			} else {
				holder.layout.setBackgroundColor(getResources().getColor(R.color.category_bg_color));
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

	public class SubCategoryAdapter extends BaseAdapter implements OnScrollListener {
		private ArrayList<String> list = new ArrayList<String>(0);
		private int selectedPosition = 0;

		public void load(ArrayList<String> list) {
			this.list.clear();
			this.list.addAll(list);
			this.notifyDataSetChanged();
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public void setSelected(String str) {
			selectedPosition = list.indexOf(str);
			notifyDataSetInvalidated();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
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
			// TODO Auto-generated method stub
			View view;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(TypeActivity.this);
				view = inflater.inflate(R.layout.list_pop_sub_category_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.name);
				convertView = view;
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			if (selectedPosition == position) {
				holder.name.setTextColor(getResources().getColor(R.color.base_color));
			} else {
				holder.name.setTextColor(getResources().getColor(R.color.gray_color));
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
