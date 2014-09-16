package net.ipetty.ibang.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BaseVO
 * 
 * @author luocanfeng
 * @date 2014年9月16日
 */
public abstract class BaseVO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -664483303236692590L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
