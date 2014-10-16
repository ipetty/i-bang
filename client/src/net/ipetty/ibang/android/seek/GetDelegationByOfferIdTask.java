package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.DelegationApi;
import net.ipetty.ibang.vo.DelegationVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetDelegationByOfferIdTask
 * 
 * @author luocanfeng
 * @date 2014年10月16日
 */
public class GetDelegationByOfferIdTask extends Task<Long, DelegationVO> {

	private String TAG = getClass().getSimpleName();

	public GetDelegationByOfferIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected DelegationVO myDoInBackground(Long... args) {
		Log.d(TAG, "get delegation by offer id");
		return IbangApi.init(activity).create(DelegationApi.class).getByOfferId(args[0]);
	}

}
