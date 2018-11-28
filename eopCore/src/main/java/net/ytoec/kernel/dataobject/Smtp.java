package net.ytoec.kernel.dataobject;

/**
 * 邮件服务器参数对象
 * 
 * @author ChenRen
 * @Date 2011-08-10
 */
public class Smtp {

	/** 发送邮件的统一帐号 */
	private String account;
	/** 帐号密码 */
	private String password;
	/** 邮件服务器地址 */
	private String host;
	/**
	 * 设置smtp身份认证<br>
	 * 根据这下面的话定义的变量; auth = true; 发信才能通过验证 props.put("mail.smtp.auth", "true");
	 */
	private String auth = "true";
	/** 端口号 */
	private String port;

	/* === constructors === */

	public Smtp() {
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
