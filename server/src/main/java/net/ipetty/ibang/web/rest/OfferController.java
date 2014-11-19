package net.ipetty.ibang.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.OfferService;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * OfferController
 * @author luocanfeng
 * @date 2014年10月14日
 */
@Controller
public class OfferController extends BaseController {

	@Resource
	private OfferService offerService;

	@Resource
	private UserService userService;

	/**
	 * 应征
	 */
	@RequestMapping(value = "/offer", method = RequestMethod.POST)
	public OfferVO offer(@RequestBody OfferVO offer) {
		Assert.notNull(offer, "应征单不能为空");
		Assert.notNull(offer.getSeekId(), "所应征的求助单不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能发起应征");
		}

		// 设置应征者
		offer.setOffererId(currentUser.getId());

		Offer o = Offer.fromVO(offer);
		offerService.offer(o);
		o = offerService.getById(o.getId());

		return o.toVO();
	}

	/**
	 * 获取
	 */
	@RequestMapping(value = "/offer", method = RequestMethod.GET)
	public OfferVO getById(Long id) {
		Assert.notNull(id, "应征单ID不能为空");

		return offerService.getById(id).toVO();
	}

	/**
	 * 获取指定求助单的应征单列表
	 */
	@RequestMapping(value = "/offerlist/byseek", method = RequestMethod.GET)
	public List<OfferVO> listBySeekId(Long seekId) {
		Assert.notNull(seekId, "应征单ID不能为空");

		List<Offer> offers = offerService.listBySeekId(seekId);
		return listToVoList(offers);
	}

	private List<OfferVO> listToVoList(List<Offer> list) {
		List<OfferVO> voList = new ArrayList<OfferVO>();
		for (Offer entity : list) {
			OfferVO vo = entity.toVO();
			Integer userId = entity.getOffererId();
			if (userId != null) {
				User user = userService.getById(userId);
				vo.setOffererNickname(user.getNickname());
				vo.setOffererAvatar(user.getAvatar());
			}
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 获取指定用户的应征列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@RequestMapping(value = "/offerlist/byuser", method = RequestMethod.GET)
	public List<OfferVO> listByUserId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Offer> offers = offerService.listByUserId(userId, pageNumber, pageSize);
		return listToVoList(offers);
	}

	/**
	 * 关闭应征
	 */
	@RequestMapping(value = "/offer/close", method = RequestMethod.POST)
	public boolean close(Long offerId) {
		Assert.notNull(offerId, "应征单ID不能为空");

		offerService.close(offerId);
		return true;
	}

}
