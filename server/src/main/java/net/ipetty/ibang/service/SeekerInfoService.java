package net.ipetty.ibang.service;

import javax.annotation.Resource;

import net.ipetty.ibang.model.SeekerInfo;
import net.ipetty.ibang.repository.SeekerInfoDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * SeekerInfoService
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Service
@Transactional
public class SeekerInfoService extends BaseService {

	@Resource
	private SeekerInfoDao seekerInfoDao;

	/**
	 * 保存或更新
	 */
	public void saveOrUpdate(SeekerInfo seekerInfo) {
		Assert.notNull(seekerInfo, "求助人信息不能为空");
		Assert.notNull(seekerInfo.getUserId(), "求助人ID不能为空");

		seekerInfoDao.saveOrUpdate(seekerInfo);
	}

	/**
	 * 获取求助者相应积分信息
	 */
	public SeekerInfo getByUserId(Integer userId) {
		SeekerInfo seekerInfo = seekerInfoDao.getByUserId(userId);
		return seekerInfo != null ? seekerInfo : new SeekerInfo(userId, 0, 0, null);
	}

}
