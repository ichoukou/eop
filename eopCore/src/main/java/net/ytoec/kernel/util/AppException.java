package net.ytoec.kernel.util;

/**
 * 系统异常
 */

public class AppException extends RuntimeException {
	/**
	 * 系统异常的类型。
	 */
	/**
	 * 错误类
	 */
	public static final String ERROR = "ERROR";
	/**
	 * 警告类异常
	 */
	public static final String CONFIRM = "CONFIRM";
	/**
	 * 提示类异常
	 */
	public static final String INFO = "INFO";

	/**
	 * 异常类型
	 */
	private String type;
	/**
	 * 发生异常之后的URL
	 */
	private String[] targetUrl;
	/**
	 * 发生异常之后的URL
	 */
	private String nextTarget;
	
	private long msgCode;

	public long getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(long msgCode) {
		this.msgCode = msgCode;
	}

	public AppException(long msgCode, String message) {
		super(message);
		this.msgCode = msgCode;
	}

	public AppException(long msgCode, String message, String nextTarget) {
		super(message);
		this.msgCode = msgCode;
		this.nextTarget = nextTarget;
	}

	public AppException(Exception e) {
		super(e);
	}
	public AppException(Throwable e) {
		super(e);
	}

	public AppException(String type, String msg, String[] targetUrl) {
		super(msg);
		this.type = type;
		this.targetUrl = targetUrl;
	}

	public String[] getTargetUrl() {
		return targetUrl;
	}

	public String getType() {
		return type;
	}

	public void setTargetUrl(String[] strings) {
		targetUrl = strings;
	}

	public void setType(String string) {
		type = string;
	}

	public String getNextTarget() {
		return nextTarget;
	}

	public void setNextTarget(String nextTarget) {
		this.nextTarget = nextTarget;
	}
}
