package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetDelegationByIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class GetDelegationByIdTask extends Task<Long, DelegationVO> {

	private String TAG = getClass().getSimpleName();

	public GetDelegationByIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected DelegationVO myDoInBackground(Long... args) {
		Log.d(TAG, "get delegation");
		return IbangApi.init(activity).create(DelegationApi.class).getById(args[0]);
	}

}
