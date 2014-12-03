package net.ipetty.ibang.android.letter;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.LetterApi;
import net.ipetty.ibang.vo.LetterContacts;
import android.app.Activity;
import android.util.Log;

/**
 * ListContactsTask
 * @author luocanfeng
 * @date 2014年12月2日
 */
public class ListContactsTask extends Task<Integer, List<LetterContacts>> {

	private String TAG = getClass().getSimpleName();

	public ListContactsTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<LetterContacts> myDoInBackground(Integer... args) {
		Log.d(TAG, "list contacts");

		return IbangApi.init(activity).create(LetterApi.class).listContacts(args[0], args[1]);
	}

}
