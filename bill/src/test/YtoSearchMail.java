package test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class YtoSearchMail {
	public String send(String[] logisticsId){
		try {			
 			
 			String parterid="jNpKcyXrHfNJ";
			
			java.util.Date d = new java.util.Date();
			HttpClient client = new DefaultHttpClient();
			String seq = d.getTime() + "";
			seq = "TT2000000002";
			String logistics_interface = getCreateXml(logisticsId);

			String url="http://jingang.yto56.com.cn/ordws/TaoBao14Servlet";

			java.util.ArrayList<NameValuePair> al = new java.util.ArrayList<NameValuePair>();

			NameValuePair nv1 = new BasicNameValuePair("logistics_interface",
					logistics_interface);
			al.add(nv1);
			NameValuePair nv2 = new BasicNameValuePair("data_digest",
					EncryptMD5(logistics_interface, parterid));// jNpKcyXrHfNJ
			al.add(nv2);
			NameValuePair type = new BasicNameValuePair("type", "offline");
			al.add(type);

			UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(al, "UTF-8");
			HttpPost method = new HttpPost(url);
			method.setEntity(uefe);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String result = client.execute(method, responseHandler);
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String EncryptMD5(String input, String parterid)
	throws Exception {

		return new String(Base64.encodeBase64(DigestUtils
		.md5((input + parterid).getBytes("UTF-8"))));
	}
	public static String getCreateXml(String[] seq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders>");
		for (int i = 0; i < seq.length; i++) {
			sb.append("<order><mailNo>");
			sb.append(seq[i]);
			sb.append("</mailNo></order>");
		}
		sb.append("</orders></BatchQueryRequest>");

		return sb.toString();
	}
}
