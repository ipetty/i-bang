package net.ipetty.ibang.android.approve;

import java.io.File;
import java.util.ArrayList;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.MyAppStateManager;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DeviceUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.ImageUtils;
import net.ipetty.ibang.android.core.util.PathUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.seek.LargerImageActivity;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ApproveActivity extends Activity {

	private UserVO user;
	private EditText realname;
	private EditText idNum;
	private TextView submitButton;
	private View operation_layout;
	private View desc_layout;
	private View status_layout;
	private TextView status;
	private ImageView idCardInHand;
	private String idCardInHandFilePath;
	private IdentityVerificationVO approve;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private String mImageName = Constants.PIC_IDCARD_IN_HAND; // 默认头像值

	private Dialog dialog;
	private ArrayList<ModDialogItem> cameraItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approve);
		ActivityManager.getInstance().addActivity(this);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_approve));

		cameraItems = new ArrayList<ModDialogItem>();
		cameraItems.add(new ModDialogItem(null, "拍照", takePhotoClick));
		cameraItems.add(new ModDialogItem(null, "从照片选择", pickPhotoClick));
		cameraItems.add(new ModDialogItem(null, "取消", cancelClick));

		user = ApiContext.getInstance(ApproveActivity.this).getCurrentUser();

		realname = (EditText) this.findViewById(R.id.realname);
		idNum = (EditText) this.findViewById(R.id.id_number);
		idCardInHand = (ImageView) this.findViewById(R.id.id_cardinhand);
		operation_layout = this.findViewById(R.id.operation_layout);
		desc_layout = this.findViewById(R.id.desc_layout);
		submitButton = (TextView) this.findViewById(R.id.button);
		status_layout = this.findViewById(R.id.status_layout);
		status = (TextView) this.findViewById(R.id.status);

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 提交身份认证
				if (user != null) {
					IdentityVerificationVO identityVerification = new IdentityVerificationVO();
					identityVerification.setUserId(user.getId());
					identityVerification.setRealName(realname.getText().toString());
					identityVerification.setIdNumber(idNum.getText().toString());
					identityVerification.setIdCardInHand(idCardInHandFilePath);

					new SubmitIdentityVerificationTask(ApproveActivity.this).setListener(
							new DefaultTaskListener<Boolean>(ApproveActivity.this, "正在提交身份认证资料...") {

								@Override
								public void onSuccess(Boolean result) {
									// 提交后加载数据并初始化视图
									ApproveActivity.this.loadDataAndInitView();
								}
							}).execute(identityVerification);
				}
			}
		});

		// 用户获取认证过程
		this.loadDataAndInitView();
	}

	/**
	 * 加载数据并初始化视图
	 */
	private void loadDataAndInitView() {
		if (user != null) {
			new GetIdentityVerificationTask(ApproveActivity.this).setListener(
					new DefaultTaskListener<IdentityVerificationVO>(ApproveActivity.this, "正在加载数据...") {

						@Override
						public void onSuccess(IdentityVerificationVO result) {
							approve = result;
							ApproveActivity.this.initLayout();
						}
					}).execute();
		}
	}

	/**
	 * 初始化视图
	 */
	private void initLayout() {
		// 如果认证过
		if (approve != null) {
			realname.setText(approve.getRealName());
			idNum.setText(approve.getIdNumber());
			if (StringUtils.isNotEmpty(approve.getIdCardInHand())) {
				ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + approve.getIdCardInHand(),
						idCardInHand, options);
			} else {
				idCardInHand.setImageResource(R.drawable.default_image_add);
			}
		}
		// 状态显示
		if (approve == null) {
			status_layout.setVisibility(View.GONE);
		} else {
			if (net.ipetty.ibang.vo.Constants.ID_VERIFICATION_VERIFYING.equals(approve.getStatus())) {
				status.setText("实名认证审核中...");
			}
			if (net.ipetty.ibang.vo.Constants.ID_VERIFICATION_APPROVED.equals(approve.getStatus())) {
				status.setText("您已通过实名认证");
			}
			if (net.ipetty.ibang.vo.Constants.ID_VERIFICATION_UNAPPROVED.equals(approve.getStatus())) {
				status.setText("实名认证审核未通过");
			}
			status_layout.setVisibility(View.VISIBLE);
		}

		// 如果未认证 或者 认证没通过
		if (approve == null || net.ipetty.ibang.vo.Constants.ID_VERIFICATION_UNAPPROVED.equals(approve.getStatus())) {
			realname.setEnabled(true);
			idNum.setEnabled(true);
			operation_layout.setVisibility(View.VISIBLE);
			desc_layout.setVisibility(View.VISIBLE);
		} else {
			initApproved();
		}

		idCardInHand.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (approve == null
						|| (approve != null && net.ipetty.ibang.vo.Constants.ID_VERIFICATION_UNAPPROVED.equals(approve
								.getStatus()))) {
					editImage();
				} else {
					showImage();
				}
			}
		});
	}

	private void editImage() {
		this.dialog = DialogUtils.modPopupDialog(this, cameraItems, dialog);
	}

	private final OnClickListener takePhotoClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			DeviceUtils.takePicture(ApproveActivity.this, PathUtils.getCarmerDir(), ApproveActivity.this.mImageName);
			ApproveActivity.this.dialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			DeviceUtils.chooserSysPics(ApproveActivity.this);
			ApproveActivity.this.dialog.cancel();
		}
	};

	private final OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dialog.cancel();
		}
	};

	private void showImage() {
		Intent intent = new Intent(this, LargerImageActivity.class);
		intent.putExtra(Constants.INTENT_IMAGE_ORIGINAL_KEY, Constants.FILE_SERVER_BASE + approve.getIdCardInHand());
		intent.putExtra(Constants.INTENT_IMAGE_SAMILL_KEY, Constants.FILE_SERVER_BASE + approve.getIdCardInHand());
		startActivity(intent);
	}

	private void initApproved() {
		realname.setEnabled(false);
		idNum.setEnabled(false);
		operation_layout.setVisibility(View.GONE);
		desc_layout.setVisibility(View.GONE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				String path = PathUtils.getPathByUriFromFile(uri, ApproveActivity.this);
				compressImage(path);
			}
		}
		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				String path = MyAppStateManager.getCameraTempFile(ApproveActivity.this);
				compressImage(path);
			}
		}
	}

	private void compressImage(String path) {
		if (!ImageUtils.isCorrectSize(path)) {
			Toast.makeText(this,
					"您选择的图片过小，请大于" + Constants.COMPRESS_IMAGE_MIN_WIDTH + "x" + Constants.COMPRESS_IMAGE_MIN_HEIGHT,
					Toast.LENGTH_LONG).show();
			return;
		}
		File file = new File(PathUtils.getCarmerDir(), this.mImageName);
		ImageUtils.compressImage(path, file.getAbsolutePath(), 1024);
		idCardInHandFilePath = file.getAbsolutePath();
		idCardInHand.setImageURI(Uri.fromFile(file));
	}

}
