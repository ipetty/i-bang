package net.ipetty.ibang.model;

import java.io.Serializable;

/**
 * 用户作为求助者身份的相应信息
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class SeekerInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 2016006370693751462L;

	private Integer userId; // 用户ID
	private int seekCount; // 总的完成求助次数
	private int totalPoint; // 作为求助者身份的总积分
	private String title; // 作为求助者身份获得的头衔

	public SeekerInfo() {
		super();
	}

	public SeekerInfo(Integer userId, int seekCount, int totalPoint, String title) {
		super();
		this.userId = userId;
		this.seekCount = seekCount;
		this.totalPoint = totalPoint;
		this.title = title;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getSeekCount() {
		return seekCount;
	}

	public void setSeekCount(int seekCount) {
		this.seekCount = seekCount;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
