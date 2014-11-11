package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.LetterContacts;
import net.ipetty.ibang.vo.LetterVO;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月10日
 */
public interface LetterApi {

	/**
	 * 发送站内信
	 */
	@FormUrlEncoded
	@POST("/letter/send")
	public LetterVO send(@Field("toUserId") Integer toUserId, @Field("content") String content);

	/**
	 * 查看联系人列表
	 */
	@GET(value = "/letter/contacts")
	public List<LetterContacts> listContacts(@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

	/**
	 * 查看与某个联系人的站内信列表
	 */
	@GET(value = "/letters")
	public List<LetterVO> list(@Query("contactUserId") Integer contactUserId, @Query("pageNumber") int pageNumber,
			@Query("pageSize") int pageSize);

	/**
	 * 获取指定用户的未读站内信数
	 */
	@GET(value = "/letter/unreadnum")
	public int getUnreadNumber();

}
