package net.ipetty.ibang.repository;

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
	public void save(Image image, Long seekId, int index);

	/**
	 * 获取
	 */
	public Image getById(Long id);

}
