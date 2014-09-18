package net.ipetty.ibang.model;

import java.util.Date;

import net.ipetty.ibang.vo.DelegationVO;

import org.springframework.beans.BeanUtils;

/**
 * 委托
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class Delegation extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -7220234745932488653L;

	private Long id; // 非业务主键
	private String sn; // 委托单流水号
	private Long seekId; // 求助单ID
	private Integer seekerId; // 求助者ID
	private Long offerId; // 应征单ID
	private Integer offererId; // 应征者ID
	private Date deadline; // 应征者承诺完成委托的截止日期
	private Date createdOn; // 委托单创建日期
	private String status; // 状态

	public Delegation() {
		super();
	}

	public DelegationVO toVO() {
		DelegationVO vo = new DelegationVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Delegation fromVO(DelegationVO vo) {
		Delegation entity = new Delegation();
		BeanUtils.copyProperties(vo, entity);
		return entity;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
