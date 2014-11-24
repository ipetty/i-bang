package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月13日
 */
public class ReportVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 2554640406857061597L;

	private Long id; // 非业务主键
	private String sn; // 委托单流水号
	private String type; // 举报内容的类型，求助（包含帮忙，通过seekType区分） | 应征 | 人
	private Long seekId; // 举报的求助单ID
	private String seekType; // 类型，求助 | 帮忙
	private Long offerId; // 举报的应征单ID
	private Integer userId; // 要举报的人
	private String userNickname; // 要举报人的昵称
	private String userAvatar; // 要举报人的头像
	private String reportContent; // 要举报的内容
	private String behave; // 举报行为类型，垃圾营销 | 小广告 | 虚假信息 | 抄袭 | 敏感信息
	private String content; // 举报备注内容
	private Integer reporterId; // 举报人
	private String reporterNickname; // 举报人的昵称
	private String reporterAvatar; // 举报人的头像
	private Date createdOn; // 举报时间
	private String result; // 处理结果，严惩不贷 | 警告 | 暂不处理 | 虚假举报 | 恶意举报
	private Date dealedOn; // 受理时间

	public ReportVO() {
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

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
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

	public String getReporterNickname() {
		return reporterNickname;
	}

	public void setReporterNickname(String reporterNickname) {
		this.reporterNickname = reporterNickname;
	}

	public String getReporterAvatar() {
		return reporterAvatar;
	}

	public void setReporterAvatar(String reporterAvatar) {
		this.reporterAvatar = reporterAvatar;
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
