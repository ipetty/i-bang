package net.ipetty.ibang.vo;

/**
 * CrashLogVO
 * 
 * @author luocanfeng
 * @date 2014年9月29日
 */
public class CrashLogVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 5457667854209291290L;

	private Integer userId; // 用户ID
	private String userName; // 用户昵称
	private String androidVersion; // Android系统版本
	private Integer appVersionCode; // 应用版本
	private String appVersionName; // 应用版本
	private String crashType; // 日志类型，crash/error
	private String log; // 日志内容

	public CrashLogVO() {
		super();
	}

	public CrashLogVO(String log) {
		super();
		this.log = log;
	}

	public CrashLogVO(Integer userId, String userName, String androidVersion, Integer appVersionCode,
			String appVersionName, String crashType, String log) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.androidVersion = androidVersion;
		this.appVersionCode = appVersionCode;
		this.appVersionName = appVersionName;
		this.crashType = crashType;
		this.log = log;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public Integer getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(Integer appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getCrashType() {
		return crashType;
	}

	public void setCrashType(String crashType) {
		this.crashType = crashType;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
