package net.ipetty.ibang.repository;

import java.util.Date;
import java.util.List;

import net.ipetty.ibang.model.Seek;

/**
 * SeekDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface SeekDao {

	/**
	 * 保存
	 */
	public void save(Seek seek);

	/**
	 * 获取
	 */
	public Seek getById(Long id);

	/**
	 * 获取最新的未关闭求助单ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listLatest(Date timeline, int pageNumber, int pageSize);

	/**
	 * 获取指定用户的求助单ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 更新求助单状态
	 */
	public void updateStatus(Long seekId, String newStatus);

}
