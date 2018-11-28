/**
 * BusinessException.java
 * Wangyong
 * 2011-8-26 上午09:53:29
 */
package net.ytoec.kernel.exception;

/**
 * 公共自定义异常类，扩展unchecked异常。
 * @author Wangyong
 * @2011-8-26
 * net.ytoec.kernel.exception
 */
public class BusinessException extends RuntimeException {

	/**
	 * BusinessException.java
	 * Wangyong
	 * 上午09:55:41
	 * 2011-8-26
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(createFriendlyMsg(message), cause);
	}

	public BusinessException(String message) {
		super(createFriendlyMsg(message));
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 创建页面友好异常信息提示
	 * @param msg
	 * @return
	 */
	private static String createFriendlyMsg(String msg){
		String prefix = "抱歉，";
        String suffix = "请稍后再试或与易通工作人员联系。";
		StringBuffer friendlyStr = new StringBuffer("");
		friendlyStr.append(prefix).append(msg);
		return friendlyStr.toString();
	}

}
