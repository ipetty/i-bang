package net.ipetty.ibang.android.publish;

import java.io.File;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UploadLargerImageActivity extends Activity {

	private ImageView image;
	private PhotoViewAttacher mAttacher;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_larger_image);
		ActivityManager.getInstance().addActivity(this);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		ImageView btnDel = (ImageView) this.findViewById(R.id.action_bar_right_image);
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		Intent intent = getIntent();
		path = intent.getStringExtra(Constants.INTENT_IMAGE_UPLOAD_PATH);

		image = (ImageView) this.findViewById(R.id.image);
		// 设置初始图片
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);

		Uri uri = Uri.fromFile(new File(path));
		image.setImageURI(uri);
		mAttacher = new PhotoViewAttacher(image);
	}

}
