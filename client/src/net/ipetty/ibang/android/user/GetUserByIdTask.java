package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.cache.Caches;
import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * GetUserByIdTask
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class GetUserByIdTask extends Task<Integer, UserVO> {

	private String TAG = getClass().getSimpleName();

	public GetUserByIdTask(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(Integer... args) {
		Log.d(TAG, "get user by id");

		Integer id = args[0];
		UserVO user = Caches.get(UserVO.class, id);
		if (user != null) {
			return user;
		}

		user = IbangApi.init(activity).getUserApi().getById(id);
		Caches.cache(user.getId(), user);
		return user;
	}

}
