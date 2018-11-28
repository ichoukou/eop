package com.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 发送请求消息并得到响应消息.
 */
public class HttpClient {

	/**
	 * HTTP请求方法:POST方法.
	 */
	public static final String POST_REQUEST_METHOD = "POST";
	/**
	 * HTTP请求方法:GET方法.
	 */
	public static final String GET_REQUEST_METHOD = "GET";
	/**
	 * 字符集:UTF-8字符集.
	 */
	public static final String UTF8_CHARSET = "UTF-8";
	public static final String GBK_CHARSET = "GBK";
	private String requestMethod = GET_REQUEST_METHOD;
	private String urlString;
	private String requestParams;

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public HttpClient() {

	}

	// 调用
	public String send(Map<String, String> queryParams) {
		// String urlString = this.createGetUrl(queryParams);
		String result = this.send();
		return result;
	}

	public String send() {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// 写入日志到log表
		}

		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
			HttpURLConnection huc = (HttpURLConnection) urlConnection;

			huc.setUseCaches(false);
			huc.setDoInput(true);
			if (HttpClient.GET_REQUEST_METHOD.equals(this.getRequestMethod())) {
				this.executeGetMethod(huc);
			} else if (HttpClient.POST_REQUEST_METHOD.equals(this
					.getRequestMethod())) {
				this.executePostMethod(huc);
			} else {
				throw new RuntimeException("不支持此请求方法:"
						+ this.getRequestMethod());
			}

			// huc.disconnect();
		} catch (Exception e) {
			// 写入日志到log表
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), HttpClient.UTF8_CHARSET));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// 写入日志到log表
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					// 写入日志到log表
				}
			}
		}

		String result = sb.toString();
		return result;
	}

	private void executeGetMethod(HttpURLConnection urlConnection) {

	}

	private void executePostMethod(HttpURLConnection huc) throws IOException {
		huc.setDoOutput(true);
		huc.setRequestMethod(HttpClient.POST_REQUEST_METHOD);
		huc.setInstanceFollowRedirects(true);
		huc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
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