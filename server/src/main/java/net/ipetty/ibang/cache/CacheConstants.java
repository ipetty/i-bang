package net.ipetty.ibang.cache;

/**
 * CacheConstants
 * 
 * @author luocanfeng
 * @date 2014年5月20日
 */
public interface CacheConstants {

	/* user token */
	public static final String CACHE_USER_TOKEN_TO_USER_ID = "cacheUserToken2UserId";

	/* user refresh token */
	public static final String CACHE_USER_REFRESH_TOKEN = "cacheUserRefreshToken";

	/* user */
	public static final String CACHE_USER_ID_TO_USER = "cacheUserId2User";
	public static final String CACHE_USERNAME_TO_USER_ID = "cacheUsername2UserId";
	public static final String CACHE_EMAIL_TO_USER_ID = "cacheEmail2UserId";
	public static final String CACHE_LOGIN_NAME_TO_USER_ID = "cacheLoginName2UserId";
	public static final String CACHE_USER_ID_TO_SEEKER_INFO = "cacheUserId2SeekerInfo";
	public static final String CACHE_USER_ID_TO_OFFERER_INFO = "cacheUserId2OffererInfo";
	public static final String CACHE_USER_ID_TO_OFFER_RANGE = "cacheUserId2OfferRange";

	/* location */
	public static final String CACHE_LOCATION_ID_TO_LOCATION = "cacheLocationId2Location";

	/* seek */
	public static final String CACHE_SEEK_ID_TO_SEEK = "cacheSeekId2Seek";
	/* offer */
	public static final String CACHE_OFFER_ID_TO_OFFER = "cacheOfferId2Offer";
	/* delegation */
	public static final String CACHE_DELEGATION_ID_TO_DELEGATION = "cacheDelegationId2Delegation";
	/* evaluation */
	public static final String CACHE_EVALUATION_ID_TO_EVALUATION = "cacheEvaluationId2Evaluation";

	/* image */
	public static final String CACHE_IMAGE_ID_TO_IMAGE = "cacheImageId2Image";
	public static final String CACHE_SEEK_ID_TO_IMAGE_IDS = "cacheSeekId2ImageIds";

	/* system message */
	public static final String CACHE_SYS_MSG_ID_TO_SYS_MSG = "cacheSysMsgId2SysMsg";

}
