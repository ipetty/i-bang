package net.ipetty.ibang.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 求助分类
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class SeekCategory extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -4022674935009452591L;

	private String categoryL1; // 一级分类
	private String categoryL2; // 二级分类

	public SeekCategory() {
		super();
	}

	public SeekCategory(String categoryL1, String categoryL2) {
		super();
		this.categoryL1 = categoryL1;
		this.categoryL2 = categoryL2;
	}

	public String getCategoryL1() {
		return categoryL1;
	}

	public void setCategoryL1(String categoryL1) {
		this.categoryL1 = categoryL1;
	}

	public String getCategoryL2() {
		return categoryL2;
	}

	public void setCategoryL2(String categoryL2) {
		this.categoryL2 = categoryL2;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	public static void main(String[] args) {
		SeekCategory s1 = new SeekCategory("AAA", "aaa");
		SeekCategory s2 = new SeekCategory("AAA", "bbb");
		SeekCategory s3 = new SeekCategory("AAA", "bbb");
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());
		System.out.println(s3.hashCode());
		System.out.println(s1.equals(s2));
		System.out.println(s2.equals(s3));
	}

}
