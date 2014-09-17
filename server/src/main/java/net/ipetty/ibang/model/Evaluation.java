package net.ipetty.ibang.model;

import java.util.Date;

/**
 * 评价
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class Evaluation extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 3181415747184715319L;

	private Long id; // 非业务主键
	private Long delegationId; // 委托单ID
	private String type; // 评价类型，针对求助者的评价/针对应征者的评价
	private Integer evaluatorId; // 评价发起人
	private Integer evaluationObjectId; // 评价对象人
	private int point; // 评分
	private String content; // 评价内容
	private Date createdAt; // 评价日期

	public Evaluation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDelegationId() {
		return delegationId;
	}

	public void setDelegationId(Long delegationId) {
		this.delegationId = delegationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getEvaluatorId() {
		return evaluatorId;
	}

	public void setEvaluatorId(Integer evaluatorId) {
		this.evaluatorId = evaluatorId;
	}

	public Integer getEvaluationObjectId() {
		return evaluationObjectId;
	}

	public void setEvaluationObjectId(Integer evaluationObjectId) {
		this.evaluationObjectId = evaluationObjectId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
