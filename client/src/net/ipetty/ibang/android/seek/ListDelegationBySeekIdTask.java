package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListDelegationBySeekIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class ListDelegationBySeekIdTask extends Task<Long, List<DelegationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListDelegationBySeekIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<DelegationVO> myDoInBackground(Long... args) {
		Log.d(TAG, "list delegations by seek id");
		return IbangApi.init(activity).create(DelegationApi.class).listBySeekId(args[0]);
	}

}
