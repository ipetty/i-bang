package net.ipetty.ibang.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * UserOfferRange
 * 
 * @author luocanfeng
 * @date 2014年10月28日
 */
public class UserOfferRange extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -3839233171476928965L;

	private Integer userId; // 用户ID
	private List<SeekCategory> offerRange = new ArrayList<SeekCategory>(); // 帮忙范围

	public UserOfferRange() {
		super();
	}

	public UserOfferRange(Integer userId, List<SeekCategory> offerRange) {
		super();
		this.userId = userId;
		this.offerRange = offerRange;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<SeekCategory> getOfferRange() {
		return offerRange;
	}

	public void setOfferRange(List<SeekCategory> offerRange) {
		this.offerRange = offerRange;
	}

}
