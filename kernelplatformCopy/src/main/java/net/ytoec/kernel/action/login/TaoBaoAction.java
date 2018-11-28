/**
 * TaoBaoAction.java
 * Wangyong
 * 2011-9-21 下午03:05:35
 */
package net.ytoec.kernel.action.login;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.ytoec.kernel.action.taobao.common.Util;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
/**
 * 淘宝免登录签名验证、参数解析、获取用户信息
 * @author Wangyong
 * @2011-9-21
 * net.ytoec.kernel.action.login
 */

@Controller
@Scope("prototype")
public class TaoBaoAction {
	  private static Logger                     logger          = LoggerFactory.getLogger(TaoBaoAction.class);
	/**
	 * 组装请求参数（淘宝TOP获取用户信息）
	 * @param format
	 * @param method
	 * @param sign_method
	 * @param app_key
	 * @param session
	 * @param nick
	 * @return
	 */
	public static String getTaoBaoUser(String format, String method, String sign_method, String app_key, String session, String visitorId){
		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("format", format);
        apiparamsMap.put("method", method);
        apiparamsMap.put("sign_method",sign_method);
        apiparamsMap.put("app_key",app_key);
        apiparamsMap.put("v", "2.0");
        apiparamsMap.put("session",session);//他用型需要sessionkey
        String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp",timestamp);
        apiparamsMap.put("fields",Constants.FIELDS);//需要获取的字段
//        apiparamsMap.put("nick",nick);
        apiparamsMap.put("uid",visitorId);
        //生成签名
        String sign = Util.md5Signature(apiparamsMap, ConfigUtilSingle.getInstance().getTOP_SECRET());
        apiparamsMap.put("sign", sign);
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        logger.error("==================param.toString()：=="+param.toString());
        return param.toString().substring(1);
	}
	
	/**
	 * 通过淘宝接口获取用户的加密后的customerId
	 * 
	 * @param session	TOP分配给用户的SessionKey
	 * @return	加密后的customerId
	 */
	public static String getCustomerId(String session) {
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
        String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        paramMap.put("timestamp",timestamp);
        paramMap.put("v", "2.0");
        paramMap.put("app_key", ConfigUtilSingle.getInstance().getTOP_APPKEY());
        paramMap.put("method", ConfigUtilSingle.getInstance().getTAOBAO_USER_ENCRYPTEDID()); // 请求方法
        paramMap.put("session",session);
        paramMap.put("sign_method", ConfigUtilSingle.getInstance().getSIGN_METHOD_MD5()); // 参数的加密方式
        paramMap.put("format", ConfigUtilSingle.getInstance().getFORMAT_JSON()); // 响应的数据格式

        //生成签名
        String sign = Util.md5Signature(paramMap, ConfigUtilSingle.getInstance().getTOP_SECRET());
        paramMap.put("sign", sign);
        
        StringBuilder sbParam = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = paramMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            sbParam.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        
        // <?xml version=\"1.0\" encoding=\"utf-8\" ?><logistics_encryptedid_get_response><encrypted_id>56920bd8de5205f17c1cd833e3bb125a</encrypted_id></logistics_encryptedid_get_response>
        // "{\"logistics_encryptedid_get_response\":{\"encrypted_id\":\"56920bd8de5205f17c1cd833e3bb125a\"}}"
        String response = Util.getResult(ConfigUtilSingle.getInstance().getOFFICALURL(), sbParam.toString());
        String[] arr = response.split("\"");
        
		return arr[5];
	} // getCustomerId
	
	/**
	 * 退出top登录
	 * @param sign_method
	 * @param app_key
	 * @return
	 */
	public static String logoffOut(String sign_method, String app_key){
		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		// 组装协议参数。
		apiparamsMap.put("sign_method", sign_method);
		apiparamsMap.put("app_key", app_key);
		apiparamsMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String sign = Util.md5Signature(apiparamsMap, ConfigUtilSingle.getInstance().getTOP_SECRET());
		// 组装协议参数sign
		apiparamsMap.put("sign", sign);
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=").append(e.getValue());
		}
		return param.toString().substring(1);
	}

    /**
     * 获取app退出URL
     * 
     * @throws UnsupportedEncodingException
     */
    public static String getlogoffURL(String sign_method, String app_key) throws UnsupportedEncodingException {
		// 组装请求URL
        StringBuilder url = new StringBuilder(ConfigUtilSingle.getInstance().getLOGOFF_URL() + "?");
		url.append(logoffOut(sign_method, app_key));
        return url.toString();
	}
    
    /**
     * 获取淘宝服务url地址
     */
    public static String getTaobaofuwuURL(){
    	String url = ConfigUtilSingle.getInstance().getTAOBAOFUWU_URL();
    	return url;
    }

	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) {
        // String result = Util.getResult(Constants.OFFICALURL,getTaoBaoUser("xml", "taobao.user.get", "md5",
        // ConfigUtilSingle.getInstance().getTOP_APPKEY(), "409210859c7c261XSuBwPLv723db03b314fd8b9bbe4499a5604851361",
        // "笙箫一郎"));
//        System.out.print(result);

//		String param = "customerId=" + "laolu90" 

        // + "&app_key=" + ConfigUtilSingle.getInstance().getTOP_APPKEY()
//		+ "&secret=" + Constants.TOP_SECRET;
//		
//		String response = Util.getResult(Constants.TAOBAO_USER_ENCRYPTEDID, param);
//		logger.debug("响应："+response);
//		String x = "sign=D6F9032E728CB56C5B4BF4374982BE5F&timestamp=2011-10-26+11%3A01%3A25&v=2.0&app_key=12395881&method=taobao.logistics.encryptedid.get&partner_id=top-apitools&session=41026266cd61ea98609d3de61071d49derXUwaYqiefa5a76274422361&format=xml";
//		String x = "&app_key=12395881&format=xml&method=taobao.logistics.encryptedid.get&session=41026071933e5aPyoPvZih5151b8e742907a761dd52b9df445428541&sign=8BDEC8A881079790ABEE24CC154C8177&sign_method=md5&timestamp=2011-10-26 13:47:24&v=2.0";
//		String response = Util.getResult(Constants.OFFICALURL, "");
//        logger.debug("################# response: " + response);
        
        String t = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><logistics_encryptedid_get_response><encrypted_id>56920bd8de5205f17c1cd833e3bb125a</encrypted_id></logistics_encryptedid_get_response>";
//        int i = t.indexOf("encrypted_id") + 1;
//        int j = t.lastIndexOf("encrypted_id") - 1;
//        t = t.replace("encrypted_id", "$");
//        int i = t.indexOf("$") + 2;
//        int j = t.lastIndexOf("$") - 2;
//        logger.debug(t.substring(i, j));
        
        String [] aaa = t.split("encrypted_id");
//        logger.debug(aaa[1].substring(1, aaa[1].length()-2));
        
        String x  = "{\"logistics_encryptedid_get_response\":{\"encrypted_id\":\"56920bd8de5205f17c1cd833e3bb125a\"}}";
        String[] xx = x.split("\"");
        
	}

}
