package net.ipetty.ibang.android.main;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.PrettyDateFormat;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SeekAdapter extends BaseAdapter implements OnScrollListener {

	private List<SeekVO> list = new ArrayList<SeekVO>();
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private LayoutInflater inflater;
	private Integer resId;

	public SeekAdapter(Activity activity) {
		inflater = LayoutInflater.from(activity);
	}

	public SeekAdapter(Activity activity, int resId) {
		inflater = LayoutInflater.from(activity);
		this.resId = resId;
	}

	public void loadData(List<SeekVO> list) {
		this.list.clear();
		this.addData(list);
	}

	public void addData(List<SeekVO> list) {
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return ((SeekVO) this.getItem(arg0)).getId();
	}

	private class ViewHolder {
		public View layout;
		public TextView content;
		public ImageView image;
		public TextView category;
		public TextView time;
		public TextView local;
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			if (resId == null) {
				resId = R.layout.list_seek_item;
			}
			view = inflater.inflate(resId, null);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.imageView);
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.category = (TextView) view.findViewById(R.id.category);

			if (resId == R.layout.list_seek_item) {
				holder.time = (TextView) view.findViewById(R.id.time);
			} else {
				holder.local = (TextView) view.findViewById(R.id.local);
			}

			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		SeekVO seek = (SeekVO) this.getItem(position);
		if (seek.getImages().size() == 0) {
			holder.image.setImageResource(R.drawable.default_seek_images);
		} else {
			ImageVO image = seek.getImages().get(0);
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + image.getSmallUrl(), holder.image,
					options);
		}

		String category = seek.getCategoryL1() + "-" + seek.getCategoryL2();
		holder.category.setText(category);
		holder.content.setText(seek.getContent());

		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd").format(seek.getCreatedOn());

		if (resId == R.layout.list_seek_item) {
			holder.time.setText(creatAt);
		} else {
			// TODO:距离远近
			holder.local.setText("3公里");
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
