package net.ipetty.ibang.android.message;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SystemMessageApi;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 定时读取系统消息的线程
 * 
 * @author luocanfeng
 * @date 2014年10月23日
 */
public class RequestSystemMessageThread implements Runnable {

	private String TAG = getClass().getSimpleName();

	private static final int interval = 300 * 1000; // 定时间隔时间为5分钟，单位毫秒

	private Context context;
	private Integer userId;

	private static RequestSystemMessageThread instance = null;
	private static Thread thread = null;
	private static boolean flag = true;

	public static void start(Context context, Integer userId) {
		if (instance == null) {
			instance = new RequestSystemMessageThread(context, userId);
			thread = new Thread(instance);
			flag = true;
			thread.start();
		}
	}

	public static void stop() {
		flag = false;
		// thread.interrupt();
		thread = null;
		instance = null;
	}

	private RequestSystemMessageThread(Context context, Integer userId) {
		super();
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void run() {
		SystemMessageApi systemMessageApi = IbangApi.init(context).create(SystemMessageApi.class);
		while (flag) {
			try {
				if (context != null && userId != null) {
					int num = systemMessageApi.getUnreadNumberByUserId(userId);
					Log.d(TAG, "获取通知消息，未读数=" + num);
					if (num > 0) {
						Intent intent = new Intent(Constants.BROADCAST_INTENT_NEW_MESSAGE);
						context.sendBroadcast(intent);
					}
				}

				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
