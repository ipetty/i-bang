package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.DelegationVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * DelegationApi
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public interface DelegationApi {

	/**
	 * 接受应征
	 */
	@POST("/delegate")
	public DelegationVO delegate(@Body DelegationVO delegation);

	/**
	 * 获取
	 */
	@GET("/delegation")
	public DelegationVO getById(@Query("id") Long id);

	/**
	 * 根据应征单ID获取委托单，如没有对应的委托则返回null
	 */
	@GET("/delegation/byoffer")
	public DelegationVO getByOfferId(@Query("offerId") Long offerId);

	/**
	 * 获取指定用户的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/delegationlist/byuser")
	public List<DelegationVO> listByUserId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的指定状态的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/delegationlist/byuserandstatus")
	public List<DelegationVO> listByUserIdAndStatus(@Query("userId") Integer userId, @Query("status") String status,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取指定求助单的所有委托ID列表
	 */
	@GET("/delegationlist/byseek")
	public List<DelegationVO> listBySeekId(@Query("seekId") Long seekId);

	/**
	 * 完成委托
	 */
	@POST("/delegation/finish")
	public boolean finish(@Query("delegationId") Long delegationId);

	/**
	 * 关闭应征
	 */
	@POST("/delegation/close")
	public boolean close(@Query("delegationId") Long delegationId);

}
