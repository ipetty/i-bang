package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Letter;
import net.ipetty.ibang.repository.LetterDao;
import net.ipetty.ibang.vo.LetterContacts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月7日
 */
@Service
@Transactional
public class LetterService extends BaseService {

	@Resource
	private LetterDao letterDao;

	/**
	 * 保存
	 */
	public Letter send(Integer fromUserId, Integer toUserId, String content) {
		Assert.notNull(fromUserId, "站内信发送者不能为空");
		Assert.notNull(toUserId, "站内信接收者不能为空");
		Assert.isTrue(!fromUserId.equals(toUserId), "不能给自己发站内信");
		Assert.hasText(content, "站内信内容不能为空");

		Letter letterA = new Letter();
		letterA.setOwnerId(fromUserId);
		letterA.setCooperatorId(toUserId);
		letterA.setContent(content);
		letterA.setInbox(false);
		letterA.setRead(true);
		letterA.setCreatedOn(new Date());
		letterDao.save(letterA);

		Letter letterB = new Letter();
		letterB.setOwnerId(toUserId);
		letterB.setCooperatorId(fromUserId);
		letterB.setContent(content);
		letterB.setInbox(true);
		letterA.setRead(false);
		letterB.setCreatedOn(letterA.getCreatedOn());
		letterDao.save(letterB);

		return letterA;
	}

	/**
	 * 获取
	 */
	public Letter getById(Long id) {
		return letterDao.getById(id);
	}

	/**
	 * 获取指定用户的联系人列表
	 */
	public List<LetterContacts> listContactsByUserId(Integer userId, int pageNumber, int pageSize) {
		return letterDao.listContactsByUserId(userId, pageNumber, pageSize);
	}

	/**
	 * 获取指定用户与某个联系人的站内信ID列表
	 */
	public List<Letter> listByUserIdAndContactUserId(Integer userId, Integer contactUserId, int pageNumber, int pageSize) {
		List<Long> letterIds = letterDao.listByUserIdAndContactUserId(userId, contactUserId, pageNumber, pageSize);
		List<Letter> letters = new ArrayList<Letter>();
		for (Long letterId : letterIds) {
			letters.add(this.getById(letterId));
		}

		letterDao.updateReadByContactUserId(userId, contactUserId);

		return letters;
	}

	/**
	 * 获取指定用户的未读站内信数
	 */
	public int getUnreadNumberByUserId(Integer userId) {
		return letterDao.getUnreadNumberByUserId(userId);
	}

}
