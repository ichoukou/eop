package net.ytoec.kernel.dataobject;

/**
 * 客户营销统计
 * @author mabo
 *
 */
public class CoreEdm {

	private Integer id;
	//  1:etong发邮件
	private String sourceType;
	// 客户邮箱
	private String email;
	//客户ip
	private String ip;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
