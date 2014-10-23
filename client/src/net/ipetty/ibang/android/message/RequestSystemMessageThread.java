package net.ipetty.ibang.android.message;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.SystemMessageApi;
import android.content.Context;
import android.content.Intent;

/**
 * 定时读取系统消息的线程
 * 
 * @author luocanfeng
 * @date 2014年10月23日
 */
public class RequestSystemMessageThread implements Runnable {

	private static final int interval = 60 * 1000; // 定时间隔时间为60秒，单位毫秒

	private Context context;
	private Integer userId;

	private static RequestSystemMessageThread instance = null;
	private static Thread thread = null;

	public static void start(Context context, Integer userId) {
		if (instance == null) {
			instance = new RequestSystemMessageThread(context, userId);
			thread = new Thread(instance);
			thread.start();
		}
	}

	public static void stop() {
		thread.interrupt();
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
		while (true) {
			try {
				if (context != null && userId != null) {
					int num = IbangApi.init(context).create(SystemMessageApi.class).getUnreadNumberByUserId(userId);
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