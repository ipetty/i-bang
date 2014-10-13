package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.vo.UserVO;
import retrofit.mime.TypedFile;
import android.app.Activity;
import android.util.Log;

/**
 * UpdateAvatarTask
 * 
 * @author luocanfeng
 * @date 2014年10月11日
 */
public class UpdateAvatarTask extends Task<String, UserVO> {

	private String TAG = getClass().getSimpleName();

	public UpdateAvatarTask(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(String... args) {
		Log.d(TAG, "update avatar");
		String imagePath = args[0];
		TypedFile typedFile = FileUtils.typedFile(imagePath);
		return IbangApi.init(activity).getUserApi().updateAvatar(typedFile);
	}

}
