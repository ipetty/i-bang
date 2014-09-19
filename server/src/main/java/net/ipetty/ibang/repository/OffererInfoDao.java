package net.ipetty.ibang.repository;

import net.ipetty.ibang.model.OffererInfo;

/**
 * OffererInfoDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface OffererInfoDao {

	/**
	 * 保存或更新
	 */
	public void saveOrUpdate(OffererInfo offererInfo);

	/**
	 * 获取帮助者相应积分信息
	 */
	public OffererInfo getByUserId(Integer userId);

}
