package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.EvaluationVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * EvaluationApi
 * 
 * @author luocanfeng
 * @date 2014年10月16日
 */
public interface EvaluationApi {

	/**
	 * 评价
	 */
	@POST("/evaluate")
	public EvaluationVO evaluate(@Body EvaluationVO evaluation);

	/**
	 * 获取
	 */
	@GET("/evaluation")
	public EvaluationVO getById(@Query("id") Long id);

	/**
	 * 获取委托对应的评价
	 */
	@GET("/evaluationlist/bydelegation")
	public List<EvaluationVO> listByDelegationId(@Query("delegationId") Long delegationId);

	/**
	 * 获取指定用户给出的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/evaluationlist/byevaluator")
	public List<EvaluationVO> listByEvaluatorId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户获得的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@GET("/evaluationlist/byevaluatetarget")
	public List<EvaluationVO> listByEvaluateTargetId(@Query("userId") Integer userId,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

}
