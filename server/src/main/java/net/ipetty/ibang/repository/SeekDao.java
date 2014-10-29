package net.ipetty.ibang.repository;

import java.util.Date;
import java.util.List;

import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.vo.SeekCategory;

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
	 * 获取指定分类中最新的未关闭求助单ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listLatestByCategory(String categoryL1, String categoryL2, Date timeline, int pageNumber,
			int pageSize);

	/**
	 * 获取所在城市指定分类中最新的未关闭求助ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listLatestByCityOrCategory(String city, String district, String categoryL1, String categoryL2,
			Date timeline, int pageNumber, int pageSize);

	/**
	 * 获取所在城市指定用户帮忙范围内的最新未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listLatestByCityAndOfferRange(String city, String district, List<SeekCategory> offerRange,
			Date timeline, int pageNumber, int pageSize);

	/**
	 * 根据关键字搜索最新的未关闭求助单ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listLatestByKeyword(String keyword, Date timeline, int pageNumber, int pageSize);

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
