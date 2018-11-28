package net.ytoec.kernel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInvoker {
	private static Logger logger = LoggerFactory.getLogger(HttpInvoker.class);
	private static HttpInvoker httpInvoker = new HttpInvoker();
	private HttpClient client = null;
	private String charset = "utf-8";
	private int timeout = 25000; // 超时时间，根据实际的网络环境可能微调
	private boolean useProxy = false;
	private String proxyHost = null;
	private int proxyPort;
	private String proxyUsername = null;
	private String proxyPassword = null;
	private boolean initialized = false;

	public static HttpInvoker getInstance() {
		return httpInvoker;
	}

	private HttpInvoker() {
		this.client = new HttpClient(new MultiThreadedHttpConnectionManager());
		this.client.getParams().setParameter("http.protocol.content-charset",
				"utf-8");
		this.client.getParams().setContentCharset("utf-8");
		this.client.getParams().setSoTimeout(this.timeout);
	}

	public HttpInvoker(String charset, int timeout, boolean useProxy,
			String proxyHost, int proxyPort, String proxyUsername,
			String proxyPassword) {
		this.client = new HttpClient(new MultiThreadedHttpConnectionManager());
		if ((charset != null) && (!charset.trim().equals(""))) {
			this.charset = charset;
		}
		if (timeout > 0) {
			this.timeout = timeout;
		}
		this.client.getParams().setParameter("http.protocol.content-charset",
				charset);
		this.client.getParams().setContentCharset(charset);
		this.client.getParams().setSoTimeout(timeout);
		if ((useProxy) && (proxyHost != null) && (!proxyHost.trim().equals(""))
				&& (proxyPort > 0)) {
			HostConfiguration hc = new HostConfiguration();
			hc.setProxy(proxyHost, proxyPort);
			this.client.setHostConfiguration(hc);
			if ((proxyUsername != null) && (!proxyUsername.trim().equals(""))
					&& (proxyPassword != null)
					&& (!proxyPassword.trim().equals(""))) {
				this.client.getState().setProxyCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(proxyUsername,
								proxyPassword));
			}
		}

		this.initialized = true;
		this.logger.debug("HttpInvoker初始化完成");
	}

	public synchronized void init() {
		if ((this.charset != null) && (!this.charset.trim().equals(""))) {
			this.client.getParams().setParameter(
					"http.protocol.content-charset", this.charset);
			this.client.getParams().setContentCharset(this.charset);
		}
		if (this.timeout > 0) {
			this.client.getParams().setSoTimeout(this.timeout);
		}
		if ((this.useProxy) && (this.proxyHost != null)
				&& (!this.proxyHost.trim().equals("")) && (this.proxyPort > 0)) {
			HostConfiguration hc = new HostConfiguration();
			hc.setProxy(this.proxyHost, this.proxyPort);
			this.client.setHostConfiguration(hc);
			if ((this.proxyUsername != null)
					&& (!this.proxyUsername.trim().equals(""))
					&& (this.proxyPassword != null)
					&& (!this.proxyPassword.trim().equals(""))) {
				this.client.getState().setProxyCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(this.proxyUsername,
								this.proxyPassword));
			}
		}

		this.initialized = true;
		this.logger.debug("HttpInvoker初始化完成");
	}

	public String invoke(String url) throws Exception {
		return invoke(url, null, false);
	}

	public String invoke(String url, Map params, boolean isPost)
			throws Exception {
		this.logger.debug("HTTP调用[" + (isPost ? "POST" : "GET") + "][" + url
				+ "][" + params + "]");
		HttpMethod httpMethod = null;
		String result = "";
		try {
			if ((isPost) && (params != null) && (params.size() > 0)) {
				Iterator paramKeys = params.keySet().iterator();
				httpMethod = new PostMethod(url);
				NameValuePair[] form = new NameValuePair[params.size()];
				int formIndex = 0;
				while (paramKeys.hasNext()) {
					String key = (String) paramKeys.next();
					Object value = params.get(key);
					if ((value != null) && ((value instanceof String))
							&& (!value.equals(""))) {
						form[formIndex] = new NameValuePair(key, (String) value);
						formIndex++;
					} else if ((value != null) && ((value instanceof String[]))
							&& (((String[]) (String[]) value).length > 0)) {
						NameValuePair[] tempForm = new NameValuePair[form.length
								+ ((String[]) (String[]) value).length - 1];

						for (int i = 0; i < formIndex; i++) {
							tempForm[i] = form[i];
						}
						form = tempForm;
						for (String v : (String[]) (String[]) value) {
							form[formIndex] = new NameValuePair(key, v);
							formIndex++;
						}
					}
				}
				((PostMethod) httpMethod).setRequestBody(form);
			} else if ((params != null) && (params.size() > 0)) {
				Iterator paramKeys = params.keySet().iterator();
				StringBuffer getUrl = new StringBuffer(url.trim());
				if (url.trim().indexOf("?") > -1) {
					if ((url.trim().indexOf("?") < url.trim().length() - 1)
							&& (url.trim().indexOf("&") < url.trim().length() - 1)) {
						getUrl.append("&");
					}
				} else
					getUrl.append("?");

				while (paramKeys.hasNext()) {
					String key = (String) paramKeys.next();
					Object value = params.get(key);
					if ((value != null) && ((value instanceof String))
							&& (!value.equals("")))
						getUrl.append(key).append("=").append(value)
								.append("&");
					else if ((value != null) && ((value instanceof String[]))
							&& (((String[]) (String[]) value).length > 0)) {
						for (String v : (String[]) (String[]) value) {
							getUrl.append(key).append("=").append(v)
									.append("&");
						}
					}
				}
				if (getUrl.lastIndexOf("&") == getUrl.length() - 1)
					httpMethod = new GetMethod(getUrl.substring(0,
							getUrl.length() - 1));
				else
					httpMethod = new GetMethod(getUrl.toString());
			} else {
				httpMethod = new GetMethod(url);
			}

			this.client.executeMethod(httpMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpMethod.getResponseBodyAsStream(), "ISO-8859-1"));

			String line = null;
			String html = null;
			while ((line = reader.readLine()) != null) {
				if (html == null)
					html = "";
				else {
					html = html + "\r\n";
				}
				html = html + line;
			}
			if (html != null)
				result = new String(html.getBytes("ISO-8859-1"), this.charset);
		} catch (SocketTimeoutException e) {
			this.logger.error("连接超时[" + url + "]");
			throw e;
		} catch (ConnectException e) {
			this.logger.error("连接失败[" + url + "]");
			throw e;
		} catch (Exception e) {
			this.logger.error("连接时出现异常[" + url + "]");
			throw e;
		} finally {
			if (httpMethod != null) {
				try {
					httpMethod.releaseConnection();
				} catch (Exception e) {
					this.logger.error("释放网络连接失败[" + url + "]");
					throw e;
				}
			}
		}

		return result;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public synchronized boolean isInitialized() {
		return this.initialized;
	}

}
