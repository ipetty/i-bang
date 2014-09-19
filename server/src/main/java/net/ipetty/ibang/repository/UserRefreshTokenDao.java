package net.ipetty.ibang.repository;

import net.ipetty.ibang.model.UserRefreshToken;

/**
 * UserRefreshTokenDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface UserRefreshTokenDao {

	/**
	 * 保存
	 */
	public void save(UserRefreshToken refreshToken);

	/**
	 * 获取RefreshToken
	 */
	public UserRefreshToken get(String refreshToken);

	/**
	 * 获取指定用户在指定设备上的RefreshToken
	 */
	public UserRefreshToken get(Integer userId, String deviceUuid);

	/**
	 * 删除RefreshToken
	 */
	public void delete(String refreshToken);

}
