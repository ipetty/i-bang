package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.SeekerInfo;
import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.repository.SeekDao;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.SeekCategory;

import org.apache.commons.collections.CollectionUtils;
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

	@Resource
	private ImageService imageService;

	@Resource
	private SeekerInfoService seekerInfoService;

	@Resource
	private OfferService offerService;

	@Resource
	private UserService userService;

	@Resource
	private SystemMessageService systemMessageService;

	/**
	 * 发布求助
	 */
	public void publish(Seek seek) {
		Assert.notNull(seek, "求助单不能为空");
		Assert.notNull(seek.getType(), "类型不能为空");
		Assert.notNull(seek.getSeekerId(), "求助人不能为空");

		// 生成序列号 TODO 生成日期数字的序列号
		seek.setSn(UUIDUtils.generateShortUUID());

		// 状态
		seek.setStatus(Constants.SEEK_STATUS_CREATED);

		// 求助单中的图片信息
		List<Long> imageIds = new ArrayList<Long>();
		for (Image image : seek.getImages()) {
			imageService.save(image);
			imageIds.add(image.getId());
		}

		// 保存求助单主体
		seekDao.save(seek);

		// 将图片与求助单相关联
		imageService.saveImageToSeek(seek.getId(), imageIds);

		// 更新求助者求助次数
		SeekerInfo seekerInfo = seekerInfoService.getByUserId(seek.getSeekerId());
		seekerInfo.setSeekCount(seekerInfo.getSeekCount() + 1);
		seekerInfoService.saveOrUpdate(seekerInfo);
	}

	/**
	 * 获取
	 */
	public Seek getById(Long id) {
		Assert.notNull(id, "求助单ID不能为空");

		Seek seek = seekDao.getById(id);
		if (seek == null) {
			throw new BusinessException("指定ID（" + id + "）的求助单不存在");
		}

		// 求助单中的图片信息
		List<Image> images = imageService.listBySeekId(id);
		seek.setImages(images);

		return seek;
	}

	/**
	 * 获取最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatest(String type, Date timeline, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatest(type, timeline, pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 获取指定分类中最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatestByCategory(String type, String categoryL1, String categoryL2, Date timeline,
			int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatestByCategory(type, categoryL1, categoryL2, timeline, pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 获取所在城市指定分类中最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatestByCityOrCategory(String type, String city, String district, String categoryL1,
			String categoryL2, Date timeline, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatestByCityOrCategory(type, city, district, categoryL1, categoryL2, timeline,
				pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 获取所在城市指定用户帮忙范围内的最新未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatestByCityAndOfferRange(String type, String city, String district, Integer userId,
			Date timeline, int pageNumber, int pageSize) {
		if (userId == null) {
			return this.listLatestByCityOrCategory(type, city, district, null, null, timeline, pageNumber, pageSize);
		}

		User user = userService.getById(userId);
		if (user == null) {
			return this.listLatestByCityOrCategory(type, city, district, null, null, timeline, pageNumber, pageSize);
		}
		List<SeekCategory> offerRange = user.getOffererInfo().getOfferRange();
		if (CollectionUtils.isEmpty(offerRange)) {
			return this.listLatestByCityOrCategory(type, city, district, null, null, timeline, pageNumber, pageSize);
		}

		List<Long> seekIds = seekDao.listLatestByCityAndOfferRange(type, city, district, offerRange, timeline,
				pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 根据关键字搜索最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listLatestByKeyword(String type, String keyword, Date timeline, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatestByKeyword(type, keyword, timeline, pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Seek> listByUserId(String type, Integer userId, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listByUserId(type, userId, pageNumber, pageSize);
		return this.listByIds(seekIds);
	}

	/**
	 * 根据id列表获取求助列表
	 */
	public List<Seek> listByIds(List<Long> seekIds) {
		List<Seek> seeks = new ArrayList<Seek>();
		for (Long seekId : seekIds) {
			seeks.add(this.getById(seekId));
		}
		return seeks;
	}

	/**
	 * 求助已结束（在所有委托都已关闭或成为双方已评价的状态则由业务自动调用此方法）
	 */
	public void finish(Long seekId) {
		seekDao.updateStatus(seekId, Constants.SEEK_STATUS_FINISHED);
	}

	/**
	 * 关闭求助
	 */
	public void close(Long seekId) {
		seekDao.updateStatus(seekId, Constants.SEEK_STATUS_CLOSED);

		// 向所有应征者发送系统消息
		Seek seek = getById(seekId);
		List<Offer> offers = offerService.listBySeekId(seekId);
		for (Offer offer : offers) {
			// 保存系统消息
			SystemMessage systemMessage = new SystemMessage(seek.getSeekerId(), offer.getOffererId(),
					Constants.SYS_MSG_TYPE_SEEK_CLOSED, "您曾应征的一个求助单已被求助者关闭。", seek.getContent());
			systemMessage.setSeekId(seekId);
			systemMessage.setOfferId(offer.getId());
			systemMessageService.save(systemMessage);
		}
	}

	/**
	 * 更新求助单状态至应征中状态
	 */
	public void offered(Long seekId) {
		seekDao.updateStatus(seekId, Constants.SEEK_STATUS_OFFERED);
	}

	/**
	 * 更新求助单状态至已委托状态
	 */
	public void delegated(Long seekId) {
		seekDao.updateStatus(seekId, Constants.SEEK_STATUS_DELEGATED);
	}

}
