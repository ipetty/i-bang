package net.ipetty.ibang.service;

import javax.annotation.Resource;

import net.ipetty.ibang.model.OffererInfo;
import net.ipetty.ibang.repository.OfferRangeDao;
import net.ipetty.ibang.repository.OffererInfoDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * OffererInfoService
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Service
@Transactional
public class OffererInfoService extends BaseService {

	@Resource
	private OffererInfoDao offererInfoDao;
	@Resource
	private OfferRangeDao offerRangeDao;

	/**
	 * 保存或更新
	 */
	public void saveOrUpdate(OffererInfo offererInfo) {
		Assert.notNull(offererInfo, "帮助人信息不能为空");
		Assert.notNull(offererInfo.getUserId(), "帮助人ID不能为空");

		offererInfoDao.saveOrUpdate(offererInfo);
		offerRangeDao.saveOrUpdate(offererInfo.getUserId(), offererInfo.getOfferRange());
	}

	/**
	 * 获取帮助者相应积分信息
	 */
	public OffererInfo getByUserId(Integer userId) {
		OffererInfo offererInfo = offererInfoDao.getByUserId(userId);
		if (offererInfo != null) {
			offererInfo.setOfferRange(offerRangeDao.getByUserId(userId));
		} else {
			offererInfo = new OffererInfo(userId, 0, 0, this.getTitle(0), offerRangeDao.getByUserId(userId));
		}
		return offererInfo;
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
