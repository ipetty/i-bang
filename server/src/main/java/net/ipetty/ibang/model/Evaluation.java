package net.ipetty.ibang.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.vo.EvaluationVO;

import org.springframework.beans.BeanUtils;

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
	private Integer evaluateTargetId; // 评价对象人
	private int point; // 评分
	private String content; // 评价内容
	private Date createdOn; // 评价日期

	public Evaluation() {
		super();
	}

	public EvaluationVO toVO() {
		EvaluationVO vo = new EvaluationVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Evaluation fromVO(EvaluationVO vo) {
		Evaluation entity = new Evaluation();
		BeanUtils.copyProperties(vo, entity);
		return entity;
	}

	public static List<EvaluationVO> listToVoList(List<Evaluation> evaluationList) {
		List<EvaluationVO> voList = new ArrayList<EvaluationVO>();
		for (Evaluation evaluation : evaluationList) {
			voList.add(evaluation.toVO());
		}
		return voList;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
