package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.SeekVO;
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
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/seeklist/byuser")
	public List<SeekVO> listByUserId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 关闭求助
	 */
	@POST("/seek/close")
	public boolean close(@Query("seekId") Long seekId);

}
