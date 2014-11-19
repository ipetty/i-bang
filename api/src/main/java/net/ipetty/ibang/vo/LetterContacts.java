package net.ipetty.ibang.vo;

/**
 * 站内信联系人
 * @author luocanfeng
 * @date 2014年11月7日
 */
public class LetterContacts extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -5490871107502710908L;

	private Integer userId; // 联系人ID
	private String userNickname; // 联系人昵称
	private String userAvatar; // 联系人头像
	private String latestContent; // 最后一次交谈内容
	private boolean read; // 是否已读所有沟通内容
	private int unreadNumber; // 未读站内信条数

	public LetterContacts() {
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

	public String getLatestContent() {
		return latestContent;
	}

	public void setLatestContent(String latestContent) {
		this.latestContent = latestContent;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public int getUnreadNumber() {
		return unreadNumber;
	}

	public void setUnreadNumber(int unreadNumber) {
		this.unreadNumber = unreadNumber;
	}

}
