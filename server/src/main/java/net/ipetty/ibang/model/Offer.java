package net.ipetty.ibang.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.vo.OfferVO;

import org.springframework.beans.BeanUtils;

/**
 * 应征
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
	private Date createdOn; // 应征单创建日期
	private Date closedOn; // 关闭时间
	private String status; // 状态
	private Delegation delegation; // 对应的委托单，如果不存在则为null
	private boolean enable = true; // 是否有效

	public Offer() {
		super();
	}

	public OfferVO toVO() {
		OfferVO vo = new OfferVO();
		BeanUtils.copyProperties(this, vo, "delegation");
		if (delegation != null) {
			vo.setDelegation(delegation.toVO());
		}
		return vo;
	}

	public static Offer fromVO(OfferVO vo) {
		Offer entity = new Offer();
		BeanUtils.copyProperties(vo, entity, "delegation");
		if (vo.getDelegation() != null) {
			entity.setDelegation(Delegation.fromVO(vo.getDelegation()));
		}
		return entity;
	}

	public static List<OfferVO> listToVoList(List<Offer> offerList) {
		List<OfferVO> voList = new ArrayList<OfferVO>();
		for (Offer offer : offerList) {
			voList.add(offer.toVO());
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

	public Delegation getDelegation() {
		return delegation;
	}

	public void setDelegation(Delegation delegation) {
		this.delegation = delegation;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
