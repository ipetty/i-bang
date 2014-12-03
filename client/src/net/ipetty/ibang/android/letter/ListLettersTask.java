package net.ipetty.ibang.android.letter;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.LetterApi;
import net.ipetty.ibang.vo.LetterVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListLettersTask
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class ListLettersTask extends Task<Integer, List<LetterVO>> {

	private String TAG = getClass().getSimpleName();

	public ListLettersTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<LetterVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list letters");

		return IbangApi.init(activity).create(LetterApi.class).list(args[0], args[1], args[2]);
	}

}
