package net.ipetty.ibang.android.approve;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.IdentityVerificationApi;
import net.ipetty.ibang.vo.IdentityVerificationVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetIdentityVerificationTask
 * @author luocanfeng
 * @date 2014年11月17日
 */
public class GetIdentityVerificationTask extends Task<Void, IdentityVerificationVO> {

	private String TAG = getClass().getSimpleName();

	public GetIdentityVerificationTask(Activity activity) {
		super(activity);
	}

	@Override
	protected IdentityVerificationVO myDoInBackground(Void... args) {
		Log.d(TAG, "get identity verification");
		return IbangApi.init(activity).create(IdentityVerificationApi.class).get();
	}

}
