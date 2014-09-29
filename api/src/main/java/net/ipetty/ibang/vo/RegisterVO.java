package net.ipetty.ibang.vo;

/**
 * 用户注册VO
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class RegisterVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 6928184521593127759L;

	private String username; // 用户名
	private String password; // 密码
	private String nickname; // 昵称
	private String phone; // 手机号码

	public RegisterVO() {
		super();
	}

	public RegisterVO(String username, String password, String nickname, String phone) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
