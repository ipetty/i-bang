package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * 系统消息
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class SystemMessageVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -1884321555326238001L;

	private Long id; // 非业务主键
	private Integer fromUserId; // 消息来源用户ID
	private String fromUserNickname; // 求助者昵称
	private String fromUserAvatar; // 求助者头像
	private Integer receiverId; // 消息接收者用户ID
	private String type; // 消息类型
	private Long seekId; // 对应的求助单ID
	private Long offerId; // 对应的应征单ID
	private Long delegationId; // 对应的委托单ID
	private Long evaluationId; // 对应的评价ID
	private String title; // 消息标题
	private String content; // 消息内容
	private boolean read; // 是否已读
	private Date createdOn; // 消息创建时间

	public SystemMessageVO() {
		super();
	}

	public SystemMessageVO(Long id, Integer fromUserId, Integer receiverId, String type, String content, Date createdOn) {
		super();
		this.id = id;
		this.fromUserId = fromUserId;
		this.receiverId = receiverId;
		this.type = type;
		this.content = content;
		this.createdOn = createdOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserNickname() {
		return fromUserNickname;
	}

	public void setFromUserNickname(String fromUserNickname) {
		this.fromUserNickname = fromUserNickname;
	}

	public String getFromUserAvatar() {
		return fromUserAvatar;
	}

	public void setFromUserAvatar(String fromUserAvatar) {
		this.fromUserAvatar = fromUserAvatar;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
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

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
