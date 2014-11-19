package net.ipetty.ibang.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.SystemMessageService;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.vo.SystemMessageVO;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SystemMessageController
 * @author luocanfeng
 * @date 2014年10月15日
 */
@Controller
public class SystemMessageController extends BaseController {

	@Resource
	private SystemMessageService systemMessageService;

	@Resource
	private UserService userService;

	/**
	 * 获取
	 */
	@RequestMapping(value = "/sysmsg", method = RequestMethod.GET)
	public SystemMessageVO getById(Long id) {
		Assert.notNull(id, "系统消息ID不能为空");
		return systemMessageService.getById(id).toVO();
	}

	/**
	 * 更新为已读状态
	 */
	@RequestMapping(value = "/sysmsg/read", method = RequestMethod.POST)
	public boolean read(Long id) {
		Assert.notNull(id, "系统消息ID不能为空");
		systemMessageService.read(id);
		return true;
	}

	/**
	 * 获取指定用户的系统消息列表
	 */
	@RequestMapping(value = "/sysmsglist", method = RequestMethod.GET)
	public List<SystemMessageVO> listByUserId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");
		List<SystemMessage> systemMessages = systemMessageService.listByUserId(userId, pageNumber, pageSize);
		return listToVoList(systemMessages);
	}

	private List<SystemMessageVO> listToVoList(List<SystemMessage> list) {
		List<SystemMessageVO> voList = new ArrayList<SystemMessageVO>();
		for (SystemMessage entity : list) {
			SystemMessageVO vo = entity.toVO();
			Integer userId = entity.getFromUserId();
			if (userId != null) {
				User user = userService.getById(userId);
				vo.setFromUserNickname(user.getNickname());
				vo.setFromUserAvatar(user.getAvatar());
			}
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 获取指定用户的未读系统消息列表
	 */
	@RequestMapping(value = "/sysmsglist/unread", method = RequestMethod.GET)
	public List<SystemMessageVO> listUnreadByUserId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");
		List<SystemMessage> systemMessages = systemMessageService.listUnreadByUserId(userId, pageNumber, pageSize);
		return listToVoList(systemMessages);
	}

	/**
	 * 获取指定用户的未读系统消息数
	 */
	@RequestMapping(value = "/sysmsgnum/unread", method = RequestMethod.GET)
	public int getUnreadNumberByUserId(Integer userId) {
		Assert.notNull(userId, "用户ID不能为空");
		return systemMessageService.getUnreadNumberByUserId(userId);
	}

}
