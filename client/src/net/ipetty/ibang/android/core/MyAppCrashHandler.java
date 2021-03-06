/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.core.util.PathUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.CrashLogApi;
import net.ipetty.ibang.vo.CrashLogVO;
import net.ipetty.ibang.vo.UserVO;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyAppCrashHandler implements UncaughtExceptionHandler {

	private String TAG = getClass().getSimpleName();

	private final Thread.UncaughtExceptionHandler mDefaultHandler;
	private final Context context;

	private final Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private String crashInfo = "";

	public MyAppCrashHandler(Context context) {
		this.context = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG, ex.getMessage(), ex);
		String msg = "亲，出错了";
		showError(msg);
		collectDeviceInfo(thread, context);
		saveCrashInfo2File(ex);
		if (NetWorkUtils.isNetworkConnected(context)) {
			reportCrash();
		}
		waitFor(3 * 1000);
		Log.d(TAG, "waitFor3");
		ActivityManager.getInstance().exit();
		Log.d(TAG, "exit");
	}

	private void reportCrash() {
		Log.d(TAG, "reportCrash");
		try {
			final CrashLogVO crashVO = new CrashLogVO();
			crashVO.setUserId(ApiContext.getInstance(context).getCurrentUserId());
			UserVO user = ApiContext.getInstance(context).getCurrentUser();
			String nickName = user == null ? "" : user.getNickname();
			crashVO.setUserName(nickName);
			crashVO.setAndroidVersion(android.os.Build.VERSION.RELEASE);
			crashVO.setAppVersionCode(AppUtils.getAppVersionCode(context));
			crashVO.setAppVersionName(AppUtils.getAppVersionName(context));
			crashVO.setCrashType("crash");
			crashVO.setLog(crashInfo);
			new Thread() {
				@Override
				public void run() {
					IbangApi.init(context).create(CrashLogApi.class).save(crashVO);
				}
			}.start();
		} catch (Exception ex) {
			// 忽略异常
		}
	}

	// 另启线程进行等待，防止阻塞UI线程
	private void waitFor(long time) {
		Log.d(TAG, "waitFor");
		// 异步转同步
		final CountDownLatch latch = new CountDownLatch(1);
		final Long waitTime = time;
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException ex) {

				}
				latch.countDown();
			}
		}.start();
		try {
			latch.await();
		} catch (InterruptedException ex) {

		}
	}

	private void showError(final String msg) {
		// 非UI线程进行UI界面操作
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param thread
	 * @param context
	 */
	public void collectDeviceInfo(Thread thread, Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
				info.put("threadName", thread.getName());
				Integer uid = ApiContext.getInstance(context).getCurrentUserId();
				info.put("currUserId", uid.toString());
			}
		} catch (PackageManager.NameNotFoundException e) {

		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {

			} catch (IllegalAccessException e) {

			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append("=").append(value).append("\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		String result = writer.toString();
		sb.append(result);

		crashInfo = sb.toString();

		// 如果有外部存储则保存文件
		long timetamp = System.currentTimeMillis();
		String time = DateUtils.getCurrentDatetime();
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				String dir = PathUtils.getCrashDir();
				FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
				return fileName;
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			}
		}

		return null;
	}
}
