package net.ytoec.kernel.dto;

public class AlipayConfigMessage {

	    private String alipayPartner;//合作身份者ID，以2088开头由16位纯数字组成的字符串
	    private String alipayKey;//交易安全检验码，由数字和字母组成的32位字符串
	    private String alipaySellerEmail;//签约支付宝账号或卖家收款支付宝帐户
	    private String alipayRate;//支付宝费率[手续费=费率*交易额]
	    private String notifyUrl;//支付宝服务器通知的路径[必须保证能访问到,不带自定义参数]
	    private String returnUrl;//支付宝服务器异步通知路径[必须保证能访问到,不带自定义参数]
	    private String alipayInputCharset;//网站的编码方式
		public String getAlipayPartner() {
			return alipayPartner;
		}
		public void setAlipayPartner(String alipayPartner) {
			this.alipayPartner = alipayPartner;
		}
		public String getAlipayKey() {
			return alipayKey;
		}
		public void setAlipayKey(String alipayKey) {
			this.alipayKey = alipayKey;
		}
		public String getAlipaySellerEmail() {
			return alipaySellerEmail;
		}
		public void setAlipaySellerEmail(String alipaySellerEmail) {
			this.alipaySellerEmail = alipaySellerEmail;
		}
		public String getAlipayRate() {
			return alipayRate;
		}
		public void setAlipayRate(String alipayRate) {
			this.alipayRate = alipayRate;
		}
		public String getNotifyUrl() {
			return notifyUrl;
		}
		public void setNotifyUrl(String notifyUrl) {
			this.notifyUrl = notifyUrl;
		}
		public String getReturnUrl() {
			return returnUrl;
		}
		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}
		public String getAlipayInputCharset() {
			return alipayInputCharset;
		}
		public void setAlipayInputCharset(String alipayInputCharset) {
			this.alipayInputCharset = alipayInputCharset;
		}
	    
	    
}
