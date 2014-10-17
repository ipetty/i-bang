package net.ipetty.ibang.web.rest;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.context.SpringContextHelper;
import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Image;
import net.ipetty.ibang.service.ImageService;
import net.ipetty.ibang.util.ImageUtils;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.web.rest.exception.RestException;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * ImageController
 * 
 * @author luocanfeng
 * @date 2014年10月17日
 */
@Controller
public class ImageController extends BaseController {

	@Resource
	private ImageService imageService;

	/**
	 * 上传图片文件并返回图片对象VO
	 */
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	public ImageVO upload(@RequestBody MultipartFile imageFile) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("登录后才能发布图片");
		}

		try {
			Image image = ImageUtils.saveImage(imageFile, SpringContextHelper.getWebContextRealPath());
			imageService.save(image);
			return image.toVO();
		} catch (IOException e) {
			throw new RestException("保存图片时磁盘写入失败");
		}
	}

	/**
	 * 获取
	 */
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public ImageVO getById(Long id) {
		Assert.notNull(id, "图片ID不能为空");

		Image image = imageService.getById(id);
		return image == null ? null : image.toVO();
	}

	/**
	 * 获取指定求助单的图片列表
	 */
	@RequestMapping(value = "/imagelist/byseek", method = RequestMethod.GET)
	public List<ImageVO> listBySeekId(Long seekId) {
		Assert.notNull(seekId, "求助单ID不能为空");

		return Image.listToVoList(imageService.listBySeekId(seekId));
	}

}
