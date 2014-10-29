package net.ipetty.ibang.android.core;

public class Constants {

	// 文件服务器地址
	// public static final String FILE_SERVER_BASE = "http://ibang.ipetty.net";
	public static final String FILE_SERVER_BASE = "http://172.16.80.132:8080";

	// API服务器地址
	// public static final String API_SERVER_BASE =
	// "http://ibang.ipetty.net/api";
	public static final String API_SERVER_BASE = "http://172.16.80.132:8080/api";

	public static final int MAX_DELEGATION = 999; // 不限制委托默认最大数
	public static final int MAX_DELEGATION_CONDITION = 0; // 不限制委托的条件
	public static final String MAX_EXIPIREDATE = "2037-12-31"; // 不限制时间默认最大数
	public static final String MAX_EXIPIREDATE_CONDITION = "不限"; // 不限制时间的条件

	public static final String BROADCAST_INTENT_IS_LOGIN = "BROADCAST_INTENT_IS_LOGIN";
	public static final String BROADCAST_INTENT_UPDATA_USER = "BROADCAST_INTENT_UPDATA_USER";
	public static final String BROADCAST_INTENT_NEW_MESSAGE = "BROADCAST_INTENT_NEW_MESSAGE";
	public static final String BROADCAST_INTENT_PUBLISH_SEEK = "BROADCAST_INTENT_PUBLISH_SEEK";
	public static final String BROADCAST_INTENT_PUBLISH_OFFER = "BROADCAST_INTENT_PUBLISH_OFFER";
	public static final String BROADCAST_INTENT_EVALUATE = "BROADCAST_INTENT_EVALUATE";

	public final static String INTENT_IMAGE_ORIGINAL_KEY = "ORIGINAL_url";
	public final static String INTENT_IMAGE_SAMILL_KEY = "SAMILL_url";

	public static final String INTENT_CATEGORY = "INTENT_CATEGORY";
	public static final String INTENT_SUB_CATEGORY = "INTENT_SUB_CATEGORY";
	public static final String INTENT_IMAGE_UPLOAD_PATH = "INTENT_IMAGE_UPLOAD_PATH";
	public static final String INTENT_SEEK_ID = "INTENT_SEEK_ID";
	public static final String INTENT_SEEK_JSON = "INTENT_SEEK_JSON";
	public static final String INTENT_USER_ID = "INTENT_USER_ID";
	public static final String INTENT_USER_JSON = "INTENT_USER_JSON";
	public static final String INTENT_OFFER_ID = "INTENT_OFFER_ID";
	public static final String INTENT_OFFER_JSON = "INTENT_OFFER_JSON";
	public static final String INTENT_DELEGATION_ID = "INTENT_DELEGATION_ID";
	public static final String INTENT_EVALUATOR_TYPE = "INTENT_EVALUATOR_TYPE";
	public static final String INTENT_EVALUATOR_ID = "INTENT_EVALUATOR_ID";
	public static final String INTENT_EVALUATE_TARGET_ID = "INTENT_EVALUATE_TARGET_ID";

	public static final String INTENT_LOCATION_PROVINCE = "INTENT_LOCATION_PROVINCE";
	public static final String INTENT_LOCATION_CITY = "INTENT_LOCATION_CITY";
	public static final String INTENT_LOCATION_DISTRICT = "INTENT_LOCATION_DISTRICT";
	public static final String INTENT_LOCATION_TYPE = "INTENT_LOCATION_TYPE";

	public final static float COMPRESS_IMAGE_MAX_WIDTH = 960f;
	public final static float COMPRESS_IMAGE_MAX_HEIGHT = 1280f;
	public final static float COMPRESS_IMAGE_MIN_WIDTH = 64f;
	public final static float COMPRESS_IMAGE_MIN_HEIGHT = 64f;
	public final static int COMPRESS_IMAGE_KB = 100;

	public final static float ZOOM_IMAGE_MAX_WIDTH = 320f;
	public final static float ZOOM_IMAGE_MAX_HEIGHT = 320f;

	public static final String INTENT_USER_EDIT_TYPE = "INTENT_USER_EDIT_TYPE";
	public static final String INTENT_USER_EDIT_TYPE_NICKNAME = "nickname";
	public static final String INTENT_USER_EDIT_TYPE_PHONE = "phone";
	public static final String INTENT_USER_EDIT_TYPE_JOB = "job";
	public static final String INTENT_USER_EDIT_TYPE_SIGNATURE = "signature";

	public static final int REQUEST_CODE_USER_EDIT = 10;
	public static final int REQUEST_CODE_CITY = 100;
	public static final int REQUEST_CODE_CATEGORY = 101;

	public static final String PIC_USER_HEAD_IMAGE_NAME = "cacheHead.jpg";

}
