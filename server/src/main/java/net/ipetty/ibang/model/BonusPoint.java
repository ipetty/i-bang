package net.ipetty.ibang.model;

import java.util.Date;

/**
 * 积分
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class BonusPoint extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2514740851485542770L;

	private Long id; // 非业务主键
	private Integer userId; // 积分所属用户ID
	private Long delegationId; // 积分来自的委托ID
	private Long evaluationId; // 积分来自的评价ID
	private Date gainedAt; // 积分获取时间

	public BonusPoint() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getDelegationId() {
		return delegationId;
	}

	public void setDelegationId(Long delegationId) {
		this.delegationId = delegationId;
	}

	public Long getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(Long evaluationId) {
		this.evaluationId = evaluationId;
	}

	public Date getGainedAt() {
		return gainedAt;
	}

	public void setGainedAt(Date gainedAt) {
		this.gainedAt = gainedAt;
	}

}
