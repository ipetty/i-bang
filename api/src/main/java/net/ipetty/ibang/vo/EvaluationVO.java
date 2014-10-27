package net.ipetty.ibang.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评价
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class EvaluationVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 1724927813581572552L;

	private Long id; // 非业务主键
	private Long delegationId; // 委托单ID
	private String type; // 评价类型，针对求助者的评价/针对应征者的评价
	private Integer evaluatorId; // 评价发起人
	private Integer evaluateTargetId; // 评价对象人
	private int point; // 评分
	private String content; // 评价内容
	private List<ImageVO> images = new ArrayList<ImageVO>(); // 图片
	private Date createdOn; // 评价日期

	public EvaluationVO() {
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

	public Integer getEvaluateTargetId() {
		return evaluateTargetId;
	}

	public void setEvaluateTargetId(Integer evaluateTargetId) {
		this.evaluateTargetId = evaluateTargetId;
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

	public List<ImageVO> getImages() {
		return images;
	}

	public void setImages(List<ImageVO> images) {
		this.images = images;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
