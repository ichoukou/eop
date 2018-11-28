package net.ytoec.kernel.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.techcenter.util.MD5;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;

/**
 * 短信发送请求类
 * @author wmd
 *
 */
public class HttpPostCore {

	private String methodSend = "yto.sms.send";
	private String methodSearch = "yto.sms.stateInquires";
	private String vision = "1.0";
	private String user_id = "wangmindong";
	private String app_key = "L7q0Wc"; 
	private String format = "xml";
	private String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	private String requestParams;
	private String parames=null;
	
	/**
	 * HTTP 连接,短信发送
	 * @param url
	 * @param xml
	 * @return
	 */
	public String connect(String url,String xml,Integer status){
		//String sendSysParams = encode(setParamsStr(status),"UTF-8");
		String sendSysParams = setParamsStr(status);
		System.err.println("请求参数为："+sendSysParams);
		String MD5SysParams = this.MD5Encode(sendSysParams);
		
		String params = "sign=" + encode(MD5SysParams,"UTF-8") + setParams(status)+"&param=" + encode(xml,"UTF-8");
		System.err.println("完整的请求参数为:"+ params);
		this.setRequestParams(params);
		return conn(url);
	}
	/**
	 * MD5加密
	 * @param sendSysParams
	 * @return
	 */
	private String MD5Encode(String sendSysParams) {
		
		MD5 charset = new MD5();
		return charset.getMD5ofStr(sendSysParams);
		
	}
	/**
	 * 查看短信发送状态
	 * @return
	 */
	public String searchSmsStatus(String url,String xml,Integer status){
		String searchSysParams = setParamsStr(status);
		String MD5SysParams = this.MD5Encode(searchSysParams);
		
		String params = "sign=" + encode(MD5SysParams,"UTF-8") + setParams(status)+"&param=" + encode(xml,"UTF-8");
		this.setRequestParams(params);
		return conn(url);
	}
	
	/**
	 * 公共连接请求
	 * @param url
	 * @return
	 */
	public String conn(String url){
		URL ul = null;
		try {
			ul = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		URLConnection urlConnection = null;
		try {
			urlConnection = ul.openConnection();
			HttpURLConnection huc = (HttpURLConnection) urlConnection;

			huc.setUseCaches(false);
			huc.setDoInput(true);
			huc.setDoOutput(true);
			huc.setRequestMethod(XmlSender.GET_REQUEST_METHOD);
			huc.setInstanceFollowRedirects(true);
			huc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			huc.connect();
			DataOutputStream out = new DataOutputStream(huc.getOutputStream());
			String args = this.getRequestParams();
			out.writeBytes(args);
			out.flush();
			out.close();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), XmlSender.UTF8_CHARSET));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
            e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
                } catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	public String setParams(Integer status){
		String method = "";
		if(status == 1)
			method = encode(methodSend,"UTF-8") ;
		else
			method = encode(methodSearch,"UTF-8") ;
		parames = "&app_key=" + encode(app_key,"UTF-8") + "&format=" + encode(format,"UTF-8")
		+ "&method=" + method +"&timestamp=" + encode(timestamp,"UTF-8") + "&user_id=" + encode(user_id,"UTF-8") + "&v=" + encode(vision,"UTF-8");
		
		return parames;
	}
	public String setParamsStr(Integer status){
		String method = "";
		if(status == 1)
			method = methodSend ;
		else
			method = methodSearch ;
		StringBuffer buffer = new StringBuffer();
		List<String> sysParamsList = new ArrayList<String>();
		sysParamsList.add("app_key="+app_key);
		sysParamsList.add("format="+format);
		sysParamsList.add("method="+method);
		sysParamsList.add("timestamp="+timestamp);
		sysParamsList.add("user_id="+user_id);
		sysParamsList.add("v="+vision);
		for(int i = 0 ;i < sysParamsList.size();i++){
			System.out.println(sysParamsList.get(i));
			buffer.append(sysParamsList.get(i).split("=")[0].trim()+ sysParamsList.get(i).split("=")[1].trim());
		}
		System.err.println("请求参数："+buffer);
		return "UzkGBW"+buffer;
	}
	/**
	 * URL编码.
	 * @param arg
	 * @param charset
	 * @return
	 */
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
}
