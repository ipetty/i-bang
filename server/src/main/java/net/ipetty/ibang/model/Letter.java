package net.ipetty.ibang.model;

import java.util.Date;

import net.ipetty.ibang.vo.LetterVO;

import org.springframework.beans.BeanUtils;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月6日
 */
public class Letter extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2914953911728112026L;

	private Long id; // 非业务主键
	private Integer ownerId; // 此站内信的所属人（在谁的收件箱内）
	private Integer cooperatorId; // 沟通对象
	private String content; // 消息内容
	private boolean inbox; // 我收到/发出的信件，true为收到的，false为发出的
	private boolean read; // 是否已读
	private Date createdOn; // 消息创建时间
	private Date readOn; // 查收的时间

	public Letter() {
		super();
	}

	public LetterVO toVO() {
		LetterVO vo = new LetterVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Letter fromVO(LetterVO vo) {
		Letter entity = new Letter();
		BeanUtils.copyProperties(vo, entity);
		return entity;
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

	public Date getReadOn() {
		return readOn;
	}

	public void setReadOn(Date readOn) {
		this.readOn = readOn;
	}

}
