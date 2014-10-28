package net.ipetty.ibang.android.publish;

import android.app.Activity;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.ibang.android.baidu.BaiduApiFactory;
import net.ipetty.ibang.android.baidu.vo.RetVO;
import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.SeekVO;
import org.springframework.util.CollectionUtils;
import retrofit.mime.TypedFile;

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
                SeekVO seekVO = null;//IbangApi.init(activity).create(SeekApi.class).publish(seek);

                BaiduApiFactory.init(activity);
                //createPoiVo.setAk("xIGFxDC9omkW6OoaHakuDKWU");
                String ak = "8691f5dfa58c9acccf7bb4c72e529382";
                String geotable_id = "82284";
                Integer coord_type = 3;
                Double longitude = 121.39842;
                Double latitude = 121.39842;
                String title = "";
                String tags = "";
                String bid = "0000";

                RetVO ret = BaiduApiFactory.getBaiduApi().createPoi(ak, geotable_id, coord_type, longitude, latitude,
                        title, tags, bid);
                Log.d(TAG, ret.getMessage() + "," + ret.getStatus() + "," + ret.getId());

                //BaiduApiFactory.getBaiduApi().createPoi(createPoiVo);
                return seekVO;
        }

}
