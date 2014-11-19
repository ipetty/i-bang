package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * 身份审核
 * @author luocanfeng
 * @date 2014年11月3日
 */
public class IdentityVerificationVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 3568443716375286837L;

	private Integer userId; // 待审核身份的用户ID
	private String userNickname; // 待审核用户昵称
	private String userAvatar; // 待审核用户头像
	private String realName; // 真实姓名
	private String idNumber; // 身份证号
	private String idCardFront; // 身份证正面照
	private String idCardReverseSide; // 身份证反面照
	private String idCardInHand; // 手持身份证照
	private String description; // 提交审核描述信息
	private Date submittedOn; // 提交审核时间
	private Integer verifierId; // 审核人ID
	private String verifyInfo; // 审核信息
	private Date verifiedOn; // 审核时间
	private String status; // 审核状态，待审核 | 审核通过 | 审核未通过

	public IdentityVerificationVO() {
		super();
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardReverseSide() {
		return idCardReverseSide;
	}

	public void setIdCardReverseSide(String idCardReverseSide) {
		this.idCardReverseSide = idCardReverseSide;
	}

	public String getIdCardInHand() {
		return idCardInHand;
	}

	public void setIdCardInHand(String idCardInHand) {
		this.idCardInHand = idCardInHand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public Integer getVerifierId() {
		return verifierId;
	}

	public void setVerifierId(Integer verifierId) {
		this.verifierId = verifierId;
	}

	public String getVerifyInfo() {
		return verifyInfo;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}

	public Date getVerifiedOn() {
		return verifiedOn;
	}

	public void setVerifiedOn(Date verifiedOn) {
		this.verifiedOn = verifiedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
