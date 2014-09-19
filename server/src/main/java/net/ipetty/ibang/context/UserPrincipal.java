package net.ipetty.ibang.context;

import net.ipetty.ibang.model.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 上下文中的用户主体
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
public class UserPrincipal {

	private Integer id;
	private String username; // 用户名
	private String email; // 邮箱
	private String nickname; // 昵称
	private String token; // 用户身份token标识，在用户登录后产生，并在服务器端缓存

	public UserPrincipal() {
	}

	public UserPrincipal(Integer id) {
		this.id = id;
	}

	public UserPrincipal(Integer id, String username, String email, String nickname) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.nickname = nickname;
	}

	public static UserPrincipal fromUser(User user, String userToken) {
		UserPrincipal principal = new UserPrincipal();
		principal.setId(user.getId());
		principal.setUsername(user.getUsername());
		principal.setEmail(user.getEmail());
		if (StringUtils.isNotEmpty(user.getNickname())) {
			principal.setNickname(user.getNickname());
		} else if (StringUtils.isNotEmpty(user.getUsername())) {
			principal.setNickname(user.getUsername());
		} else if (StringUtils.isNotEmpty(user.getEmail())) {
			principal.setNickname(user.getEmail());
		}
		principal.setToken(userToken);
		return principal;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
