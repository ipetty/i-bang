package net.ipetty.ibang.android.publish;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constant;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.utils.SeekCategoryUtils;
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

public class PublishSubTypeActivity extends Activity {
	private String title = null;
	private String[] subCategory = null;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_sub_type);

		ActivityManager.getInstance().addActivity(this);
		title = this.getIntent().getExtras().getString(Constant.INTENT_CATEGORY);
		if (title == null) {
			finish();
		}

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(title);
		btnBack.setOnClickListener(new BackClickListener(this));

		subCategory = SeekCategoryUtils.listL2Categories(title);
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new SubCategoryAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PublishSubTypeActivity.this, PublishActivity.class);
				intent.putExtra(Constant.INTENT_CATEGORY, title);
				intent.putExtra(Constant.INTENT_SUB_CATEGORY, subCategory[position]);
				startActivity(intent);
				finish();
			}
		});
	}

	public class SubCategoryAdapter extends BaseAdapter implements OnScrollListener {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return subCategory.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subCategory[position];
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
				LayoutInflater inflater = LayoutInflater.from(PublishSubTypeActivity.this);
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
