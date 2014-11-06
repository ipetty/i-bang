package net.ipetty.ibang.android.publish;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.api.SeekApi;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.SeekWithLocationVO;
import retrofit.mime.TypedFile;
import android.app.Activity;
import android.util.Log;

/**
 * PublishSeekTask
 * 
 * @author luocanfeng
 * @date 2014年10月13日
 */
public class PublishSeekTask extends Task<SeekWithLocationForm, SeekVO> {

	private String TAG = getClass().getSimpleName();

	public PublishSeekTask(Activity activity) {
		super(activity);
	}

	@Override
	protected SeekVO myDoInBackground(SeekWithLocationForm... args) {
		Log.d(TAG, "publish seek");

		SeekWithLocationForm seekForm = args[0];
		SeekWithLocationVO seek = seekForm.getSeek();

		List<String> imagePaths = seekForm.getImages();
		List<ImageVO> images = new ArrayList<ImageVO>();
		for (String imagePath : imagePaths) {
			TypedFile typedFile = FileUtils.typedFile(imagePath);
			images.add(IbangApi.init(activity).create(ImageApi.class).upload(typedFile));
		}
		seek.setImages(images);

		if (Constants.SEEK_TYPE_ASSISTANCE.equals(seek.getType())) {
			return IbangApi.init(activity).create(SeekApi.class).publishAssistance(seek);
		} else {
			return IbangApi.init(activity).create(SeekApi.class).publish(seek);
		}
	}

}
