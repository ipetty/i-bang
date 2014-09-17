package net.ipetty.ibang.model;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.vo.SeekCategory;

/**
 * 用户作为帮助者身份的相应信息
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class OffererInfo {

	private Integer userId; // 用户ID
	private int offerCount; // 总的完成帮助次数
	private int totalPoint; // 作为帮助者身份的总积分
	private String title; // 作为帮助者身份获得的头衔
	private List<SeekCategory> offerRange = new ArrayList<SeekCategory>(); // 帮忙范围

	public OffererInfo() {
		super();
	}

	public OffererInfo(Integer userId, int offerCount, int totalPoint, String title) {
		super();
		this.userId = userId;
		this.offerCount = offerCount;
		this.totalPoint = totalPoint;
		this.title = title;
	}

	public OffererInfo(Integer userId, int offerCount, int totalPoint, String title, List<SeekCategory> offerRange) {
		super();
		this.userId = userId;
		this.offerCount = offerCount;
		this.totalPoint = totalPoint;
		this.title = title;
		this.offerRange = offerRange;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(int offerCount) {
		this.offerCount = offerCount;
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

	public List<SeekCategory> getOfferRange() {
		return offerRange;
	}

	public void setOfferRange(List<SeekCategory> offerRange) {
		this.offerRange = offerRange;
	}

}
