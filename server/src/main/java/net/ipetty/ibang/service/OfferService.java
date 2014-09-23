package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.repository.OfferDao;

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

	/**
	 * 应征
	 */
	public void save(Offer offer) {
		Assert.notNull(offer, "应征单不能为空");

		// 生成序列号

		// 状态

		offerDao.save(offer);

		// 更新应征者应征次数
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
	 * 更新应征单状态
	 */
	public void updateStatus(Long offerId, String newStatus) {
		offerDao.updateStatus(offerId, newStatus);
	}

}
