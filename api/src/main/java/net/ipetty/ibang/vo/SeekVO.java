package net.ipetty.ibang.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 求助
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class SeekVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -3457713874329718078L;

	private Long id; // 非业务主键
	private String sn; // 求助单流水号
	private Integer seekerId; // 求助者ID
	private boolean contactInfoVisible; // 是否公开求助者联系信息，默认隐藏
	private String categoryL1; // 一级分类
	private String categoryL2; // 二级分类
	private String content; // 求助内容
	private List<ImageVO> images = new ArrayList<ImageVO>(); // 图片
	private String requirement; // 要求
	private int delegateNumber; // 委托数量
	private String reward; // 奖励
	private String additionalReward; // 附加奖励
	private Date createdOn; // 求助创建时间
	private Date exipireDate; // 求助有效日期
	private int status; // 状态

	public SeekVO() {
		super();
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

	public Integer getSeekerId() {
		return seekerId;
	}

	public void setSeekerId(Integer seekerId) {
		this.seekerId = seekerId;
	}

	public boolean isContactInfoVisible() {
		return contactInfoVisible;
	}

	public void setContactInfoVisible(boolean contactInfoVisible) {
		this.contactInfoVisible = contactInfoVisible;
	}

	public String getCategoryL1() {
		return categoryL1;
	}

	public void setCategoryL1(String categoryL1) {
		this.categoryL1 = categoryL1;
	}

	public String getCategoryL2() {
		return categoryL2;
	}

	public void setCategoryL2(String categoryL2) {
		this.categoryL2 = categoryL2;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ImageVO> getImages() {
		return images;
	}

	public void setImages(List<ImageVO> images) {
		this.images = images;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public int getDelegateNumber() {
		return delegateNumber;
	}

	public void setDelegateNumber(int delegateNumber) {
		this.delegateNumber = delegateNumber;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getAdditionalReward() {
		return additionalReward;
	}

	public void setAdditionalReward(String additionalReward) {
		this.additionalReward = additionalReward;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getExipireDate() {
		return exipireDate;
	}

	public void setExipireDate(Date exipireDate) {
		this.exipireDate = exipireDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
