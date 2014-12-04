package net.ipetty.ibang.android.user;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.UserApi;
import net.ipetty.ibang.vo.Unread;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 定时读取未读消息的线程
 * @author luocanfeng
 * @date 2014年12月4日
 */
public class GetUnreadThread implements Runnable {

	private String TAG = getClass().getSimpleName();

	private Context context;
	private int intervalInSeconds = 60; // 定时查询时间，默认为60秒

	private static GetUnreadThread instance = null;
	private static Thread thread = null;
	private static boolean flag = true;

	private GetUnreadThread(Context context, int intervalInSeconds) {
		this(context);
		this.intervalInSeconds = intervalInSeconds;
	}

	public static void start(Context context) {
		start(context, 60);
	}

	public static void start(Context context, int intervalInSeconds) {
		if (instance == null) {
			instance = new GetUnreadThread(context, intervalInSeconds);
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

	private GetUnreadThread(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void run() {
		UserApi userApi = IbangApi.init(context).create(UserApi.class);
		while (flag) {
			try {
				if (context != null) {
					Unread unread = userApi.getUnread();
					Log.d(TAG, "获取未读消息，未读系统消息数=" + unread.getMessageNum() + "，未读站内信数=" + unread.getLetterNum());
					if (unread.getMessageNum() > 0) {
						Intent intent = new Intent(Constants.BROADCAST_INTENT_NEW_MESSAGE);
						context.sendBroadcast(intent);
					}
					if (unread.getLetterNum() > 0) {
						Intent intent = new Intent(Constants.BROADCAST_INTENT_NEW_LETTER);
						context.sendBroadcast(intent);
					}
				}

				Thread.sleep(intervalInSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
