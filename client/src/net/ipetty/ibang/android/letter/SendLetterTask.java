package net.ipetty.ibang.android.letter;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.LetterApi;
import net.ipetty.ibang.vo.LetterVO;
import android.app.Activity;
import android.util.Log;

/**
 * SendLetterTask
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class SendLetterTask extends Task<String, LetterVO> {

	private String TAG = getClass().getSimpleName();

	public SendLetterTask(Activity activity) {
		super(activity);
	}

	@Override
	protected LetterVO myDoInBackground(String... args) {
		Log.d(TAG, "send letter");

		String toUserId = args[0];
		String content = args[1];

		return IbangApi.init(activity).create(LetterApi.class).send(Integer.valueOf(toUserId), content);
	}

}
