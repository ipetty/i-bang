package net.ipetty.ibang.android.approve;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.IdentityVerificationApi;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import net.ipetty.ibang.vo.ImageVO;
import retrofit.mime.TypedFile;
import android.app.Activity;
import android.util.Log;

/**
 * SubmitIdentityVerificationTask
 * @author luocanfeng
 * @date 2014年11月17日
 */
public class SubmitIdentityVerificationTask extends Task<IdentityVerificationVO, Boolean> {

	private String TAG = getClass().getSimpleName();

	public SubmitIdentityVerificationTask(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(IdentityVerificationVO... args) {
		Log.d(TAG, "submit identity verification");

		IdentityVerificationVO identityVerification = args[0];
		String idCardInHandFilePath = identityVerification.getIdCardInHand();

		// 保存图片
		TypedFile idCardInHandTypedFile = FileUtils.typedFile(idCardInHandFilePath);
		ImageVO idCardInHandImage = IbangApi.init(activity).create(ImageApi.class).upload(idCardInHandTypedFile);
		identityVerification.setIdCardInHand(idCardInHandImage.getOriginalUrl());

		return IbangApi.init(activity).create(IdentityVerificationApi.class).submit(args[0]);
	}

}
