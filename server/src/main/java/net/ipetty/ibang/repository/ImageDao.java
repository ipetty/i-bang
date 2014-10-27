package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Image;

/**
 * ImageDao
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface ImageDao {

	/**
	 * 保存
	 */
	public void save(Image image);

	/**
	 * 将图片与求助单相关联
	 */
	public void saveImageToSeek(Long seekId, List<Long> imageIds);

	/**
	 * 将图片与评价相关联
	 */
	public void saveImageToEvaluation(Long evaluationId, List<Long> imageIds);

	/**
	 * 获取
	 */
	public Image getById(Long id);

	/**
	 * 获取指定求助单的图片ID列表
	 */
	public List<Long> listBySeekId(Long seekId);

	/**
	 * 获取指定评价的图片ID列表
	 */
	public List<Long> listByEvaluationId(Long evaluationId);

}
