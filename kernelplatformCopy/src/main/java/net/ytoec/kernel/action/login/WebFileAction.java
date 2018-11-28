package net.ytoec.kernel.action.login;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.util.FileUtil;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 网络文件上传下载Action
 * @author wangjianzhong
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class WebFileAction extends AbstractActionSupport {
	
	private Logger logger = LoggerFactory.getLogger(WebFileAction.class);
	
	/**
	 * 帮助文件下载
	*/
	public String helpDownLoad() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			
			//根据用户类型,下载帮助文件,只有卖家和网点下载
			int userType = getUserType();
			String fileName = "";
			if(userType==1) {  //卖家
				fileName = "卖家帮助文档.zip";
			}
			if(userType==2) {  //网点
				fileName = "网点帮助文档.zip";
			}
			File file = new File(FileUtil.getClassRoot() + "/template/"+fileName);
			if (!file.exists()) {
				logger.error("文件不存在!" + file);
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			String fname = file.getName();

			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
			} else {
				fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
			}
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/msword");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fname);
			OutputStream out = response.getOutputStream();
			while ((len = bis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bis.close();
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取登录用户类型 0:未知的类型  1：卖家 2：网点 3：admin 4:平台用户
	 * @return
	 */
	private int getUserType() {
		User landUser = super.readCookieUser();
		String userType = landUser.getUserType();
		if("1".equals(userType)||"11".equals(userType)||"12".equals(userType)||"13".equals(userType)) {
			return 1;
		}else if("2".equals(userType)||"21".equals(userType)||"22".equals(userType)||"23".equals(userType)){
			return 2;
		}else if("3".equals(userType)) {
			return 3;
		}else if("4".equals(userType)||"41".equals(userType)||"42".equals(userType)||"43".equals(userType)) {
			return 4;
		}
		return 0;
	}
}
