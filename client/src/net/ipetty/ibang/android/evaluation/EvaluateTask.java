package net.ipetty.ibang.android.evaluation;

import android.app.Activity;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.android.sdk.util.FileUtils;
import net.ipetty.ibang.api.EvaluationApi;
import net.ipetty.ibang.api.ImageApi;
import net.ipetty.ibang.vo.EvaluationVO;
import net.ipetty.ibang.vo.ImageVO;
import retrofit.mime.TypedFile;

/**
 * EvaluateTask
 *
 * @author luocanfeng
 * @date 2014年10月17日
 */
public class EvaluateTask extends Task<EvaluationForm, EvaluationVO> {

    private String TAG = getClass().getSimpleName();

    public EvaluateTask(Activity activity) {
        super(activity);
    }

    @Override
    protected EvaluationVO myDoInBackground(EvaluationForm... args) {
        Log.d(TAG, "evaluate");

        EvaluationForm evaluationForm = args[0];
        EvaluationVO evaluation = evaluationForm.getEvaluation();
        List<String> imagePaths = evaluationForm.getImages();

        List<ImageVO> images = new ArrayList<ImageVO>();

        for (String imagePath : imagePaths) {
            TypedFile typedFile = FileUtils.typedFile(imagePath);
            images.add(IbangApi.init(activity).create(ImageApi.class).upload(typedFile));
        }

        evaluation.setImages(images);
        return IbangApi.init(activity).create(EvaluationApi.class).evaluate(evaluation);
    }

}
