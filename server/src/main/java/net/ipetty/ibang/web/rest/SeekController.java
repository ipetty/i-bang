package net.ipetty.ibang.web.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.service.SeekService;
import net.ipetty.ibang.util.DateUtils;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SeekController
 * 
 * @author luocanfeng
 * @date 2014年10月10日
 */
@Controller
public class SeekController extends BaseController {

	@Resource
	private SeekService seekService;

	/**
	 * 发布求助
	 */
	@RequestMapping(value = "/seek/publish", method = RequestMethod.POST)
	public SeekVO publish(@RequestBody SeekVO seek) {
		Assert.notNull(seek, "求助单不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("用户登录后才能发布求助");
		}

		// 设置求助人
		seek.setSeekerId(currentUser.getId());

		Seek s = Seek.fromVO(seek);
		seekService.publish(s);
		s = seekService.getById(s.getId());

		return s.toVO();
	}

	/**
	 * 获取
	 */
	@RequestMapping(value = "/seek", method = RequestMethod.GET)
	public SeekVO getById(Long id) {
		Assert.notNull(id, "求助单ID不能为空");

		return seekService.getById(id).toVO();
	}

	/**
	 * 获取最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/seeklist/latest", method = RequestMethod.GET)
	public List<SeekVO> listLatest(String timeline, int pageNumber, int pageSize) {
		Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
		List<Seek> seeks = seekService.listLatest(date, pageNumber, pageSize);
		return Seek.listToVoList(seeks);
	}

	/**
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/seeklist/byuser", method = RequestMethod.GET)
	public List<SeekVO> listByUserId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Seek> seeks = seekService.listByUserId(userId, pageNumber, pageSize);
		return Seek.listToVoList(seeks);
	}

	/**
	 * 关闭求助
	 */
	@RequestMapping(value = "/seek/close", method = RequestMethod.POST)
	public boolean close(Long seekId) {
		Assert.notNull(seekId, "求助单ID不能为空");

		seekService.close(seekId);
		return true;
	}

}
