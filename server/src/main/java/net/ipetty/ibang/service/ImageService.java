package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.repository.ImageDao;
import net.ipetty.ibang.util.UUIDUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageService
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
@Service
@Transactional
public class ImageService extends BaseService {

	@Resource
	private ImageDao imageDao;

	/**
	 * 保存
	 */
	public void save(Image image) {
		// sn
		image.setSn(UUIDUtils.generateShortUUID());

		imageDao.save(image);
	}

	/**
	 * 将图片与求助单相关联
	 */
	public void saveImageToSeek(Long seekId, List<Long> imageIds) {
		imageDao.saveImageToSeek(seekId, imageIds);
	}

	/**
	 * 获取
	 */
	public Image getById(Long id) {
		return imageDao.getById(id);
	}

	/**
	 * 获取指定求助单的图片列表
	 */
	public List<Image> listBySeekId(Long seekId) {
		List<Long> imageIds = imageDao.listBySeekId(seekId);
		List<Image> images = new ArrayList<Image>();
		for (Long imageId : imageIds) {
			images.add(imageDao.getById(imageId));
		}
		return images;
	}

}
