package net.ipetty.ibang.model;

import java.util.Date;

import net.ipetty.ibang.vo.SystemMessageVO;

import org.springframework.beans.BeanUtils;

/**
 * 系统消息
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class SystemMessage extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -8994626162111085279L;

	private Long id; // 非业务主键
	private Integer fromUserId; // 消息来源用户ID
	private Integer receiverId; // 消息接收者用户ID
	private String type; // 消息类型
	private String content; // 消息内容
	private Date createdOn; // 消息创建时间

	public SystemMessage() {
		super();
	}

	public SystemMessage(Long id, Integer fromUserId, Integer receiverId, String type, String content, Date createdOn) {
		super();
		this.id = id;
		this.fromUserId = fromUserId;
		this.receiverId = receiverId;
		this.type = type;
		this.content = content;
		this.createdOn = createdOn;
	}

	public SystemMessageVO toVO() {
		SystemMessageVO vo = new SystemMessageVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static SystemMessage fromVO(SystemMessageVO vo) {
		SystemMessage entity = new SystemMessage();
		BeanUtils.copyProperties(vo, entity);
		return entity;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
