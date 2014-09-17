package net.ipetty.ibang.model;

import java.util.Date;

/**
 * 应征
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class Offer extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -595839011713341379L;

	private Long id; // 非业务主键
	private String sn; // 应征单流水号
	private Integer offererId; // 应征者ID
	private Long seekId; // 应征的求助单ID
	private String content; // 应征内容
	private String description; // 应征描述
	private Date deadline; // 应征者承诺完成委托的截止日期
	private Date createdAt; // 应征单创建日期
	private int status; // 状态

	public Offer() {
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

	public Integer getOffererId() {
		return offererId;
	}

	public void setOffererId(Integer offererId) {
		this.offererId = offererId;
	}

	public Long getSeekId() {
		return seekId;
	}

	public void setSeekId(Long seekId) {
		this.seekId = seekId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
