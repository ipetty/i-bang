package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.OffererInfo;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.repository.OfferDao;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.Constants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * OfferService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class OfferService extends BaseService {

	@Resource
	private OfferDao offerDao;

	@Resource
	private SeekService seekService;

	@Resource
	private OffererInfoService offererInfoService;

	/**
	 * 应征
	 */
	public void offer(Offer offer) {
		Assert.notNull(offer, "应征单不能为空");
		Assert.notNull(offer.getOffererId(), "应征者不能为空");
		Assert.notNull(offer.getSeekId(), "所应征的求助单不能为空");

		Seek seek = seekService.getById(offer.getSeekId());
		Assert.notNull(seek, "所应征的求助单不存在");

		// 生成序列号 TODO 生成日期数字的序列号
		offer.setSn(UUIDUtils.generateShortUUID());

		// 状态
		offer.setStatus(Constants.OFFER_STATUS_OFFERED);

		// 保存应征单
		offerDao.save(offer);

		// 更新应征者应征次数
		OffererInfo offererInfo = offererInfoService.getByUserId(offer.getOffererId());
		offererInfo.setOfferCount(offererInfo.getOfferCount() + 1);
		offererInfoService.saveOrUpdate(offererInfo);

		// 更新求助单状态为应征中
		if (Constants.SEEK_STATUS_CREATED.equals(seek.getStatus())) {
			seekService.offered(seek.getId());
		}
	}

	/**
	 * 获取
	 */
	public Offer getById(Long id) {
		return offerDao.getById(id);
	}

	/**
	 * 获取指定求助单的应征单列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Offer> listBySeekId(Long seekId, int pageNumber, int pageSize) {
		List<Long> offerIds = offerDao.listBySeekId(seekId, pageNumber, pageSize);
		List<Offer> offers = new ArrayList<Offer>();
		for (Long offerId : offerIds) {
			offers.add(offerDao.getById(offerId));
		}
		return offers;
	}

	/**
	 * 获取指定用户的应征列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Offer> listByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> offerIds = offerDao.listByUserId(userId, pageNumber, pageSize);
		List<Offer> offers = new ArrayList<Offer>();
		for (Long offerId : offerIds) {
			offers.add(offerDao.getById(offerId));
		}
		return offers;
	}

	/**
	 * 应征已完成
	 */
	public void finish(Long seekId) {
		offerDao.updateStatus(seekId, Constants.OFFER_STATUS_FINISHED);
	}

	/**
	 * 应征已关闭
	 */
	public void close(Long seekId) {
		offerDao.updateStatus(seekId, Constants.OFFER_STATUS_CLOSED);
	}

	/**
	 * 更新应征单状态至已委托状态
	 */
	public void delegated(Long seekId) {
		offerDao.updateStatus(seekId, Constants.OFFER_STATUS_DELEGATED);
	}

}
