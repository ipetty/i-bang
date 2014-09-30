package net.ipetty.ibang.api.context;

import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

/**
 * 应用容器，单例，线程安全，存放API范围内公共变量
 * 
 * @author xiaojinghai
 */
public class ApiContext {

	private static String DEVICE_UUID = UUIDUtils.generateShortUUID(); // 设备UUID
	private static String USER_TOKEN;
	private static String REFRESH_TOKEN;
	private static Boolean AUTHORIZED; // 是否已登录
	private static Integer CURRENT_USER_ID; // 当前用户Id
	private static UserVO CURRENT_USER; // 当前用户VO
	private static String PLATFORM_NAME; // 第三方帐号登录时的第三方平台名称

	private static ApiContext instance;

	private ApiContext() {
		getDeviceUuid();
		if (StringUtils.isNotBlank(DEVICE_UUID)) {
			// TODO 如果没有找到device uuid信息，则从设备中获取device uuid等信息
		}
		getUserToken();
		getRefreshToken();
		isAuthorized();
		getCurrentUserId();
		getCurrentUser();
		getPlatformName();

	}

	public static ApiContext getInstance() {
		if (instance == null) {
			instance = new ApiContext();
		}
		return instance;
	}

	/**
	 * 获取设备UUID
	 */
	public synchronized String getDeviceUuid() {
		return DEVICE_UUID;
	}

	/**
	 * 设置设备UUID
	 */
	public synchronized void setDeviceUuid(String deviceUuid) {
		DEVICE_UUID = deviceUuid;
	}

	/**
	 * 获取user_token
	 */
	public synchronized String getUserToken() {
		return USER_TOKEN;
	}

	/**
	 * 设置user_token
	 */
	public synchronized void setUserToken(String userToken) {
		USER_TOKEN = userToken;
	}

	/**
	 * 获取refresh_token
	 */
	public synchronized String getRefreshToken() {
		return REFRESH_TOKEN;
	}

	/**
	 * 设置refresh_token
	 */
	public synchronized void setRefreshToken(String refreshToken) {
		REFRESH_TOKEN = refreshToken;
	}

	/**
	 * 是否已登录
	 */
	public synchronized Boolean isAuthorized() {
		return AUTHORIZED;
	}

	/**
	 * 设置登录状态
	 */
	public synchronized void setAuthorized(boolean authorized) {
		AUTHORIZED = authorized;
	}

	/**
	 * 获取当前用户ID
	 */
	public synchronized Integer getCurrentUserId() {
		return CURRENT_USER_ID;
	}

	/**
	 * 设置当前用户ID
	 */
	public synchronized void setCurrentUserId(Integer currentUserId) {
		CURRENT_USER_ID = currentUserId;
	}

	/**
	 * 获取当前用户
	 */
	public synchronized UserVO getCurrentUser() {
		return CURRENT_USER;
	}

	/**
	 * 设置当前用户
	 */
	public synchronized void setCurrentUser(UserVO currentUser) {
		CURRENT_USER = currentUser;
	}

	/**
	 * 获取第三方帐号登录时的第三方平台名称
	 */
	public synchronized String getPlatformName() {
		return PLATFORM_NAME;
	}

	/**
	 * 设置第三方帐号登录时的第三方平台名称
	 */
	public synchronized void setPlatformName(String platformName) {
		PLATFORM_NAME = platformName;
	}

}
