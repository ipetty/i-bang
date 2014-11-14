package net.ipetty.ibang.api;

import net.ipetty.ibang.vo.ReportVO;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月14日
 */
public interface ReportApi {

	/**
	 * 举报
	 */
	@POST("/report")
	public ReportVO report(@Body ReportVO report);

	/**
	 * 获取举报信息
	 */
	@GET("/report")
	public ReportVO getById(@Query("id") Long id);

}
