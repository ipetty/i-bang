package net.ipetty.ibang.android.user;

import java.util.concurrent.CountDownLatch;

import net.ipetty.ibang.android.cache.Caches;
import net.ipetty.ibang.android.sdk.exception.ApiException;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.vo.UserVO;
import android.content.Context;
import android.util.Log;

/**
 * GetUserByIdSynchronous
 * @author luocanfeng
 * @date 2014年10月14日
 */
public class GetUserByIdSynchronously {

	private static final String TAG = GetUserByIdSynchronously.class.getSimpleName();
	private static UserVO syncUser;

	public static UserVO get(final Context context, final Integer id) {
		Log.d(TAG, "get user by id synchronously, id=" + id);
		UserVO user = Caches.get(UserVO.class, id);
		if (user != null) {
			return user;
		}

		final CountDownLatch latch = new CountDownLatch(1);
		new Thread(new Runnable() {

			@Override
			public void run() {
				syncUser = IbangApi.init(context).getUserApi().getById(id);
				latch.countDown();
			}
		}).start();

		try {
			latch.await();
		} catch (InterruptedException ex) {
			throw new ApiException(ex);
		}

		Caches.cache(syncUser.getId(), syncUser);
		return syncUser;
	}

}
