package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.vo.SeekCategory;

/**
 * OfferRangeDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface OfferRangeDao {

	/**
	 * 保存或更新帮助者的帮忙范围
	 */
	public void saveOrUpdate(Integer userId, List<SeekCategory> offerRange);

	/**
	 * 获取帮助者的帮忙范围信息
	 */
	public List<SeekCategory> getByUserId(Integer userId);

}
