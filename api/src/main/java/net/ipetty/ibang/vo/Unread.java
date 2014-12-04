package net.ipetty.ibang.vo;

/**
 * Unread
 * @author luocanfeng
 * @date 2014年12月3日
 */
public class Unread extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 6483504406396454354L;

	private int messageNum = 0; // 未读系统消息数
	private int letterNum = 0; // 未读站内信数

	public Unread() {
		super();
	}

	public Unread(int messageNum, int letterNum) {
		super();
		this.messageNum = messageNum;
		this.letterNum = letterNum;
	}

	public int getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}

	public int getLetterNum() {
		return letterNum;
	}

	public void setLetterNum(int letterNum) {
		this.letterNum = letterNum;
	}

}
