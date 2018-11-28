package net.ytoec.kernel.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.util.WebUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(AppServlet.class);

	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	private static final String DATA_DIGEST_PARAM = "data_digest";
	private static final String CLIENT_ID_PARAM = "clientID";
	private static final String GET_METHOD_RESPONSE = "Success";

	public void service(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		request.setCharacterEncoding("GBK");
		// resp.setCharacterEncoding("GBK");
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			resp.getWriter().print(GET_METHOD_RESPONSE);
			resp.flushBuffer();
			resp.getWriter().close();
			// super.print(GET_METHOD_RESPONSE);
			return;
		}
		// 临时解码提供给测试使用
		String logisticsInterface = WebUtil.decode(
				request.getParameter(LOGISTICS_INTERFACE_PARAM), "GBK");
		String cdataDigest = WebUtil.decode(
				request.getParameter(DATA_DIGEST_PARAM), "GBK");
		// 上线时不需要解码
		// String logisticsInterface =
		// request.getParameter(LOGISTICS_INTERFACE_PARAM);
		// String cdataDigest = request.getParameter(DATA_DIGEST_PARAM);
		String clientID = request.getParameter(CLIENT_ID_PARAM);
		// 标识
		String JGtoTBtype = request.getParameter("type");
		logger.debug("--liug--AppServlet--logisticsInterface-------"
				+ logisticsInterface + "--------AppServlet参数---");
		logger.debug("--liug--AppServlet-dataDigest--------" + cdataDigest
				+ "--------AppServlet参数---");
		logger.debug("-----AppServlet---" + request.getCharacterEncoding()
				+ "---" + request.getRemoteAddr() + ","
				+ request.getRemoteHost() + "-----------"
				+ System.getProperty("file.encoding")
				+ "--------------------------");
		logger.debug("--liug--AppServlet-clientID--------" + clientID
				+ "--------AppServlet参数---");

		// resp.sendRedirect("");

		XmlSender xmlSender = new XmlSender();
		String utf8LogisticsInterface = WebUtil.encode(logisticsInterface,
				"utf-8");
		String dataDigest = logisticsInterface + "123456";
		String md5DataDigest = Md5Encryption.MD5EncodeGBK(dataDigest);
		logger.debug("utf8LogisticsInterface:===" + utf8LogisticsInterface
				+ "===liug==");
		logger.debug("md5DataDigest:===" + md5DataDigest + "===liug==");

		String utf8DataDigest = WebUtil.encode(md5DataDigest, "utf-8");
		logger.debug("md5utf8DataDigest:===" + utf8DataDigest + "===liug==");

		// String url = "http://localhost:8080/VipOrderServlet.action";
		// String url = "http://10.1.198.71/kernel/VipOrderServlet.action";
		// 拆分
		String url = "http://localhost:8080/TaoBaoOrderServlet.action";
		// String url = "http://10.1.198.71/kernel/TaoBaoOrderServlet.action";

		if (JGtoTBtype != null && "1".equals(JGtoTBtype)) {
			url = "http://10.1.198.71/kernel/taoBaoStatusNotify.action";
			// url = "http://localhost:8080/kingGang2TaoBao.action";
		}

		// String url = "http://10.1.198.71/kernel/VipOrderServlet";
		// String url = "http://116.228.70.232/kernel/VipOrderServlet";
		// String url = "http://jingangtest.yto56.com.cn/ordws/VipOrderServlet";
		// String url = "http://10.1.198.71/kernel/kingGang2TaoBao";
		// String url =
		// "http://localhost:8080/kernel/kingGang2TaoBao?clientID=360buy";
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
		xmlSender.setRequestParams("logistics_interface="
				+ utf8LogisticsInterface + "&data_digest=" + utf8DataDigest
				+ "&clientID=TAOBAO");
		// xmlSender.setRequestParams("logistics_interface=" +
		// utf8LogisticsInterface + "&data_digest=" + utf8DataDigest);
		String result = xmlSender.send();
		logger.debug("--liug--AppServlet-result--------" + result
				+ "--------AppServlet--result---");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().print(result);
		resp.flushBuffer();
		resp.getWriter().close();

		// request.getRequestDispatcher("/VipOrderServlet.action").forward(request,
		// resp);
	}
}
