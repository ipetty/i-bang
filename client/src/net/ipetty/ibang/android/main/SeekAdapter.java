package net.ipetty.ibang.android.main;

import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SeekAdapter extends BaseAdapter implements OnScrollListener {
	private List<SeekVO> list;
	LayoutInflater inflater;
	
	public SeekAdapter(Activity activity){
		inflater = LayoutInflater.from(activity);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;//list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return 0;//list.get(0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ViewHolder {
		public View layout;
		public TextView content;
		public TextView delegateNumber;
		public TextView category;
	}
	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_seek_item, null);
			holder = new ViewHolder();
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.category = (TextView) view.findViewById(R.id.category);
			
			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		return view;
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
