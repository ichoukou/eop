package net.ytoec.kernel.action.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送请求消息并得到响应消息.
 */
public class XmlSender {

	/**
	 * HTTP请求方法:POST方法.
	 */
	public static final String POST_REQUEST_METHOD = "POST";
	/**
	 * HTTP请求方法:GET方法.
	 */
	public static final String GET_REQUEST_METHOD = "GET";
	/**
	 * 字符集:GBK字符集.
	 */
	public static final String GBK_CHARSET = "GBK";
	
	/**
	 * 字符集:UTF-8字符集.
	 */
	public static final String UTF8_CHARSET = "UTF-8";

	private String requestMethod = GET_REQUEST_METHOD;
	private String urlString;
	private String requestParams;

	private static Logger logger=LoggerFactory.getLogger(XmlSender.class);

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public XmlSender() {

	}

	// 调用
	public String send(Map<String, String> queryParams) {
		// String urlString = this.createGetUrl(queryParams);
		String result = this.send();
		return result;
	}

	public String send() {
		// FIXME 可配置
		URL url = null;
		try {
			url = new URL(urlString);

		} catch (MalformedURLException e) {
            logger.error("", e);
		}

		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
			HttpURLConnection huc = (HttpURLConnection) urlConnection;

			huc.setUseCaches(false);
			huc.setDoInput(true);
			if (XmlSender.GET_REQUEST_METHOD.equals(this.getRequestMethod())) {
				this.executeGetMethod(huc);
			} else if (XmlSender.POST_REQUEST_METHOD.equals(this
					.getRequestMethod())) {
				this.executePostMethod(huc);
			} else {
				throw new RuntimeException("不支持此请求方法:"
						+ this.getRequestMethod());
			}

			// huc.disconnect();
        } catch (Exception e) {
            logger.error(" ", e);
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
            logger.error(" ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
                } catch (Exception e) {
					logger.debug("BufferedReader对象无法正常关闭!" + e);
				}
			}
		}

		String result = sb.toString();
		// String result =
		// "<Response><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP05082300225709000</txLogisticID><success>true</success></Response>";
		return result;
	}

	public String sendTaoBao() {
		// FIXME 可配置
		URL url = null;
		try {
			url = new URL(urlString);

		} catch (MalformedURLException e) {
            logger.error("", e);
		}

		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
			HttpURLConnection huc = (HttpURLConnection) urlConnection;

			huc.setUseCaches(false);
			huc.setDoInput(true);
			if (XmlSender.GET_REQUEST_METHOD.equals(this.getRequestMethod())) {
				this.executeGetMethod(huc);
			} else if (XmlSender.POST_REQUEST_METHOD.equals(this
					.getRequestMethod())) {
				this.executePostMethod(huc);
			} else {
				throw new RuntimeException("不支持此请求方法:"
						+ this.getRequestMethod());
			}

			// huc.disconnect();
        } catch (Exception e) {
            logger.error(" ", e);
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), XmlSender.GBK_CHARSET));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
            logger.error(" ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
                } catch (Exception e) {
					logger.debug("BufferedReader对象无法正常关闭!" + e);
				}
			}
		}

		String result = sb.toString();
		// String result =
		// "<Response><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP05082300225709000</txLogisticID><success>true</success></Response>";
		return result;
	}
	
	private void executeGetMethod(HttpURLConnection urlConnection) {

	}

	private void executePostMethod(HttpURLConnection huc) throws IOException {
		huc.setDoOutput(true);
		huc.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
		huc.setInstanceFollowRedirects(true);
		huc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		huc.connect();
		DataOutputStream out = new DataOutputStream(huc.getOutputStream());
		String args = this.getRequestParams();
		out.writeBytes(args);
		out.flush();
		out.close();
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

}