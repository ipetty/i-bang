package net.ipetty.ibang.model;

import java.util.Date;

import net.ipetty.ibang.vo.ReportVO;

import org.springframework.beans.BeanUtils;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月13日
 */
public class Report extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -6794670638928552333L;

	private Long id; // 非业务主键
	private String sn; // 委托单流水号
	private String type; // 举报内容的类型，求助（包含帮忙，通过seekType区分） | 应征 | 人
	private Long seekId; // 举报的求助单ID
	private String seekType; // 类型，求助 | 帮忙
	private Long offerId; // 举报的应征单ID
	private Integer userId; // 要举报的人
	private String reportContent; // 要举报的内容
	private String behave; // 举报行为类型，垃圾营销 | 小广告 | 虚假信息 | 抄袭 | 敏感信息
	private String content; // 举报备注内容
	private Integer reporterId; // 举报人
	private Date createdOn; // 举报时间
	private String result; // 处理结果，严惩不贷 | 警告 | 暂不处理 | 虚假举报 | 恶意举报
	private Date dealedOn; // 受理时间

	public Report() {
		super();
	}

	public ReportVO toVO() {
		ReportVO vo = new ReportVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Report fromVO(ReportVO vo) {
		Report entity = new Report();
		BeanUtils.copyProperties(vo, entity);
		return entity;
	}

	// public static List<ReportVO> listToVoList(List<Report> list) {
	// List<ReportVO> voList = new ArrayList<ReportVO>();
	// for (Report entity : list) {
	// voList.add(entity.toVO());
	// }
	// return voList;
	// }

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSeekId() {
		return seekId;
	}

	public void setSeekId(Long seekId) {
		this.seekId = seekId;
	}

	public String getSeekType() {
		return seekType;
	}

	public void setSeekType(String seekType) {
		this.seekType = seekType;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public String getBehave() {
		return behave;
	}

	public void setBehave(String behave) {
		this.behave = behave;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getReporterId() {
		return reporterId;
	}

	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getDealedOn() {
		return dealedOn;
	}

	public void setDealedOn(Date dealedOn) {
		this.dealedOn = dealedOn;
	}

}
