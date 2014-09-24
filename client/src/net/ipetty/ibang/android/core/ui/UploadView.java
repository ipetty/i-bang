package net.ipetty.ibang.android.core.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constant;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.util.DeviceUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.ImageUtils;
import net.ipetty.ibang.android.core.util.PathUtils;
import net.ipetty.ibang.android.publish.UploadLarginImageActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadView {

	private Activity activity;

	private ImageView upload1;
	private ImageView upload2;
	private ImageView upload3;
	private ImageView upload4;

	private List<File> pathList = new ArrayList<File>();
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private ArrayList<ModDialogItem> dialogItems;

	private Dialog cameraDialog;

	public static int REQUEST_CODE__DELETE = 59;
	public int curIndex;

	public UploadView(final Activity activity) {
		this.activity = activity;
		upload1 = (ImageView) activity.findViewById(R.id.upload1);
		upload2 = (ImageView) activity.findViewById(R.id.upload2);
		upload3 = (ImageView) activity.findViewById(R.id.upload3);
		upload4 = (ImageView) activity.findViewById(R.id.upload4);

		upload1.setOnClickListener(new MyOnClickListener(1));
		upload2.setOnClickListener(new MyOnClickListener(2));
		upload3.setOnClickListener(new MyOnClickListener(3));
		upload4.setOnClickListener(new MyOnClickListener(4));

		imageViews.add(upload1);
		imageViews.add(upload2);
		imageViews.add(upload3);
		imageViews.add(upload4);

		dialogItems = new ArrayList<ModDialogItem>();
		dialogItems.add(new ModDialogItem(null, "拍照", takePhotoClick));
		dialogItems.add(new ModDialogItem(null, "从照片选择", pickPhotoClick));
		dialogItems.add(new ModDialogItem(null, "取消", cancelClick));

		this.setImageView();
	}

	private void setImageView() {
		// TODO Auto-generated method stub列表
		int j = pathList.size();

		for (int i = 0; i < imageViews.size(); i++) {
			ImageView upload = imageViews.get(i);
			if (i > j) {
				upload.setVisibility(View.INVISIBLE);
				upload.setImageResource(R.drawable.default_image_add);
			}
			if (i == j) {
				upload.setVisibility(View.VISIBLE);
				upload.setImageResource(R.drawable.default_image_add);
			}
			if (i < j) {
				upload.setImageURI(Uri.fromFile(pathList.get(i)));
			}
		}

	}

	private class MyOnClickListener implements OnClickListener {
		private int i;

		public MyOnClickListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (i > pathList.size()) {
				cameraDialog = DialogUtils.modPopupDialog(activity, dialogItems, cameraDialog);
			} else {
				curIndex = i - 1;
				Intent intent = new Intent(activity, UploadLarginImageActivity.class);
				intent.putExtra(Constant.INTENT_IMAGE_UPLOAD_PATH, pathList.get(curIndex).getAbsolutePath());
				activity.startActivityForResult(intent, REQUEST_CODE__DELETE);
			}
		}
	};

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DeviceUtils.takePicture(activity);
			cameraDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DeviceUtils.chooserSysPics(activity);
			cameraDialog.cancel();
		}
	};

	private final OnClickListener cancelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			cameraDialog.cancel();
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String path = PathUtils.getPathByUriFromFile(uri, activity);
				compressImage(path);
			}
		}

		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				String path = MyAppStateManager.getCameraTempFile(activity);
				compressImage(path);

			}
		}

		if (requestCode == REQUEST_CODE__DELETE) {
			if (resultCode == Activity.RESULT_OK) {
				pathList.remove(curIndex);
			}
			setImageView();
		}
	}

	private void compressImage(String path) {
		if (!ImageUtils.isCorrectSize(path)) {
			Toast.makeText(activity,
					"您选择的图片过小，请大于" + Constant.COMPRESS_IMAGE_MIN_WIDTH + "x" + Constant.COMPRESS_IMAGE_MIN_HEIGHT,
					Toast.LENGTH_LONG).show();
			return;
		}
		File file = new File(path);
		pathList.add(file);
		setImageView();
	}

	public List<File> getFiles() {
		return this.pathList;
	}
}
