package net.ipetty.ibang.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 所有实体的基类
 * @author luocanfeng
 * @date 2014年9月16日
 */
public abstract class AbstractEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3176473038059278269L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
