package net.ipetty.ibang.cache;

/**
 * CacheConstants
 * 
 * @author luocanfeng
 * @date 2014年5月20日
 */
public interface CacheConstants {

	/* user token */
	public static final String CACHE_USER_TOKEN_TO_USER_ID = "mapUserToken2UserId";

	/* user refresh token */
	public static final String CACHE_USER_REFRESH_TOKEN = "mapUserRefreshToken";

	/* user */
	public static final String CACHE_USER_ID_TO_USER = "mapUserId2User";
	public static final String CACHE_USERNAME_TO_USER_ID = "mapUsername2UserId";
	public static final String CACHE_EMAIL_TO_USER_ID = "mapEmail2UserId";
	public static final String CACHE_LOGIN_NAME_TO_USER_ID = "mapLoginName2UserId";

	/* image */
	public static final String CACHE_IMAGE_ID_TO_IMAGE = "mapImageId2Image";

}
