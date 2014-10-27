package net.ipetty.ibang.api;

import java.util.List;

import net.ipetty.ibang.vo.ImageVO;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * ImageApi
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
public interface ImageApi {

	/**
	 * 上传图片文件并返回图片对象VO
	 */
	@Multipart
	@POST("/image/upload")
	public ImageVO upload(@Part("imageFile") TypedFile imageFile);

	/**
	 * 获取
	 */
	@GET("/image")
	public ImageVO getById(@Query("id") Long id);

	/**
	 * 获取指定求助单的图片列表
	 */
	@GET("/imagelist/byseek")
	public List<ImageVO> listBySeekId(@Query("seekId") Long seekId);

	/**
	 * 获取指定评价的图片列表
	 */
	@GET("/imagelist/byevaluation")
	public List<ImageVO> listByEvaluationId(@Query("evaluationId") Long evaluationId);

}
