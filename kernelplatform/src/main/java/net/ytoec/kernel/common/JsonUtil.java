package net.ytoec.kernel.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	public static String toJson(Object obj) {
		String json = "";

		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			if (obj != null) {
				mapper.writeValue(sw, obj);
				json = sw.toString();
			}
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return json;
	}

	public static void response(String json) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");// 解决中文乱码
		try {
			PrintWriter out = response.getWriter();
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
