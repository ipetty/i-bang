package net.ipetty.ibang.api.util;

import net.ipetty.ibang.util.UUIDUtils;

/**
 * 应用容器，单例，线程安全，存放API范围内公共变量
 * 
 * @author xiaojinghai
 */
public class ApiContext {

	// FIXME 这里测试时使用简单的字符串作为用户上下文，实际Android客户端中应该将用户上下文存储到本地系统中
	public static String USER_CONTEXT;
	public static String REFRESH_TOKEN;
	public static String DEVICE_UUID = UUIDUtils.generateShortUUID();
	public static String DEVICE_ID = UUIDUtils.generateShortUUID();
	public static String DEVICE_MAC = UUIDUtils.generateShortUUID();

	private Boolean authorized;

	private Integer currUserId;

	// 文件服务器地址
	public static final String FILE_SERVER_BASE = "http://localhost:8080";

	// API服务器地址
	public static final String API_SERVER_BASE = "http://localhost:8080/api";

	private static ApiContext instance;

	private ApiContext() {
	}

	public static synchronized ApiContext getInstance() {
		if (instance == null) {
			instance = new ApiContext();
		}
		return instance;
	}

	public synchronized Boolean isAuthorized() {
		return authorized;
	}

	public synchronized void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}

	public synchronized Integer getCurrUserId() {
		return currUserId;
	}

	public synchronized void setCurrUserId(Integer currUserId) {
		this.currUserId = currUserId;
	}

}
