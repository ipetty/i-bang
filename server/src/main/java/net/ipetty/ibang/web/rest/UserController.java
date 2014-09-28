package net.ipetty.ibang.web.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.ipetty.ibang.api.Constants;
import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.Caches;
import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.model.UserRefreshToken;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
@Controller
public class UserController extends BaseController {

	@Resource
	private UserService userService;

	@Resource
	private HttpServletRequest request;

	/**
	 * 用户登陆验证
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public LoginResultVO login(String loginName, String password) {
		logger.debug("login with loginName={}", loginName);
		Assert.hasText(loginName, "登录名不能为空");
		Assert.hasText(password, "密码不能为空");
		User user = userService.login(loginName, password); // 未发生异常则已登录成功

		String deviceUuid = request.getHeader(Constants.HEADER_NAME_DEVICE_UUID);
		String deviceId = request.getHeader(Constants.HEADER_NAME_DEVICE_ID);
		String deviceMac = request.getHeader(Constants.HEADER_NAME_DEVICE_MAC);
		logger.debug("deviceUuid={}, deviceId={}, deviceMac={}", deviceUuid, deviceId, deviceMac);
		return loginPostProcess(user, deviceUuid, deviceId, deviceMac);
	}

	private LoginResultVO loginPostProcess(User user, String deviceUuid, String deviceId, String deviceMac) {
		// userToken
		String userToken = UUIDUtils.generateShortUUID();
		logger.debug("generate new user token {}", userToken);

		// 设置用户上下文
		UserPrincipal principal = UserPrincipal.fromUser(user);
		principal.setToken(userToken);
		UserContext.setContext(principal);

		// refreshToken
		UserRefreshToken userRefreshToken = null;
		String refreshToken = null;
		if (StringUtils.isNotBlank(deviceUuid)) {
			userRefreshToken = userService.getRefreshToken(user.getId(), deviceUuid);
		}
		if (userRefreshToken != null) {
			refreshToken = userRefreshToken.getRefreshToken();
		} else {
			refreshToken = UUIDUtils.generateShortUUID();
			userRefreshToken = new UserRefreshToken();
			userRefreshToken.setUserId(user.getId());
			userRefreshToken.setDeviceUuid(deviceUuid);
			userRefreshToken.setDeviceId(deviceId);
			userRefreshToken.setDeviceMac(deviceMac);
			userRefreshToken.setRefreshToken(refreshToken);
			userService.saveRefreshToken(userRefreshToken);
		}

		// 将userToken写入缓存
		Caches.set(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, principal.getToken(), principal.getId());

		return new LoginResultVO(user.toVO(), userToken, refreshToken);
	}

	/**
	 * 用户再次打开App时，使用本地存储的userToken 和refreshToken 登陆
	 */
	@RequestMapping(value = "/relogin", method = RequestMethod.POST)
	@ResponseBody
	public LoginResultVO relogin(String userToken, String refreshToken) {
		if (StringUtils.isNotBlank(userToken)) { // 根据userToken获取用户
			Integer userId = Caches.get(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, userToken);
			if (userId != null) {
				User user = userService.getById(userId);
				if (user != null) {
					return new LoginResultVO(user.toVO(), userToken, refreshToken);
				}
			}
		}

		UserRefreshToken userRefreshToken = userService.getRefreshToken(refreshToken);
		if (userRefreshToken != null) { // 根据refreshToken获取用户
			Integer userId = userRefreshToken.getUserId();
			if (userId != null) {
				User user = userService.getById(userId);
				if (user != null) {
					userToken = UUIDUtils.generateShortUUID();
					Caches.set(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, userToken, userId);
					return new LoginResultVO(user.toVO(), userToken, refreshToken);
				}
			}
		}

		// userToken和refreshToken都无效
		return null;
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public boolean logout() {
		logger.debug("logout");

		// 清理用户上下文
		UserPrincipal principal = UserContext.getContext();
		if (principal == null || StringUtils.isBlank(principal.getToken())) {
			logger.error("用户尚未登录");
			UserContext.clearContext();
			return true;
		}
		Caches.delete(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, principal.getToken());
		UserContext.clearContext();

		return true;
	}

	/**
	 * 注册帐号
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public LoginResultVO register(@RequestBody RegisterVO register) {
		logger.debug("register={}", register);
		Assert.notNull(register.getUsername(), "用户名不能为空");
		Assert.hasText(register.getPassword(), "密码不能为空");

		User user = User.fromRegisterVO(register);
		userService.register(user);

		String deviceUuid = request.getHeader(Constants.HEADER_NAME_DEVICE_UUID);
		String deviceId = request.getHeader(Constants.HEADER_NAME_DEVICE_ID);
		String deviceMac = request.getHeader(Constants.HEADER_NAME_DEVICE_MAC);
		logger.debug("deviceUuid={}, deviceId={}, deviceMac={}", deviceUuid, deviceId, deviceMac);
		return loginPostProcess(user, deviceUuid, deviceId, deviceMac);
	}

	/**
	 * 检查用户名是否可用，true表示可用，false表示不可用
	 */
	@RequestMapping(value = "/user/checkUsernameAvailable", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkUsernameAvailable(String username) {
		Assert.hasText(username, "用户名不能为空");
		User user = userService.getByLoginName(username);
		logger.debug("get by username {}, result is {}", username, user);
		return user == null;
	}

	/**
	 * 根据ID获取用户帐号
	 */
	@RequestMapping(value = "/user/id/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserVO getById(@PathVariable("id") Integer id) {
		Assert.notNull(id, "ID不能为空");
		User user = userService.getById(id);
		if (user == null) {
			throw new RestException("用户不存在");
		}
		logger.debug("get by id {}, result is {}", id, user);
		return user.toVO();
	}

}
