package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.SystemMessageVO;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * SystemMessageApi
 * 
 * @author luocanfeng
 * @date 2014年10月15日
 */
public interface SystemMessageApi {

	/**
	 * 获取
	 */
	@GET("/sysmsg")
	public SystemMessageVO getById(@Query("id") Long id);

	/**
	 * 更新为已读状态
	 */
	@POST("/sysmsg/read")
	public boolean read(@Query("id") Long id);

	/**
	 * 获取指定用户的系统消息列表
	 */
	@GET("/sysmsglist")
	public List<SystemMessageVO> listByUserId(@Query("userId") Integer userId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的未读系统消息列表
	 */
	@GET("/sysmsglist/unread")
	public List<SystemMessageVO> listUnreadByUserId(@Query("userId") Integer userId,
			@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的未读系统消息数
	 */
	@GET("/sysmsgnum/unread")
	public int getUnreadNumberByUserId(@Query("userId") Integer userId);

}
