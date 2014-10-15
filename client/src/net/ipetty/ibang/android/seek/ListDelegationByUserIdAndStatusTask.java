package net.ipetty.ibang.android.seek;

import java.util.List;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * ListDelegationByUserIdAndStatusTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class ListDelegationByUserIdAndStatusTask extends Task<String, List<DelegationVO>> {

	private String TAG = getClass().getSimpleName();

	public ListDelegationByUserIdAndStatusTask(Activity activity) {
		super(activity);
	}

	@Override
	protected List<DelegationVO> myDoInBackground(String... args) {
		Log.d(TAG, "list delegations by user id and status");
		return IbangApi
				.init(activity)
				.create(DelegationApi.class)
				.listByUserIdAndStatus(Integer.valueOf(args[0]), args[1], Integer.valueOf(args[2]),
						Integer.valueOf(args[3]));
	}

}
