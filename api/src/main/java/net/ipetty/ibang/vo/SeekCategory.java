package net.ipetty.ibang.vo;

/**
 * 求助分类
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class SeekCategory extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -4022674935009452591L;

	private int level; // 分类级别，目前总共两级
	private String category; // 分类名称
	private SeekCategory parent; // 父分类

	public SeekCategory() {
		super();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public SeekCategory getParent() {
		return parent;
	}

	public void setParent(SeekCategory parent) {
		this.parent = parent;
	}

}
