package net.ipetty.ibang.model;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.vo.ImageVO;

import org.springframework.beans.BeanUtils;

/**
 * 图片
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class Image extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8112495603277406732L;

	private Long id; // 非业务主键
	private String sn; // 图片随机串号
	private String smallUrl; // 缩略图URL
	private String originalUrl; // 原图URL

	public Image() {
		super();
	}

	public Image(Long id, String sn, String smallUrl, String originalUrl) {
		super();
		this.id = id;
		this.sn = sn;
		this.smallUrl = smallUrl;
		this.originalUrl = originalUrl;
	}

	public ImageVO toVO() {
		ImageVO vo = new ImageVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Image fromVO(ImageVO vo) {
		Image entity = new Image();
		BeanUtils.copyProperties(vo, entity);
		return entity;
	}

	public static List<ImageVO> listToVoList(List<Image> imageList) {
		List<ImageVO> voList = new ArrayList<ImageVO>();
		for (Image image : imageList) {
			voList.add(image.toVO());
		}
		return voList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

}
