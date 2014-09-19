package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.repository.SeekDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * SeekService
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Service
@Transactional
public class SeekService extends BaseService {

	@Resource
	private SeekDao seekDao;

	/**
	 * 发布求助
	 */
	public void publish(Seek seek) {
		Assert.notNull(seek, "求助单不能为空");

		// 生成序列号

		// 状态

		seekDao.save(seek);

		// 求助单中的图片信息

		// 更新求助者求助次数
	}

	/**
	 * 获取
	 */
	public Seek getById(Long id) {
		Seek seek = seekDao.getById(id);

		// 求助单中的图片信息

		return seek;
	}

	/**
	 * 获取最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatest(Date timeline, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatest(timeline, pageNumber, pageSize);
		List<Seek> seeks = new ArrayList<Seek>();
		for (Long seekId : seekIds) {
			seeks.add(seekDao.getById(seekId));
		}
		return seeks;
	}

	/**
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listByUserId(userId, pageNumber, pageSize);
		List<Seek> seeks = new ArrayList<Seek>();
		for (Long seekId : seekIds) {
			seeks.add(seekDao.getById(seekId));
		}
		return seeks;
	}

	/**
	 * 更新求助单状态
	 */
	public void updateStatus(Long seekId, String newStatus) {
		seekDao.updateStatus(seekId, newStatus);
	}

}
