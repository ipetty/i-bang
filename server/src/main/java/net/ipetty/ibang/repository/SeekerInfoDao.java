package net.ipetty.ibang.repository;

import net.ipetty.ibang.model.SeekerInfo;

/**
 * SeekerInfoDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface SeekerInfoDao {

	/**
	 * 保存或更新
	 */
	public void saveOrUpdate(SeekerInfo seekerInfo);

	/**
	 * 获取求助者相应积分信息
	 */
	public SeekerInfo getByUserId(Integer userId);

}
