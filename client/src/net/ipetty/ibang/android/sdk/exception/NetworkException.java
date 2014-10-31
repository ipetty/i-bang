package net.ipetty.ibang.android.sdk.exception;

/**
 * 服务不可用
 *
 */
public class NetworkException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 15055364153750405L;

	public NetworkException() {
		super();
	}

	public NetworkException(String message) {
		super(message);
	}

	public NetworkException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkException(Throwable cause) {
		super(cause);
	}

}
