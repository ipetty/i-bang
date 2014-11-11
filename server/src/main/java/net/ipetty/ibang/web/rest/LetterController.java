package net.ipetty.ibang.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Letter;
import net.ipetty.ibang.service.LetterService;
import net.ipetty.ibang.vo.LetterContacts;
import net.ipetty.ibang.vo.LetterVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月10日
 */
@Controller
public class LetterController extends BaseController {

	@Resource
	private LetterService letterService;

	/**
	 * 发送站内信
	 */
	@RequestMapping(value = "/letter/send", method = RequestMethod.POST)
	public LetterVO send(Integer toUserId, String content) {
		Assert.notNull(toUserId, "联系人ID不能为空");
		Assert.hasText(content, "站内信内容不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能发送站内信");
		}
		if (currentUser.getId().equals(toUserId)) {
			throw new RestException("不能给自己发送站内信");
		}

		Letter letter = letterService.send(currentUser.getId(), toUserId, content);
		return letter.toVO();
	}

	/**
	 * 查看联系人列表
	 */
	@RequestMapping(value = "/letter/contacts", method = RequestMethod.GET)
	public List<LetterContacts> listContacts(int pageNumber, int pageSize) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能查看站内信");
		}

		return letterService.listContactsByUserId(currentUser.getId(), pageNumber, pageSize);
	}

	/**
	 * 查看与某个联系人的站内信列表
	 */
	@RequestMapping(value = "/letters", method = RequestMethod.GET)
	public List<LetterVO> list(Integer contactUserId, int pageNumber, int pageSize) {
		Assert.notNull(contactUserId, "联系人ID不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能查看站内信");
		}

		List<Letter> letters = letterService.listByUserIdAndContactUserId(currentUser.getId(), contactUserId,
				pageNumber, pageSize);
		return Letter.listToVoList(letters);
	}

	/**
	 * 获取指定用户的未读站内信数
	 */
	@RequestMapping(value = "/letter/unreadnum", method = RequestMethod.GET)
	public int getUnreadNumber() {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			return 0;
		}

		return letterService.getUnreadNumberByUserId(currentUser.getId());
	}

}
