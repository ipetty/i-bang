package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.util.AppUtils;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class LargerImageActivity extends Activity {

	private String original_url;
	private String small_url;
	private ImageView image;
	// 手势放大组件
	private PhotoViewAttacher mAttacher;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	// 加载大图前默认透明度
	private int initAlpha = 155;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_larger_image);
		ActivityManager.getInstance().addActivity(this);
		Intent intent = getIntent();
		original_url = intent.getStringExtra(Constants.INTENT_IMAGE_ORIGINAL_KEY);
		small_url = intent.getStringExtra(Constants.INTENT_IMAGE_SAMILL_KEY);
		image = (ImageView) this.findViewById(R.id.image);
		// 设置初始图片
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		image.setAlpha(initAlpha);
		// image.setImageResource(R.drawable.default_image);

		// 同步加载小图
		image = (ImageView) this.findViewById(R.id.image);
		Uri uri = Uri.fromFile(ImageLoader.getInstance().getDiskCache().get(small_url));
		image.setImageURI(uri);

		// 异步加载大图
		ImageLoader.getInstance()
				.displayImage(original_url, image, options, new LoadListener(), new progressListener());
	}

	private class progressListener implements ImageLoadingProgressListener {

		public void onProgressUpdate(String imageUri, View view, int current, int total) {
			int persent = (current / total) * 100;
			if (persent > initAlpha) {
				image.setAlpha(initAlpha);

			}
		}
	}

	private class LoadListener implements ImageLoadingListener {

		public void onLoadingStarted(String imageUri, View view) {

		}

		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

		}

		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			mAttacher = new PhotoViewAttacher(image);
			image.setAlpha(255);

		}

		public void onLoadingCancelled(String imageUri, View view) {
		}

	}
}
