package net.ipetty.ibang.vo;

/**
 * 图片
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class ImageVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 2834390400762995623L;

	private Long id; // 非业务主键
	private String sn; // 图片随机串号
	private String smallUrl; // 缩略图URL
	private String originalUrl; // 原图URL

	public ImageVO() {
		super();
	}

	public ImageVO(Long id, String sn, String smallUrl, String originalUrl) {
		super();
		this.id = id;
		this.sn = sn;
		this.smallUrl = smallUrl;
		this.originalUrl = originalUrl;
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
