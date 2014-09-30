package net.ipetty.ibang.android.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.util.SeekCategoryUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TypeActivity extends Activity {
	private ArrayList<String> categoryL1 = new ArrayList<String>();
	private List<String> categoryL2;

	private String category;
	private String subCategroy;

	private ListView list1;
	private ListView list2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type);

		list1 = (ListView) this.findViewById(R.id.list1);
		list2 = (ListView) this.findViewById(R.id.list2);

		categoryL1.add("全部");
		categoryL1.addAll(1, Arrays.asList(SeekCategoryUtils.listL1Categories()));

		list1.setAdapter(new CategoryAdapter());

	}

	public class CategoryAdapter extends BaseAdapter implements OnScrollListener {

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
