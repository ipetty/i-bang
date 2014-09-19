package net.ipetty.ibang.exception;

/**
 * BusinessException
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class BusinessException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = -6215538977473686917L;

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
