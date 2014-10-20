package net.ipetty.ibang.android.publish;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.SeekVO;

import org.springframework.util.CollectionUtils;

import retrofit.mime.TypedFile;
import android.app.Activity;
import android.util.Log;

/**
 * PublishSeekTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class PublishSeekTask extends Task<SeekForm, SeekVO> {

	private String TAG = getClass().getSimpleName();

	public PublishSeekTask(Activity activity) {
		super(activity);
	}

	@Override
	protected SeekVO myDoInBackground(SeekForm... args) {
		Log.d(TAG, "publish seek");

		SeekForm seekForm = args[0];
		SeekVO seek = seekForm.getSeek();
		List<String> imagePaths = seekForm.getImages();

		List<ImageVO> images = new ArrayList<ImageVO>();
		if (!CollectionUtils.isEmpty(imagePaths)) {
			for (String imagePath : imagePaths) {
				TypedFile typedFile = FileUtils.typedFile(imagePath);
				images.add(IbangApi.init(activity).create(ImageApi.class).upload(typedFile));
			}
		}

		seek.setImages(images);
		return IbangApi.init(activity).create(SeekApi.class).publish(seek);
	}

}
