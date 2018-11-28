package net.ytoec.kernel.action.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import net.ytoec.kernel.action.common.AbstractActionSupport;
/**
 * 
 *  接口模块Action的公共超类.
 *
 */

@Controller
@Scope("prototype")
public class AbstractInterfaceAction extends AbstractActionSupport {

	private static final long serialVersionUID = 9022954438764903975L;
	public static final String REQUEST_KEY_PARAM = "requestKey";
	private Properties initParameters = new Properties();

	public Properties getInitParameters() {
		return initParameters;
	}

	public void setInitParameters(Properties initParameters) {
		this.initParameters = initParameters;
	}

	protected String getInitParameter(String paramName) {
		return this.getInitParameters().getProperty(paramName);
	}

	/**
	 * 将string的内容写入到响应对象中. 注意:此方法仅应该被调用一次,因为方法内部的流对象在写入完成后关闭,
	 * 因此程序中应该去组装string值,然后调用此方法写入响应对象中.
	 * 
	 * @param string
	 *            待写入的字符串.
	 * @throws IOException
	 */
	public void print(String string) throws IOException {
		this.setResponseContentType(response);
		PrintWriter out = this.response.getWriter();
		out.print(string);
		out.flush();
		out.close();

	}
	
	/**
	 * @param string
	 *            待写入的字符串.
	 * @throws IOException
	 */
	public void printxml(String responseData) throws IOException {
		PrintWriter out = null;
		response.setContentType("application/xml;charset=UTF-8");// 解决中文乱码
		try {
			out = response.getWriter();
			out.println(responseData);
			out.flush();
			out.close();
		} catch (IOException e) {
		}finally{
			if (out != null) {
				out.close();
				out = null;
			}
		}

	}

	private void setResponseContentType(HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
	}
}
