package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Offer;

/**
 * OfferDao
 * @author luocanfeng
 * @date 2014年9月22日
 */
public interface OfferDao {

	/**
	 * 保存
	 */
	public void save(Offer offer);

	/**
	 * 获取
	 */
	public Offer getById(Long id);

	/**
	 * 获取指定求助单的应征单ID列表
	 */
	public List<Long> listBySeekId(Long seekId);

	/**
	 * 获取指定用户的应征单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 更新应征单状态
	 */
	public void updateStatus(Long offerId, String newStatus);

	/**
	 * 禁用/软删除
	 */
	public void disable(Long offerId);

}
