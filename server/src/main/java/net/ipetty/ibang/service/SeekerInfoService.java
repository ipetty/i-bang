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
		return seekerInfo != null ? seekerInfo : new SeekerInfo(userId, 0, 0, this.getTitle(0));
	}

	/**
	 * 根据总积分获取相应头衔
	 */
	public String getTitle(int totalPoint) {
		if (totalPoint >= 50000) {
			return "国家级大好人";
		} else if (totalPoint >= 10000) {
			return "省级大好人";
		} else if (totalPoint >= 3000) {
			return "市级大好人";
		} else if (totalPoint >= 1000) {
			return "区级大好人";
		} else {
			return "大好人";
		}
	}

}
