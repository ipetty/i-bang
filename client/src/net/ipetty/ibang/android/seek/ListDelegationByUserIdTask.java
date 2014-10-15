package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListDelegationByUserIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class ListDelegationByUserIdTask extends Task<Integer, List<DelegationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListDelegationByUserIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<DelegationVO> myDoInBackground(Integer... args) {
		Log.d(TAG, "list delegations by user id");
		return IbangApi.init(activity).create(DelegationApi.class).listByUserId(args[0], args[1], args[2]);
	}

}
