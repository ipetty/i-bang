package net.ipetty.ibang.android.publish;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.vo.ImageVO;
import retrofit.mime.TypedFile;
import android.app.Activity;
import android.util.Log;

/**
 * UploadImageTask
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class UploadImagesTask extends Task<String, List<ImageVO>> {

	private String TAG = getClass().getSimpleName();

	public UploadImagesTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<ImageVO> myDoInBackground(String... args) {
		Log.d(TAG, "upload images");

		List<ImageVO> images = new ArrayList<ImageVO>();
		for (String imagePath : args) {
			TypedFile typedFile = FileUtils.typedFile(imagePath);
			images.add(IbangApi.init(activity).create(ImageApi.class).upload(typedFile));
		}
		return images;
	}

}
