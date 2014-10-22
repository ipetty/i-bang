package net.ipetty.ibang.service;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Location;
import net.ipetty.ibang.repository.LocationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * LocationService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class LocationService extends BaseService {

	@Resource
	private LocationDao locationDao;

	/**
	 * 保存位置信息
	 */
	public void save(Location location) {
		Assert.notNull(location, "位置不能为空");
		locationDao.save(location);
	}

	/**
	 * 根据ID获取位置信息
	 */
	public Location getById(Long id) {
		Assert.notNull(id, "ID不能为空");
		return locationDao.getById(id);
	}

}
