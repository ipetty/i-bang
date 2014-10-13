package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.util.SeekCategoryUtils;
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

public class SelectSeekCategoryActivity extends Activity {

	private String categoryL1 = null;
	private String[] l2Categories = null;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_sub_type);

		ActivityManager.getInstance().addActivity(this);
		categoryL1 = this.getIntent().getExtras().getString(Constants.INTENT_CATEGORY);
		if (categoryL1 == null) {
			finish();
		}

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		((TextView) this.findViewById(R.id.action_bar_title)).setText(categoryL1);

		l2Categories = SeekCategoryUtils.listL2Categories(categoryL1);
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new L2CategoriesAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SelectSeekCategoryActivity.this, PublishActivity.class);
				intent.putExtra(Constants.INTENT_CATEGORY, categoryL1);
				intent.putExtra(Constants.INTENT_SUB_CATEGORY, l2Categories[position]);
				startActivity(intent);
				finish();
			}
		});
	}

	public class L2CategoriesAdapter extends BaseAdapter implements OnScrollListener {
		@Override
		public int getCount() {
			return l2Categories.length;
		}

		@Override
		public Object getItem(int position) {
			return l2Categories[position];
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
				LayoutInflater inflater = LayoutInflater.from(SelectSeekCategoryActivity.this);
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
