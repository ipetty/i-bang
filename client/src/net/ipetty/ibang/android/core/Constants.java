package net.ipetty.ibang.android.core;

public class Constants {

	// 文件服务器地址
	// public static final String FILE_SERVER_BASE = "http://api.ipetty.net";
	public static final String FILE_SERVER_BASE = "http://192.168.253.1:8080";

	// API服务器地址
	// public static final String API_SERVER_BASE = "http://api.ipetty.net/api";
	public static final String API_SERVER_BASE = "http://192.168.253.1:8080/api";

	public static final String BROADCAST_INTENT_IS_LOGIN = "BROADCAST_INTENT_IS_LOGIN";
	public static final String BROADCAST_INTENT_UPDATA_USER = "BROADCAST_INTENT_UPDATA_USER";
	public static final String BROADCAST_INTENT_NEW_MESSAGE = "BROADCAST_INTENT_NEW_MESSAGE";

	public static final String INTENT_CATEGORY = "INTENT_CATEGORY";
	public static final String INTENT_SUB_CATEGORY = "INTENT_SUB_CATEGORY";
	public static final String INTENT_IMAGE_UPLOAD_PATH = "INTENT_IMAGE_UPLOAD_PATH";
	public static final String INTENT_SEEK_ID = "INTENT_SEEK_ID";

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
