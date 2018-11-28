package net.ytoec.kernel.action.test;

public class HttpClientTest {
	public static void main(String[] args) throws Exception {
//		testProcess();
		
		testThread();
		
	}
	
	
	public static void testThread() throws Exception {
//		String url = "http://shootyou.iteye.com/blog/1129512";
//		String url = "http://news.sina.com.cn/c/2011-09-22/065923197372.shtml";
//		String url = "http://192.168.2.202/";
//	String url = "http://127.0.0.1:8080/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
		//String url = "http://192.168.4.46:8080/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
	//    String url = "http://service.yto56.net.cn/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
	//   String url="http://10.1.198.71/kernel/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
	//	String url="http://127.0.0.1:8080/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
	//	 String url= "http://wuliu.daily.taobao.net/user/express_message_receiver.do?_input_charset=UTF-8";
	 // String url="http://10.1.5.82/taoBaoStatusNotify.action?ecCompanyId=TAOBAO";
		// String url="http://service.yto56.net.cn/TaoBaoOrderServlet.action";
		// String url="http://127.0.0.1:8080/TaoBaoOrderServlet.action";
		 String url="http://127.0.0.1:8080/kernel/TaoBaoOrderServlet.action";
		 
		
	//	String updateString="%3CUpdateInfo%3E%0A++%3CtxLogisticID%3ELP00000001557371%3C%2FtxLogisticID%3E%0A++%3CmailNo%3E6031446236%3C%2FmailNo%3E%0A++%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%0A++%3CinfoType%3ESTATUS%3C%2FinfoType%3E%0A++%3CinfoContent%3ESIGNED%3C%2FinfoContent%3E%0A++%3CacceptTime%3E2011-10-27+15%3A32%3A08.0+CST%3C%2FacceptTime%3E%0A%3C%2FUpdateInfo%3E";
			//String updateString="%3CRequestOrder%3E%0D%0A%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%3CtxLogisticID%3ELP000000020100002%3C%2FtxLogisticID%3E%0D%0A%3CcustomerId%3E6f730147d3cdb15beed67b054668c77f%3C%2FcustomerId%3E%3";
	///	String digestString="6%2Bl68%2Bm0h9MNV7qMxl4Tpg%3D%3D";
			String digestString="l0mdwKateU3ETanxjBHcDg%3D%3D";
			//String updateString="%3CRequestOrder%3E%0D%0A%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%3CtxLogisticID%3ELP000000020100002%3C%2FtxLogisticID%3E%0D%0A%3CcustomerId%3E6f730147d3cdb15beed67b054668c77f%3C%2FcustomerId%3E%3CtradeNo%3E20100002%3C%2FtradeNo%3E%0D%0A%3Ctype%3E2%3C%2Ftype%3E%3CecCompanyId%3ETaobao%3C%2FecCompanyId%3E%3CorderType%3E0%3C%2ForderType%3E%0D%0A%3CserviceType%3E0%3C%2FserviceType%3E%3Cflag%3E0%3C%2Fflag%3E%3CmailNo%3E1234567890%3C%2FmailNo%3E%0D%0A%3Csender%3E%3Cname%3E%E5%B7%A5664%3C%2Fname%3E%3CpostCode%3E310012%3C%2FpostCode%3E%3Cmobile%3E13587457874%3C%2Fmobile%3E%0D%0A%3Cprov%3E%E5%8C%97%E4%BA%AC%3C%2Fprov%3E%3Ccity%3E%E5%8C%97%E4%BA%AC%E5%B8%82%2C%E4%B8%9C%E5%9F%8E%E5%8C%BA%3C%2Fcity%3E%0D%0A%3Caddress%3E%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%E4%BA%AC%E9%83%BD4647%E5%8C%97%3C%2Faddress%3E%0D%0A%3C%2Fsender%3E%3Creceiver%3E%3Cname%3Eabcd%3C%2Fname%3E%3CpostCode%3E210000%3C%2FpostCode%3E%3Cmobile%3E123456567%3C%2Fmobile%3E%0D%0A%3Cprov%3E%E5%8C%97%E4%BA%AC%3C%2Fprov%3E%3Ccity%3E%E5%8C%97%E4%BA%AC%E5%B8%82%2C%E4%B8%9C%E5%9F%8E%E5%8C%BA%3C%2Fcity%3E%3Caddress%3E%E5%B9%BF%E5%8D%9A%E5%A4%A7%E5%8E%A6%3C%2Faddress%3E%3C%2Freceiver%3E%3CgoodsValue%3E144.0%3C%2FgoodsValue%3E%0D%0A%3CitemsValue%3E144.0%3C%2FitemsValue%3E%0D%0A%3Citems%3E%3Citem%3E%0D%0A%3CitemName%3ENOKIA%3C%2FitemName%3E%0D%0A%3Cnumber%3E12%3C%2Fnumber%3E%3CitemValue%3E12234.0%3C%2FitemValue%3E%0D%0A%3C%2Fitem%3E%3C%2Fitems%3E%3CinsuranceValue%3E0.0%3C%2FinsuranceValue%3E%0D%0A%3Cspecial%3E0%3C%2Fspecial%3E%0D%0A%3C%2FRequestOrder%3E";
		String updateString="%3CRequestOrder%3E%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%3CtxLogisticID%3ELP000000020100002%3C%2FtxLogisticID%3E%3CcustomerId%3E6f730147d3cdb15beed67b054668c77f%3C%2FcustomerId%3E%3CtradeNo%3E2";
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append("logistics_interface=");
		sbBuffer.append(updateString);
		sbBuffer.append("&data_digest=");
		sbBuffer.append(digestString);
		
		
		
		HttpChannel sender = new HttpChannel(url,sbBuffer.toString());
		
		HttpClientThreadTest[] threads = new HttpClientThreadTest[10];
		for (int i = 0; i < threads.length; i++) {
		    threads[i] = new HttpClientThreadTest(sender);
		}

		// start the threads
	
		for (int j = 0; j < threads.length; j++) {
		    threads[j].start();
		}
	}
}
