package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.Letter;
import net.ipetty.ibang.vo.LetterContacts;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月6日
 */
public interface LetterDao {

	/**
	 * 保存
	 */
	public void save(Letter letter);

	/**
	 * 获取
	 */
	public Letter getById(Long id);

	/**
	 * 将某联系人的所有站内信更新为已读状态
	 */
	public void updateReadByContactUserId(Integer userId, Integer contactUserId);

	/**
	 * 获取指定用户的联系人列表
	 */
	public List<LetterContacts> listContactsByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户与某个联系人的站内信ID列表
	 */
	public List<Long> listByUserIdAndContactUserId(Integer userId, Integer contactUserId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户的未读站内信数
	 */
	public int getUnreadNumberByUserId(Integer userId);

}
