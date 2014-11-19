package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * LetterVO
 * @author luocanfeng
 * @date 2014年11月7日
 */
public class LetterVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 398424027915735640L;

	private Long id; // 非业务主键
	private Integer ownerId; // 此站内信的所属人（在谁的收件箱内）
	private Integer cooperatorId; // 沟通对象
	private String cooperatorNickname; // 沟通对象昵称
	private String cooperatorAvatar; // 沟通对象头像
	private String content; // 消息内容
	private boolean inbox; // 我收到/发出的信件，true为收到的，false为发出的
	private Date createdOn; // 消息创建时间

	public LetterVO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getCooperatorId() {
		return cooperatorId;
	}

	public void setCooperatorId(Integer cooperatorId) {
		this.cooperatorId = cooperatorId;
	}

	public String getCooperatorNickname() {
		return cooperatorNickname;
	}

	public void setCooperatorNickname(String cooperatorNickname) {
		this.cooperatorNickname = cooperatorNickname;
	}

	public String getCooperatorAvatar() {
		return cooperatorAvatar;
	}

	public void setCooperatorAvatar(String cooperatorAvatar) {
		this.cooperatorAvatar = cooperatorAvatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isInbox() {
		return inbox;
	}

	public void setInbox(boolean inbox) {
		this.inbox = inbox;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
