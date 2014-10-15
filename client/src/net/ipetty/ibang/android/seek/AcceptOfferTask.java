package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * AcceptOfferTask
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public class AcceptOfferTask extends Task<DelegationVO, DelegationVO> {

	private String TAG = getClass().getSimpleName();

	public AcceptOfferTask(Activity activity) {
		super(activity);
	}

	@Override
	protected DelegationVO myDoInBackground(DelegationVO... args) {
		Log.d(TAG, "accept offer");
		return IbangApi.init(activity).create(DelegationApi.class).delegate(args[0]);
	}

}
