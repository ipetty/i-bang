package net.ipetty.ibang.repository;

import net.ipetty.ibang.model.User;

/**
 * UserDao
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface UserDao {

	/**
	 * 保存用户帐号
	 */
	public void save(User user);

	/**
	 * 根据ID获取用户帐号
	 */
	public User getById(Integer id);

	/**
	 * 根据帐号获取用户ID
	 */
	public Integer getUserIdByUsername(String username);

	/**
	 * 根据邮箱获取用户ID
	 */
	public Integer getUserIdByEmail(String email);

	/**
	 * 根据登录帐号或邮箱获取用户ID
	 */
	public Integer getUserIdByLoginName(String loginName);

	/**
	 * 更新用户帐号信息
	 */
	public void update(User user);

	/**
	 * 更新邮箱
	 */
	public void updateEmail(Integer id, String email);

	/**
	 * 修改密码
	 */
	public void changePassword(Integer id, String newEncodedPassword);

	/**
	 * 修改密码与盐值
	 */
	public void changePassword(Integer id, String newEncodedPassword, String salt);

	/**
	 * 禁用
	 */
	public void disable(Integer id);

}
