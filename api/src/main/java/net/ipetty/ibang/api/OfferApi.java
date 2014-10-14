package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.OfferVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * OfferApi
 * 
 * @author luocanfeng
 * @date 2014年10月14日
 */
public interface OfferApi {

	/**
	 * 应征
	 */
	@POST("/offer")
	public OfferVO offer(@Body OfferVO offer);

	/**
	 * 获取
	 */
	@GET("/offer")
	public OfferVO getById(@Query("id") Long id);

	/**
	 * 获取指定求助单的应征单列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/offerlist/byseek")
	public List<OfferVO> listBySeekId(@Query("seekId") Long seekId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的应征列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/offerlist/byuser")
	public List<OfferVO> listByUserId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 完成应征
	 */
	@POST("/offer/finish")
	public boolean finish(@Query("offerId") Long offerId);

	/**
	 * 关闭应征
	 */
	@POST("/offer/close")
	public boolean close(@Query("offerId") Long offerId);

}
