package net.ipetty.ibang.repository;

import java.util.List;

import net.ipetty.ibang.model.SystemMessage;

/**
 * SystemMessageDao
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
public interface SystemMessageDao {

	/**
	 * 保存
	 */
	public void save(SystemMessage systemMessage);

	/**
	 * 获取
	 */
	public SystemMessage getById(Long id);

	/**
	 * 更新为已读状态
	 */
	public void updateRead(Long id);

	/**
	 * 获取指定用户的系统消息ID列表
	 */
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户的未读系统消息ID列表
	 */
	public List<Long> listUnreadByUserId(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取指定用户的未读系统消息数
	 */
	public int getUnreadNumberByUserId(Integer userId);

}
