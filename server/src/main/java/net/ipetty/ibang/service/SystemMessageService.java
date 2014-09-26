package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.repository.SystemMessageDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SystemMessageService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class SystemMessageService extends BaseService {

	@Resource
	private SystemMessageDao systemMessageDao;

	/**
	 * 保存
	 */
	public void save(SystemMessage systemMessage) {
		systemMessageDao.save(systemMessage);
	}

	/**
	 * 获取
	 */
	public SystemMessage getById(Long id) {
		return systemMessageDao.getById(id);
	}

	/**
	 * 更新为已读状态
	 */
	public void read(Long id) {
		systemMessageDao.updateRead(id);
	}

	/**
	 * 获取指定用户的系统消息列表
	 */
	public List<SystemMessage> listByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> systemMessageIds = systemMessageDao.listByUserId(userId, pageNumber, pageSize);
		List<SystemMessage> systemMessages = new ArrayList<SystemMessage>();
		for (Long systemMessageId : systemMessageIds) {
			systemMessages.add(systemMessageDao.getById(systemMessageId));
		}
		return systemMessages;
	}

	/**
	 * 获取指定用户的未读系统消息列表
	 */
	public List<SystemMessage> listUnreadByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> systemMessageIds = systemMessageDao.listUnreadByUserId(userId, pageNumber, pageSize);
		List<SystemMessage> systemMessages = new ArrayList<SystemMessage>();
		for (Long systemMessageId : systemMessageIds) {
			systemMessages.add(systemMessageDao.getById(systemMessageId));
		}
		return systemMessages;
	}

}
