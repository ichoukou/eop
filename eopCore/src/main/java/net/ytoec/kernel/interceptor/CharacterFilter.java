package net.ytoec.kernel.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.FilterDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 访问的Filter 本Filter控制所有的后台访问，除了预定义的少数URL之外，大部分都是需要登录才可以访问的
 */

public class CharacterFilter extends FilterDispatcher {
	// implements Filter {

	private static Logger logger = LoggerFactory
			.getLogger(CharacterFilter.class);
	private static String encoding = "GBK";

	public void init(FilterConfig filterConfig) throws ServletException {

		super.init(filterConfig);

		String encodingParam = filterConfig.getInitParameter("encoding");

		if (encodingParam != null && encodingParam.trim().length() != 0) {

			encoding = encodingParam;

		}

	}

	public void doFilter(ServletRequest request, ServletResponse response,

	FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse ros = (HttpServletResponse) response;
		request.setCharacterEncoding(encoding);

		super.doFilter(request, response, chain);

	}

}
