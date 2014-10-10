package net.ipetty.ibang.android.sdk.context;

import net.ipetty.ibang.android.core.util.DeviceUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.sdk.util.SharedPreferencesUtils;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.util.Log;

/**
 * 应用容器，单例，线程安全，存放API范围内公共变量
 * 
 * @author xiaojinghai
 */
public class ApiContext {

	private static String TAG = ApiContext.class.getSimpleName();

	private static String DEVICE_UUID; // 设备UUID
	private static String USER_TOKEN;
	private static String REFRESH_TOKEN;
	private static Boolean AUTHORIZED; // 是否已登录
	private static Integer CURRENT_USER_ID; // 当前用户Id
	private static UserVO CURRENT_USER; // 当前用户VO
	private static String PLATFORM_NAME; // 第三方帐号登录时的第三方平台名称

	private static final String SP_DEVICE_UUID = "sp_device_uuid";
	private static final String SP_USER_TOKEN = "sp_user_token";
	private static final String SP_REFRESH_TOKEN = "sp_refresh_token";
	private static final String SP_AUTHORIZED = "sp_authorized";
	private static final String SP_CURRENT_USER_ID = "sp_current_user_id";
	private static final String SP_CURRENT_USER = "sp_current_user";
	private static final String SP_PLATFORM_NAME = "sp_platform_name";

	private static Context context;
	private static ApiContext instance;

	private ApiContext(Context ctx) {
		context = ctx;

		getDeviceUuid();
		if (StringUtils.isBlank(DEVICE_UUID)) {
			// 如果没有找到device uuid信息，则从设备中获取device uuid等信息
			DEVICE_UUID = DeviceUtils.getDeviceUUID(context).toString();
		}
		if (StringUtils.isBlank(DEVICE_UUID)) { // 此处不应该为空，此处代码为容错代码
			DEVICE_UUID = UUIDUtils.generateShortUUID();
		}
		Log.d(TAG, "device uuid is " + DEVICE_UUID);

		getUserToken();
		getRefreshToken();
		isAuthorized();
		getCurrentUserId();
		getCurrentUser();
		getPlatformName();

	}

	public static ApiContext getInstance(Context context) {
		if (instance == null) {
			instance = new ApiContext(context);
		}
		return instance;
	}

	/**
	 * 获取设备UUID
	 */
	public synchronized String getDeviceUuid() {
		if (StringUtils.isBlank(DEVICE_UUID)) {
			DEVICE_UUID = SharedPreferencesUtils.getString(context, SP_DEVICE_UUID);
		}
		return DEVICE_UUID;
	}

	/**
	 * 设置设备UUID
	 */
	public synchronized void setDeviceUuid(String deviceUuid) {
		DEVICE_UUID = deviceUuid;
		SharedPreferencesUtils.setString(context, SP_DEVICE_UUID, deviceUuid);
	}

	/**
	 * 获取user_token
	 */
	public synchronized String getUserToken() {
		if (StringUtils.isBlank(USER_TOKEN)) {
			USER_TOKEN = SharedPreferencesUtils.getString(context, SP_USER_TOKEN);
		}
		return USER_TOKEN;
	}

	/**
	 * 设置user_token
	 */
	public synchronized void setUserToken(String userToken) {
		USER_TOKEN = userToken;
		SharedPreferencesUtils.setString(context, SP_USER_TOKEN, userToken);
		Log.d(TAG, "set user token " + userToken);
	}

	/**
	 * 获取refresh_token
	 */
	public synchronized String getRefreshToken() {
		if (StringUtils.isBlank(REFRESH_TOKEN)) {
			REFRESH_TOKEN = SharedPreferencesUtils.getString(context, SP_REFRESH_TOKEN);
		}
		return REFRESH_TOKEN;
	}

	/**
	 * 设置refresh_token
	 */
	public synchronized void setRefreshToken(String refreshToken) {
		REFRESH_TOKEN = refreshToken;
		SharedPreferencesUtils.setString(context, SP_REFRESH_TOKEN, refreshToken);
		Log.d(TAG, "set refresh token " + refreshToken);
	}

	/**
	 * 是否已登录
	 */
	public synchronized Boolean isAuthorized() {
		if (AUTHORIZED == null) {
			AUTHORIZED = SharedPreferencesUtils.getBoolean(context, SP_AUTHORIZED);
		}
		return AUTHORIZED;
	}

	/**
	 * 设置登录状态
	 */
	public synchronized void setAuthorized(boolean authorized) {
		AUTHORIZED = authorized;
		SharedPreferencesUtils.setBoolean(context, SP_AUTHORIZED, authorized);
		Log.d(TAG, "set authorized " + authorized);
	}

	/**
	 * 获取当前用户ID
	 */
	public synchronized Integer getCurrentUserId() {
		if (CURRENT_USER_ID == null) {
			int userId = SharedPreferencesUtils.getInt(context, SP_CURRENT_USER_ID);
			if (userId != 0) {
				CURRENT_USER_ID = userId;
			}
		}
		return CURRENT_USER_ID;
	}

	/**
	 * 设置当前用户ID
	 */
	public synchronized void setCurrentUserId(Integer currentUserId) {
		CURRENT_USER_ID = currentUserId;
		if (currentUserId != null) {
			SharedPreferencesUtils.setInt(context, SP_CURRENT_USER_ID, currentUserId);
			Log.d(TAG, "set current user id " + currentUserId);
		}
	}

	/**
	 * 获取当前用户
	 */
	public synchronized UserVO getCurrentUser() {
		if (CURRENT_USER == null) {
			String currentUser = SharedPreferencesUtils.getString(context, SP_CURRENT_USER);
			if (StringUtils.isNotBlank(currentUser)) {
				CURRENT_USER = JSONUtils.fromJSON(currentUser, UserVO.class);
			}
		}
		return CURRENT_USER;
	}

	/**
	 * 设置当前用户
	 */
	public synchronized void setCurrentUser(UserVO currentUser) {
		CURRENT_USER = currentUser;
		SharedPreferencesUtils.setString(context, SP_CURRENT_USER, JSONUtils.toJson(currentUser));
		Log.d(TAG, "set current user " + currentUser);
	}

	/**
	 * 获取第三方帐号登录时的第三方平台名称
	 */
	public synchronized String getPlatformName() {
		if (PLATFORM_NAME == null) {
			PLATFORM_NAME = SharedPreferencesUtils.getString(context, SP_PLATFORM_NAME);
		}
		return PLATFORM_NAME;
	}

	/**
	 * 设置第三方帐号登录时的第三方平台名称
	 */
	public synchronized void setPlatformName(String platformName) {
		PLATFORM_NAME = platformName;
		SharedPreferencesUtils.setString(context, SP_PLATFORM_NAME, platformName);
	}

}
