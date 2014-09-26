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
			offererInfo = new OffererInfo(userId, 0, 0, null, offerRangeDao.getByUserId(userId));
		}
		return offererInfo;
	}

}
