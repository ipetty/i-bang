package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.SeekWithLocationVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * SeekApi
 * 
 * @author luocanfeng
 * @date 2014年10月10日
 */
public interface SeekApi {

	/**
	 * 发布求助
	 */
	@POST("/seek/publish")
	public SeekVO publish(@Body SeekVO seek);

	/**
	 * 发布求助（带地理位置）
	 */
	@POST("/seek/publishWithLocation")
	public SeekVO publish(@Body SeekWithLocationVO seek);

	/**
	 * 发布帮忙（带地理位置）
	 */
	@POST("/seek/publishAssistance")
	public SeekVO publishAssistance(@Body SeekWithLocationVO seek);

	/**
	 * 获取
	 */
	@GET("/seek")
	public SeekVO getById(@Query("id") Long id);

	/**
	 * 获取最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/latest")
	public List<SeekVO> listLatest(@Query("timeline") String timeline, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取最新的未关闭求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/latest")
	public List<SeekVO> listLatest(@Query("type") String type, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取指定分类中最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/latestbycategory")
	public List<SeekVO> listLatestByCategory(@Query("categoryL1") String categoryL1,
			@Query("categoryL2") String categoryL2, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取指定分类中最新的未关闭求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/latestbycategory")
	public List<SeekVO> listLatestByCategory(@Query("type") String type, @Query("categoryL1") String categoryL1,
			@Query("categoryL2") String categoryL2, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取所在城市指定分类中最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/latestbycityorcategory")
	public List<SeekVO> listLatestByCityOrCategory(@Query("city") String city, @Query("district") String district,
			@Query("categoryL1") String categoryL1, @Query("categoryL2") String categoryL2,
			@Query("timeline") String timeline, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取所在城市指定分类中最新的未关闭求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/latestbycityorcategory")
	public List<SeekVO> listLatestByCityOrCategory(@Query("type") String type, @Query("city") String city,
			@Query("district") String district, @Query("categoryL1") String categoryL1,
			@Query("categoryL2") String categoryL2, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取所在城市指定用户帮忙范围内的最新未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/latestbycityAndOfferRange")
	public List<SeekVO> listLatestByCityAndOfferRange(@Query("city") String city, @Query("district") String district,
			@Query("userId") Integer userId, @Query("timeline") String timeline, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取所在城市指定用户帮忙范围内的最新未关闭求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/latestbycityAndOfferRange")
	public List<SeekVO> listLatestByCityAndOfferRange(@Query("type") String type, @Query("city") String city,
			@Query("district") String district, @Query("userId") Integer userId, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 根据关键字搜索最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/latestbykeyword")
	public List<SeekVO> listLatestByKeyword(@Query("keyword") String keyword, @Query("timeline") String timeline,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 根据关键字搜索最新的未关闭求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/latestbykeyword")
	public List<SeekVO> listLatestByKeyword(@Query("type") String type, @Query("keyword") String keyword,
			@Query("timeline") String timeline, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/byuser")
	public List<SeekVO> listByUserId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的求助/帮忙列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeks/byuser")
	public List<SeekVO> listByUserId(@Query("type") String type, @Query("userId") Integer userId,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 根据id列表获取求助列表
	 */
	@GET("/seeklist/byids")
	public List<SeekVO> listByIds(@Query("seekIds") Long... seekIds);

	/**
	 * 关闭求助
	 */
	@POST("/seek/close")
	public boolean close(@Query("seekId") Long seekId);

}
