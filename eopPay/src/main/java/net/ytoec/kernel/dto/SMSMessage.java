package net.ytoec.kernel.dto;


/**
 * 充值返回给短信的对象
 * @author guoliang.wang
 *
 * 
 */
public class SMSMessage {

	/**
	 * 可使用短信数	（累计购买短信量-累计发送量）
	 */
	private Integer smsUsecount;
	/**
	*开始日期
	*/
	private java.util.Date beginDate;
	/**
	*结束日期
	*/
	private java.util.Date endDate;
	public Integer getSmsUsecount() {
		if(smsUsecount==null){
			return 0;
		}
		return smsUsecount;
	}
	public void setSmsUsecount(Integer smsUsecount) {
		this.smsUsecount = smsUsecount;
	}
	public java.util.Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}	
	
	
}
