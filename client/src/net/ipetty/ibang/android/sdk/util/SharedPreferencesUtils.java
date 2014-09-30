package net.ipetty.ibang.android.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferencesUtils
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class SharedPreferencesUtils {

	private static final String TAG = SharedPreferencesUtils.class.getSimpleName();

	public static String getString(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}

	public static void setString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, false);
	}

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);
	}

	public static void setInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

}
