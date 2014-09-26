package net.ipetty.ibang.service;

import java.io.IOException;

import javax.annotation.Resource;

import net.ipetty.ibang.context.SpringContextHelper;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.model.UserRefreshToken;
import net.ipetty.ibang.repository.UserDao;
import net.ipetty.ibang.repository.UserRefreshTokenDao;
import net.ipetty.ibang.util.ImageUtils;
import net.ipetty.ibang.util.SaltEncoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserService
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
@Service
@Transactional
public class UserService extends BaseService {

	@Resource
	private UserDao userDao;

	@Resource
	private UserRefreshTokenDao refreshTokenDao;

	/**
	 * 登录验证
	 */
	public User login(String loginName, String password) throws BusinessException {
		Assert.hasText(loginName, "用户名不能为空");
		Assert.hasText(password, "密码不能为空");

		User user = this.getByLoginName(loginName);
		if (user == null) {
			throw new BusinessException("用户名不存在");
		}

		String encodedPassword = SaltEncoder.encode(password, user.getSalt());
		if (!StringUtils.equals(user.getPassword(), encodedPassword)) {
			throw new BusinessException("密码错误");
		}

		return user;
	}

	/**
	 * 注册帐号
	 */
	public void register(User user) throws BusinessException {
		// verify accounts feilds
		if (StringUtils.isBlank(user.getUsername()) && StringUtils.isBlank(user.getEmail())) {
			// No account field been setted
			throw new BusinessException("请填写帐号或邮箱");
		}

		// verify password
		Assert.hasText(user.getPassword(), "密码不能为空");

		// check unique
		this.checkUniqueForRegister(user.getUsername(), "帐号");
		this.checkUniqueForRegister(user.getEmail(), "邮箱");

		// nickname
		if (StringUtils.isBlank(user.getNickname())) {
			if (StringUtils.isNotBlank(user.getUsername())) {
				user.setNickname(user.getUsername());
			} else {
				user.setNickname(user.getEmail().contains("@") ? user.getEmail().substring(0,
						user.getEmail().indexOf("@")) : user.getEmail());
			}
		}

		// generate salt
		String salt = SaltEncoder.generateSalt();
		user.setSalt(salt);

		// encode password
		user.setPassword(SaltEncoder.encode(user.getPassword(), user.getSalt()));

		// persist user
		userDao.save(user);
	}

	private void checkUniqueForRegister(String fieldValue, String fieldLabel) throws BusinessException {
		if (StringUtils.isNotBlank(fieldValue)) {
			User orignal = this.getByLoginName(fieldValue);
			if (orignal != null) {
				throw new BusinessException(fieldLabel + "已存在");
			}
		}
	}

	/**
	 * 根据ID获取用户帐号
	 */
	public User getById(Integer id) {
		Assert.notNull(id, "ID不能为空");
		User user = userDao.getById(id);
		if (user == null) {
			throw new BusinessException("指定ID（" + id + "）的用户不存在");
		}
		return user;
	}

	/**
	 * 根据帐号获取用户帐号
	 */
	public User getByUsername(String username) {
		Assert.hasText(username, "帐号不能为空");
		Integer id = userDao.getUserIdByUsername(username);
		if (id == null) {
			throw new BusinessException("指定帐号（" + username + "）的用户不存在");
		}
		return this.getById(id);
	}

	/**
	 * 根据邮箱获取用户帐号
	 */
	public User getByEmail(String email) {
		Assert.hasText(email, "邮箱不能为空");
		Integer id = userDao.getUserIdByEmail(email);
		if (id == null) {
			throw new BusinessException("指定邮箱（" + email + "）的用户不存在");
		}
		return this.getById(id);
	}

	/**
	 * 根据登录帐号或邮箱获取用户帐号
	 */
	public User getByLoginName(String loginName) {
		Assert.hasText(loginName, "登录帐号不能为空");
		Integer id = userDao.getUserIdByLoginName(loginName);
		return id == null ? null : this.getById(id);
	}

	/**
	 * 更新邮箱
	 */
	public void updateEmail(Integer id, String email) {
		Assert.notNull(id, "用户ID不能为空");
		Assert.hasText(email, "邮箱不能为空");
		if (StringUtils.isNotBlank(email)) { // 校验唯一性
			User orignal = this.getByLoginName(email);
			if (orignal != null && !orignal.getId().equals(id)) {
				throw new BusinessException("该邮箱已被使用");
			}
		}

		userDao.updateEmail(id, email);
	}

	/**
	 * 修改密码
	 */
	public void changePassword(Integer id, String oldPassword, String newPassword) {
		Assert.notNull(id, "用户ID不能为空");
		Assert.hasText(newPassword, "新密码不能为空");

		User user = userDao.getById(id);
		if (user == null) {
			throw new BusinessException("指定ID（" + id + "）的用户不存在");
		}

		if (StringUtils.isNotBlank(user.getPassword())) { // 用户已设置密码，则需要校验原密码
			Assert.hasText(oldPassword, "旧密码不能为空");
			String oldEncodedPassword = SaltEncoder.encode(oldPassword, user.getSalt());
			if (!StringUtils.equals(oldEncodedPassword, user.getPassword())) {
				throw new BusinessException("原密码不匹配");
			}
		}

		if (StringUtils.isBlank(user.getSalt())) {
			user.setSalt(SaltEncoder.generateSalt());
		}
		String newEncodedPassword = SaltEncoder.encode(newPassword, user.getSalt());
		userDao.changePassword(id, newEncodedPassword, user.getSalt());
	}

	/**
	 * 更新用户头像
	 */
	public User updateAvatar(MultipartFile imageFile, Integer userId) {
		try {
			Image image = ImageUtils.saveImage(imageFile, SpringContextHelper.getWebContextRealPath());
			User user = userDao.getById(userId);
			user.setAvatar(image.getOriginalUrl());
			userDao.update(user);
			return user;
		} catch (IOException e) {
			throw new BusinessException("保存图片时出错", e);
		}
	}

	/**
	 * 更新个人信息（不含用户名、邮箱、密码、头像的更新）
	 */
	public User updateProfile(User user) {
		Assert.notNull(user, "用户个人信息不能为空");
		Assert.notNull(user.getId(), "用户ID不能为空");
		User original = userDao.getById(user.getId());
		user.setAvatar(original.getAvatar());
		userDao.update(user);
		return userDao.getById(user.getId());
	}

	/**
	 * 保存RefreshToken
	 */
	public void saveRefreshToken(UserRefreshToken refreshToken) {
		refreshTokenDao.save(refreshToken);
	}

	/**
	 * 获取RefreshToken
	 */
	public UserRefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenDao.get(refreshToken);
	}

	/**
	 * 获取指定用户在指定设备上的RefreshToken
	 */
	public UserRefreshToken getRefreshToken(Integer userId, String deviceUuid) {
		return refreshTokenDao.get(userId, deviceUuid);
	}

	/**
	 * 删除RefreshToken
	 */
	public void deleteRefreshToken(String refreshToken) {
		refreshTokenDao.delete(refreshToken);
	}

}
