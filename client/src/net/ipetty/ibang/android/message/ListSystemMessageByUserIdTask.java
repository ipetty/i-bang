package net.ipetty.ibang.android.message;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SystemMessageApi;
import net.ipetty.ibang.vo.SystemMessageVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListSystemMessageByUserIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月22日
 */
public class ListSystemMessageByUserIdTask extends Task<Integer, List<SystemMessageVO>> {

	private String TAG = getClass().getSimpleName();

	public ListSystemMessageByUserIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<SystemMessageVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list system messages by user id");
		return IbangApi.init(activity).create(SystemMessageApi.class).listByUserId(args[0], args[1], args[2]);
	}

}
