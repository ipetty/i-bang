package net.ipetty.ibang.api;

import net.ipetty.ibang.vo.CrashLogVO;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * CrashLogApi
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public interface CrashLogApi {

	/**
	 * 保存崩溃日志
	 */
	@POST("/crash")
	public boolean save(@Body CrashLogVO crashLog);

}
