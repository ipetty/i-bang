package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Delegation;

/**
 * DelegationDao
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
public interface DelegationDao {

	/**
	 * 保存
	 */
	public void save(Delegation delegation);

	/**
	 * 获取
	 */
	public Delegation getById(Long id);

	/**
	 * 获取指定用户的委托ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户的指定状态的委托ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByUserIdAndStatus(Integer userId, String status, int pageNumber, int pageSize);

	/**
	 * 更新委托状态
	 */
	public void updateStatus(Long delegationId, String newStatus);

}
