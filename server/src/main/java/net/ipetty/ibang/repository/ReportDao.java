package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Report;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月13日
 */
public interface ReportDao {

	/**
	 * 举报
	 */
	public void save(Report report);

	/**
	 * 获取
	 */
	public Report getById(Long id);

	/**
	 * 处理举报
	 */
	public void deal(Long id, String result);

	/**
	 * 获取未处理举报ID列表
	 */
	public List<Long> listUndealed(int pageNumber, int pageSize);

	/**
	 * 获取未处理举报数
	 */
	public int getUndealedNum();

	/**
	 * 获取已处理举报ID列表
	 */
	public List<Long> listDealed(int pageNumber, int pageSize);

	/**
	 * 获取已处理举报数
	 */
	public int getDealedNum();

	/**
	 * 获取举报ID列表
	 */
	public List<Long> list(int pageNumber, int pageSize);

	/**
	 * 获取全部举报数
	 */
	public int getAllNum();

}
