package net.ipetty.ibang.android.user;

import java.io.File;
import java.util.ArrayList;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.MyApplication;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DeviceUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.PathUtils;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileActivity extends Activity {
	private ImageView avatar; // 头像
	private Dialog changeAvatarDialog; // 更换头像对话框
	private String mImageName = Constants.PIC_USER_HEAD_IMAGE_NAME; // 默认头像值
	private ArrayList<ModDialogItem> cameraItems;
	private TextView nicknameView;
	private TextView phoneView;
	private TextView signatureView;
	private TextView jobView;

	private ArrayList<ModDialogItem> genderItems;
	private Dialog genderDialog;
	private TextView gender;
	private String genderValue; // 性别传值
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private UserVO user;

	private static final int REQUEST_CODE_PHOTORESOULT = 55;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		user = ((MyApplication) getApplicationContext()).getUser();

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_user_profile));
		btnBack.setOnClickListener(new BackClickListener(this));

		cameraItems = new ArrayList<ModDialogItem>();
		cameraItems.add(new ModDialogItem(null, "拍照", takePhotoClick));
		cameraItems.add(new ModDialogItem(null, "从照片选择", pickPhotoClick));
		cameraItems.add(new ModDialogItem(null, "取消", cancelClick));

		genderItems = new ArrayList<ModDialogItem>();
		genderItems.add(new ModDialogItem(null, "男", "男", genderClick));
		genderItems.add(new ModDialogItem(null, "女", "女", genderClick));

		avatar = (ImageView) this.findViewById(R.id.avatar);
		View nickname_layout = this.findViewById(R.id.nickname_layout);
		View phone_layout = this.findViewById(R.id.phone_layout);
		View gender_layout = this.findViewById(R.id.gender_layout);
		View signature_layout = this.findViewById(R.id.signature_layout);
		View job_layout = this.findViewById(R.id.job_layout);

		// 头像
		avatar = (ImageView) this.findViewById(R.id.avatar);
		nicknameView = (TextView) this.findViewById(R.id.nickname);
		phoneView = (TextView) this.findViewById(R.id.phone);
		gender = (TextView) this.findViewById(R.id.gender);
		jobView = (TextView) this.findViewById(R.id.job);
		signatureView = (TextView) this.findViewById(R.id.signature);

		avatar.setOnClickListener(changeAvatarClick);
		nickname_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_NICKNAME));
		phone_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_PHONE));
		signature_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_SIGNATURE));
		job_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_JOB));

		gender_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				genderDialog = DialogUtils.modPopupDialog(UserProfileActivity.this, genderItems, genderDialog);
			}
		});

		initTextUser(user);
		if (StringUtils.isNotEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
		gender.setText(user.getGender());
	}

	private OnClickListener genderClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
			gender.setText(label);
			genderValue = value;
			genderDialog.cancel();
			user.setGender(value);

			// TODO 这里与服务器交互 更新用户性别
		}
	};

	private class EditOnClickListener implements OnClickListener {
		private String type = null;

		public EditOnClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(UserProfileActivity.this, UserEditActivity.class);
			intent.putExtra(Constants.INTENT_USER_EDIT_TYPE, this.type);
			UserProfileActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_USER_EDIT);
		}
	};

	private OnClickListener changeAvatarClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			showCameraDialog(view);
		}
	};

	public void showCameraDialog(View view) {
		this.changeAvatarDialog = DialogUtils.modPopupDialog(this, cameraItems, changeAvatarDialog);
	}

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.takePicture(UserProfileActivity.this, PathUtils.getCarmerDir(),
					UserProfileActivity.this.mImageName);
			UserProfileActivity.this.changeAvatarDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.chooserSysPics(UserProfileActivity.this);
			UserProfileActivity.this.changeAvatarDialog.cancel();
		}
	};

	private final OnClickListener cancelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			changeAvatarDialog.cancel();
		}
	};

	public void startPhotoZoom(Uri uri, Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", Constants.ZOOM_IMAGE_MAX_WIDTH);
		intent.putExtra("outputY", Constants.ZOOM_IMAGE_MAX_HEIGHT);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("return-data", false);
		startActivityForResult(intent, REQUEST_CODE_PHOTORESOULT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String path = PathUtils.getCarmerDir() + this.mImageName;
		File picture = new File(path);
		Uri pathUri = Uri.fromFile(picture);

		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				startPhotoZoom(uri, pathUri);
			}
		}
		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				startPhotoZoom(Uri.fromFile(picture), pathUri);
			}

		}
		if (requestCode == REQUEST_CODE_PHOTORESOULT) {
			avatar.setImageURI(pathUri);
			updateAvatar(picture.getAbsolutePath());
		}

		if (requestCode == Constants.REQUEST_CODE_USER_EDIT) {
			// 从上下文重新获取用户信息
			initTextUser(((MyApplication) getApplicationContext()).getUser());
		}
	}

	private void initTextUser(UserVO user) {
		nicknameView.setText(user.getNickname());
		phoneView.setText(user.getPhone());
		signatureView.setText(user.getSignature());
		jobView.setText(user.getJob());
	}

	public void updateAvatar(final String filePath) {

		if (StringUtils.isNotBlank(filePath)) {
			avatar.setImageURI(Uri.fromFile(new File(filePath)));
			// user.setAvatar(filePath);
			// 上传后获取正确的服务器地址

			// TODO: 这里与服务器交互 更新用户头像操作

		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}

	}
}
