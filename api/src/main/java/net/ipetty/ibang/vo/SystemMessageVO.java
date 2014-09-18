package net.ipetty.ibang.vo;

import java.util.Date;

/**
 * 系统消息
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class SystemMessageVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -1884321555326238001L;

	private Long id; // 非业务主键
	private Integer fromUserId; // 消息来源用户ID
	private Integer receiverId; // 消息接收者用户ID
	private String type; // 消息类型
	private String content; // 消息内容
	private Date createdAt; // 消息创建时间

	public SystemMessageVO() {
		super();
	}

	public SystemMessageVO(Long id, Integer fromUserId, Integer receiverId, String type, String content, Date createdAt) {
		super();
		this.id = id;
		this.fromUserId = fromUserId;
		this.receiverId = receiverId;
		this.type = type;
		this.content = content;
		this.createdAt = createdAt;
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
