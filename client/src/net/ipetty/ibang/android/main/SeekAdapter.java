package net.ipetty.ibang.android.main;

import java.util.ArrayList;
import java.util.Date;
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

	public SeekAdapter(Activity activity) {
		inflater = LayoutInflater.from(activity);

		SeekVO seekVO = new SeekVO();
		seekVO.setId(111L);
		seekVO.setCategoryL1("分类一");
		seekVO.setCategoryL2("分类二");
		seekVO.setContent("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
		seekVO.setCreatedOn(new Date());
		list.add(seekVO);
	}

	public void loadDate(List<SeekVO> list) {
		this.list.clear();
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return ((SeekVO) this.getItem(arg0)).getId();
	}

	private class ViewHolder {
		public View layout;
		public TextView content;
		public ImageView image;
		public TextView category;
		public TextView time;
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_seek_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.imageView);
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.category = (TextView) view.findViewById(R.id.category);
			holder.time = (TextView) view.findViewById(R.id.time);

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
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(seek.getCreatedOn());
		holder.time.setText(creatAt);

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
