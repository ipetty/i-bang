package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.SeekerInfo;
import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.repository.SeekDao;
import net.ipetty.ibang.util.UUIDUtils;
import net.ipetty.ibang.vo.Constants;

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
		Seek seek = seekDao.getById(id);

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
	public List<Seek> listLatest(Date timeline, int pageNumber, int pageSize) {
		List<Long> seekIds = seekDao.listLatest(timeline, pageNumber, pageSize);
		List<Seek> seeks = new ArrayList<Seek>();
		for (Long seekId : seekIds) {
			seeks.add(this.getById(seekId));
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
		List<Offer> offers = offerService.listBySeekId(seekId, 0, 1000);
		for (Offer offer : offers) {
			// 保存系统消息
			SystemMessage systemMessage = new SystemMessage(null, offer.getOffererId(),
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
