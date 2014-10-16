package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Evaluation;

/**
 * EvaluationDao
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
public interface EvaluationDao {

	/**
	 * 保存
	 */
	public void save(Evaluation evaluation);

	/**
	 * 获取
	 */
	public Evaluation getById(Long id);

	/**
	 * 获取委托对应的评价
	 */
	public List<Long> listByDelegationId(Long delegationId);

	/**
	 * 获取指定用户给出的评价ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByEvaluatorId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户获得的评价ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByEvaluateTargetId(Integer userId, int pageNumber, int pageSize);

}
