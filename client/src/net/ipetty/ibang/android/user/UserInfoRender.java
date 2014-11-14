package net.ipetty.ibang.android.user;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.seek.SeekActivity;
import net.ipetty.ibang.vo.SeekVO;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoRender {
	private String TAG = getClass().getSimpleName();
	public int j = 0;
	public Activity activity;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private List<SeekVO> list = new ArrayList<SeekVO>(0); // 这个就本地dataStore
	private DisplayImageOptions options;
	private int width;

	public UserInfoRender(Activity activity, View view) {
		this.activity = activity;
		DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics(this.activity);
		width = dm.widthPixels / 2;

		layout1 = (LinearLayout) view.findViewById(R.id.layout1);
		layout2 = (LinearLayout) view.findViewById(R.id.layout2);
	}

	public void loadData(List<SeekVO> result) {
		list.clear();
		j = 0;
		layout1.removeAllViews();
		layout2.removeAllViews();
		addData(result);
	}

	public void addData(List<SeekVO> result) {
		list.addAll(result);
		addImage(result);
	}

	private void addImage(List<SeekVO> result) {
		for (int i = 0; i < result.size(); i++) {
			addBitMapToImage(result.get(i), j, i);
			j++;
			if (j >= 2)
				j = 0;
		}
	}

	public class ViewHolder {
		public ImageView image;
		public TextView text;
		public View view;
	}

	public ViewHolder getViewHolder() {
		ViewHolder v = new ViewHolder();
		int px = AnimUtils.dip2px(this.activity, 4f);
		LayoutParams params = new LayoutParams(width - 2 * px, LayoutParams.WRAP_CONTENT);
		LinearLayout ly = new LinearLayout(this.activity);
		ly.setPadding(px, px, px, px);
		params.setMargins(0, 0, 0, px);
		ly.setLayoutParams(params);
		ly.setOrientation(LinearLayout.VERTICAL);
		int x = width - 4 * px;
		LayoutParams layoutParams = new LayoutParams(x, x);
		ImageView imageView = new ImageView(this.activity);
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.default_seek_images);

		TextView tx = new TextView(this.activity);
		tx.setTextSize(AnimUtils.dip2px(this.activity, 9f));
		tx.setTextColor(this.activity.getResources().getColor(R.color.gray_color));
		tx.setEllipsize(TruncateAt.END);
		tx.setMaxLines(1);

		ly.addView(imageView);
		ly.addView(tx);

		v.view = ly;
		v.image = imageView;
		v.text = tx;
		return v;
	}

	public ImageView getImageview() {

		// 建显示图片的对象
		ImageView imageView = new ImageView(this.activity);
		LayoutParams layoutParams = new LayoutParams(width, width);
		layoutParams.setMargins(0, 0, 0, 2);
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.default_seek_images);

		return imageView;
	}

	public void addBitMapToImage(final SeekVO seekVO, int j, int i) {
		ViewHolder viewHolder = getViewHolder();
		if (j == 0) {
			layout1.addView(viewHolder.view);
		} else if (j == 1) {
			layout2.addView(viewHolder.view);
		}

		final long id = seekVO.getId();
		final int pos = i;
		viewHolder.view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, SeekActivity.class);
				intent.putExtra(Constants.INTENT_SEEK_ID, id);
				intent.putExtra(Constants.INTENT_SEEK_JSON, JSONUtils.toJson(seekVO).toString());
				activity.startActivity(intent);
			}
		});
		if (seekVO.getImages().size() > 0) {
			String str = Constants.FILE_SERVER_BASE + seekVO.getImages().get(0).getOriginalUrl();
			Log.d(TAG, str);
			ImageLoader.getInstance().displayImage(str, viewHolder.image, options);
		}
		viewHolder.text.setText(seekVO.getContent());

	}
}
