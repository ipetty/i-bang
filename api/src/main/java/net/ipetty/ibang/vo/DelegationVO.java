package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * 委托
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class DelegationVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 8689978142647812958L;

	private Long id; // 非业务主键
	private String sn; // 委托单流水号
	private Long seekId; // 求助单ID
	private Integer seekerId; // 求助者ID
	private UserVO seeker;
	private Long offerId; // 应征单ID
	private Integer offererId; // 应征者ID
	private UserVO offerer;
	private Date deadline; // 应征者承诺完成委托的截止日期
	private Date createdOn; // 委托单创建日期
	private Date closedOn; // 关闭时间
	private String status; // 状态

	public DelegationVO() {
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

	public Long getSeekId() {
		return seekId;
	}

	public void setSeekId(Long seekId) {
		this.seekId = seekId;
	}

	public Integer getSeekerId() {
		return seekerId;
	}

	public void setSeekerId(Integer seekerId) {
		this.seekerId = seekerId;
	}

	public UserVO getSeeker() {
		return seeker;
	}

	public void setSeeker(UserVO seeker) {
		this.seeker = seeker;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public Integer getOffererId() {
		return offererId;
	}

	public void setOffererId(Integer offererId) {
		this.offererId = offererId;
	}

	public UserVO getOfferer() {
		return offerer;
	}

	public void setOfferer(UserVO offerer) {
		this.offerer = offerer;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
