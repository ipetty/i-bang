package net.ipetty.ibang.android.letter;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.letter.LetterActivity.LetterAdapter;
import net.ipetty.ibang.vo.LetterVO;
import android.app.Activity;
import android.util.Log;

/**
 * SendLetterTaskListener
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class SendLetterTaskListener extends DefaultTaskListener<LetterVO> {

	private String TAG = getClass().getSimpleName();

	private final LetterAdapter adapter;

	public SendLetterTaskListener(Activity activity, LetterAdapter adapter) {
		super(activity);
		this.adapter = adapter;
	}

	@Override
	public void onSuccess(LetterVO letter) {
		Log.d(TAG, "send letter success");

		List<LetterVO> letters = new ArrayList<LetterVO>();
		letters.add(letter);
		adapter.addData(letters);
	}

}
