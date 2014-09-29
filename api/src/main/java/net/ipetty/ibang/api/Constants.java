package net.ipetty.ibang.api;

import java.nio.charset.Charset;

/**
 * Constants
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public interface Constants {

	public static final String HEADER_NAME_USER_TOKEN = "user_token";
	public static final String HEADER_NAME_REFRESH_TOKEN = "refresh_token";
	public static final String HEADER_NAME_DEVICE_ID = "device_id";
	public static final String HEADER_NAME_DEVICE_MAC = "device_mac";
	public static final String HEADER_NAME_DEVICE_UUID = "device_uuid";
	public static final Charset UTF8 = Charset.forName("UTF-8");

}
