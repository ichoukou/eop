package net.ytoec.kernel.alipay.config;

public class AlipayConfig {
	
	public static String partner = "2088701753830358";
	
	// 交易安全检验码，由数字和字母组成的32位字符串
	public static String key = "p6vci9e44p8xep2xn54tngu7a090cyat";
	
	// 签约支付宝账号或卖家收款支付宝帐户
	public static String seller_email = "ytoxl@ytoxl.com";
	
	public static String notify_url = "http://ec.yto.net.cn/alipayNotify.action";
	
	public static String return_url = "http://ec.yto.net.cn/alipayReturn.action";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志路径
	//public static String log_path = "D:\\alipay_log_" + System.currentTimeMillis()+".txt";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";

}
