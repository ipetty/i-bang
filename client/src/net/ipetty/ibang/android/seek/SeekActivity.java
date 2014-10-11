package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.SeekVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SeekActivity extends Activity {

	private TextView offerBtn;
	private LinearLayout offerListView;
	private LinearLayout delegationListView;
	private View imageView;
	private ViewPager viewPager;
	private List<ImageView> imageViews = new ArrayList<ImageView>(); // 滑动的图片
	private int currentItem = 0;// 当前图片的索引号
	private TextView imageViewText;
	private SeekVO seekVO = null;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_seek));
		btnBack.setOnClickListener(new BackClickListener(this));

		// image
		imageView = this.findViewById(R.id.imageView_layout);
		viewPager = (ViewPager) findViewById(R.id.vp);
		imageViewText = (TextView) this.findViewById(R.id.imageView_text);

		seekVO = new SeekVO();
		ImageVO img = new ImageVO();
		seekVO.getImages().add(img);
		seekVO.getImages().add(img);

		initImageView();

		offerBtn = (TextView) this.findViewById(R.id.offer);
		offerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SeekActivity.this, PublishOfferActivity.class);
				startActivity(intent);
			}
		});

		delegationListView = (LinearLayout) this.findViewById(R.id.delegationList);
		List<DelegationVO> delegationList = new ArrayList<DelegationVO>();
		DelegationVO tt = new DelegationVO();
		delegationList.add(tt);
		delegationList.add(tt);
		initDelegationView(delegationList);

		offerListView = (LinearLayout) this.findViewById(R.id.offerList);
		List<OfferVO> offerList = new ArrayList<OfferVO>();
		OfferVO t = new OfferVO();
		offerList.add(t);
		offerList.add(t);
		initOfferView(offerList);
	}

	private void initImageView() {

		List<ImageVO> imageList = seekVO.getImages();
		if (imageList.size() == 0) {
			imageView.setVisibility(View.GONE);
			return;
		}

		for (ImageVO imageVO : imageList) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			String url = imageVO.getSmallUrl();
			if (StringUtils.isNotEmpty(url)) {
				ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + url, imageView, options);
			} else {
				imageView.setImageResource(R.drawable.default_seek_images);
			}
			imageView.setOnClickListener(new ImageOnClickListener(imageVO));
			imageViews.add(imageView);
		}

		imageView.setVisibility(View.VISIBLE);
		// TODO Auto-generated method stub
		viewPager.setAdapter(new pageAdapter());// 设置填充Viewpager 页面的适配器
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		setImageViewText();
	}

	public class ImageOnClickListener implements OnClickListener {
		private ImageVO imageVO;

		public ImageOnClickListener(ImageVO imageVO) {
			this.imageVO = imageVO;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 展示大图
			if (StringUtils.isNotEmpty(this.imageVO.getSmallUrl())) {
				Intent intent = new Intent(SeekActivity.this, LargerImageActivity.class);
				intent.putExtra(Constants.INTENT_IMAGE_ORIGINAL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getOriginalUrl());
				intent.putExtra(Constants.INTENT_IMAGE_SAMILL_KEY,
						Constants.FILE_SERVER_BASE + this.imageVO.getSmallUrl());
				startActivity(intent);
			}
		}
	}

	private void setImageViewText() {
		// TODO Auto-generated method stub
		String text = String.valueOf(currentItem + 1) + "/" + String.valueOf(imageViews.size());
		imageViewText.setText(text);
	}

	private class pageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			currentItem = position;
			setImageViewText();

		}

	}

	private void initDelegationView(List<DelegationVO> delegationList) {
		// TODO Auto-generated method stub
		delegationListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (DelegationVO delegationVO : delegationList) {
			View view = inflater.inflate(R.layout.list_delegation_simple_item, null);
			delegationListView.addView(view);
		}
	}

	private void initOfferView(List<OfferVO> offerList) {
		// TODO Auto-generated method stub
		offerListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (OfferVO offer : offerList) {
			View view = inflater.inflate(R.layout.list_offer_item, null);
			offerListView.addView(view);
		}
	}
}
