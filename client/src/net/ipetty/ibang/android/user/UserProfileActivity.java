package net.ipetty.ibang.android.user;

import java.io.File;
import java.util.ArrayList;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.approve.ApproveActivity;
import net.ipetty.ibang.android.city.ProvinceActivity;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DeviceUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.PathUtils;
import net.ipetty.ibang.android.core.util.UserUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.UserFormVO;
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
import android.text.Html;
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

	private TextView provinceView;
	private TextView cityView;
	private TextView districtView;
	private TextView categoryView;
	private TextView seekerTitleView;

	private TextView approveView;

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
		ActivityManager.getInstance().addActivity(this);
		user = ApiContext.getInstance(UserProfileActivity.this).getCurrentUser();

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_user_profile));

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
		View city_layout = this.findViewById(R.id.city_layout);
		View category_layout = this.findViewById(R.id.category_layout);

		// 头像
		avatar = (ImageView) this.findViewById(R.id.avatar);
		nicknameView = (TextView) this.findViewById(R.id.nickname);
		phoneView = (TextView) this.findViewById(R.id.phone);
		gender = (TextView) this.findViewById(R.id.gender);
		jobView = (TextView) this.findViewById(R.id.job);
		signatureView = (TextView) this.findViewById(R.id.signature);
		provinceView = (TextView) this.findViewById(R.id.province);
		cityView = (TextView) this.findViewById(R.id.city);
		districtView = (TextView) this.findViewById(R.id.district);
		categoryView = (TextView) this.findViewById(R.id.category);
		seekerTitleView = (TextView) this.findViewById(R.id.seekerTitle);
		approveView = (TextView) this.findViewById(R.id.approve);

		avatar.setOnClickListener(changeAvatarClick);
		nickname_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_NICKNAME));
		phone_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_PHONE));
		signature_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_SIGNATURE));
		job_layout.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_JOB));

		gender_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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

		city_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserProfileActivity.this, ProvinceActivity.class);
				intent.putExtra(Constants.INTENT_LOCATION_TYPE, Constants.INTENT_LOCATION_DISTRICT);
				startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
			}
		});

		category_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserProfileActivity.this, SelectUserCategoryActivity.class);
				startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
			}
		});

		View approve_layout = this.findViewById(R.id.approve_layout);
		approve_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserProfileActivity.this, ApproveActivity.class);
				startActivity(intent);
			}
		});
	}

	private OnClickListener genderClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
			gender.setText(label);
			genderValue = value;
			genderDialog.cancel();

			// 这里与服务器交互，更新用户性别
			user = ApiContext.getInstance(UserProfileActivity.this).getCurrentUser();
			UserFormVO userForm = new UserFormVO();
			userForm.setId(user.getId());
			userForm.setNickname(user.getNickname());
			userForm.setGender(value);
			userForm.setJob(user.getJob());
			userForm.setPhone(user.getPhone());
			userForm.setTelephone(user.getTelephone());
			userForm.setSignature(user.getSignature());
			userForm.setAddress(user.getAddress());
			userForm.setCity(user.getCity());
			userForm.setProvince(user.getProvince());
			userForm.setDistrict(user.getDistrict());

			new UpdateProfileTask(UserProfileActivity.this).setListener(
					new UpdateProfileTaskListener(UserProfileActivity.this)).execute(userForm);
		}
	};

	private class EditOnClickListener implements OnClickListener {
		private String type = null;

		public EditOnClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
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

		if (requestCode == Constants.REQUEST_CODE_USER_EDIT || requestCode == Constants.REQUEST_CODE_CATEGORY) {
			// 从上下文重新获取用户信息
			user = ApiContext.getInstance(UserProfileActivity.this).getCurrentUser();
			initTextUser(user);
		}

		if (requestCode == Constants.REQUEST_CODE_CITY) {
			if (resultCode == RESULT_OK) {

				String province = data.getExtras().getString(Constants.INTENT_LOCATION_PROVINCE);
				String city = data.getExtras().getString(Constants.INTENT_LOCATION_CITY);
				String district = data.getExtras().getString(Constants.INTENT_LOCATION_DISTRICT);
				user.setProvince(province);
				user.setCity(city);
				user.setDistrict(district);

				user = ApiContext.getInstance(UserProfileActivity.this).getCurrentUser();
				UserFormVO userForm = new UserFormVO();
				userForm.setId(user.getId());
				userForm.setNickname(user.getNickname());
				userForm.setGender(user.getGender());
				userForm.setJob(user.getJob());
				userForm.setPhone(user.getPhone());
				userForm.setTelephone(user.getTelephone());
				userForm.setSignature(user.getSignature());
				userForm.setAddress(user.getAddress());
				userForm.setCity(user.getCity());
				userForm.setProvince(user.getProvince());
				userForm.setDistrict(user.getDistrict());

				provinceView.setText(user.getProvince());
				cityView.setText(user.getCity());
				districtView.setText(user.getDistrict());

				new UpdateProfileTask(UserProfileActivity.this).setListener(
						new UpdateProfileTaskListener(UserProfileActivity.this)).execute(userForm);
			}
		}
	}

	private void initTextUser(UserVO user) {
		nicknameView.setText(user.getNickname());
		phoneView.setText(user.getPhone());
		signatureView.setText(user.getSignature());
		jobView.setText(user.getJob());

		provinceView.setText(user.getProvince());
		cityView.setText(user.getCity());
		districtView.setText(user.getDistrict());
		seekerTitleView.setText(user.getSeekerTitle());
		approveView.setText(UserUtils.getIdentityVerified(user));

		StringBuffer str = new StringBuffer();
		for (SeekCategory category : user.getOfferRange()) {
			String l1 = category.getCategoryL1();
			String l2 = category.getCategoryL2();
			str.append("<br>");
			str.append(l1 + "-" + l2);
		}

		String cat = str.toString();
		if (cat.length() > 0) {
			cat = cat.substring(4);
		}
		categoryView.setText(Html.fromHtml(cat));

	}

	public void updateAvatar(final String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			avatar.setImageURI(Uri.fromFile(new File(filePath)));
			// user.setAvatar(filePath);
			// 上传后获取正确的服务器地址

			// 这里与服务器交互，更新用户头像操作
			new UpdateAvatarTask(UserProfileActivity.this).setListener(
					new UpdateAvatarTaskListener(UserProfileActivity.this)).execute(filePath);

		} else {
			avatar.setImageResource(R.drawable.default_avatar);
		}
	}

}
