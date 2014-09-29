package net.ipetty.ibang.vo;

/**
 * LoginResultVO
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class LoginResultVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -3831822372451256408L;

	private UserVO userVo;
	private String userToken;
	private String refreshToken;

	public LoginResultVO() {
		super();
	}

	public LoginResultVO(UserVO userVo, String userToken, String refreshToken) {
		super();
		this.userVo = userVo;
		this.userToken = userToken;
		this.refreshToken = refreshToken;
	}

	public UserVO getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVO userVo) {
		this.userVo = userVo;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
