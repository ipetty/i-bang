package net.ipetty.ibang.web.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.ipetty.ibang.api.Constants;
import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.Caches;
import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.model.UserRefreshToken;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.util.Encodes;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.LoginResultVO;
import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
	public LoginResultVO login(String loginName, String password) {
		logger.debug("login with loginName={}", loginName);
		Assert.hasText(loginName, "登录名不能为空");
		Assert.hasText(password, "密码不能为空");
		User user = userService.login(loginName, password); // 未发生异常则已登录成功

		String deviceUuid = request.getHeader(Constants.HEADER_NAME_DEVICE_UUID);
		deviceUuid = StringUtils.isBlank(deviceUuid) ? null : new String(Encodes.decodeBase64(deviceUuid),
				Constants.UTF8);
		String deviceId = request.getHeader(Constants.HEADER_NAME_DEVICE_ID);
		deviceId = StringUtils.isBlank(deviceId) ? null : new String(Encodes.decodeBase64(deviceId), Constants.UTF8);
		String deviceMac = request.getHeader(Constants.HEADER_NAME_DEVICE_MAC);
		deviceMac = StringUtils.isBlank(deviceMac) ? null : new String(Encodes.decodeBase64(deviceMac), Constants.UTF8);
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
	public UserVO getById(@PathVariable("id") Integer id) {
		Assert.notNull(id, "ID不能为空");
		User user = userService.getById(id);
		if (user == null) {
			throw new RestException("用户不存在");
		}
		logger.debug("get by id {}, result is {}", id, user);
		return user.toVO();
	}

	/**
	 * 根据用户帐号获取用户信息
	 */
	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	public UserVO getByUsername(@PathVariable("username") String username) {
		Assert.hasText(username, "用户帐号不能为空");
		try {
			User user = userService.getByUsername(username);
			logger.debug("get by username {}, result is {}", username, user);
			return user.toVO();
		} catch (BusinessException e) {
			throw new RestException("用户（" + username + "）不存在");
		}
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public boolean changePassword(String oldPassword, String newPassword) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能修改密码");
		}
		// Assert.hasText(oldPassword, "旧密码不能为空");
		Assert.hasText(newPassword, "新密码不能为空");
		userService.changePassword(currentUser.getId(), oldPassword, newPassword);
		return true;
	}

	/**
	 * 更新用户头像
	 */
	@RequestMapping(value = "/user/updateAvatar", method = RequestMethod.POST)
	public UserVO updateAvatar(MultipartFile imageFile) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能更新头像");
		}
		return userService.updateAvatar(imageFile, currentUser.getId()).toVO();
	}

	/**
	 * 修改用户个人信息
	 */
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public UserVO update(@RequestBody UserFormVO userFormVo) {
		Assert.notNull(userFormVo, "用户个人信息表单不能为空");
		Assert.notNull(userFormVo.getId(), "用户ID不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能修改个人信息");
		}

		Assert.isTrue(userFormVo.getId().equals(currentUser.getId()), "只能修改自己的个人信息");

		return userService.updateProfile(User.fromUserFormVO(userFormVo)).toVO();
	}

	/**
	 * 修改用户帮助范围
	 */
	@RequestMapping(value = "/user/updateOfferRange", method = RequestMethod.POST)
	public UserVO updateOfferRange(@RequestBody UserOfferRange userOfferRange) {
		Assert.notNull(userOfferRange, "帮助范围不能为空");
		Assert.notNull(userOfferRange.getUserId(), "用户ID不能为空");
		Assert.notNull(userOfferRange.getOfferRange(), "帮助范围不能为空");

		User user = userService.getById(userOfferRange.getUserId());
		if (user == null) {
			throw new RestException("用户不存在");
		}

		userService.updateOfferRange(userOfferRange.getUserId(), userOfferRange.getOfferRange());
		return userService.getById(userOfferRange.getUserId()).toVO();
	}

}
