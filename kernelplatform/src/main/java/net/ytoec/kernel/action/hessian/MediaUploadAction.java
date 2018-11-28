package net.ytoec.kernel.action.hessian;

import java.io.File;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.caucho.hessian.client.HessianProxyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.ytoec.uninet.model.MediaHessianModel;
import com.ytoec.uninet.service.MediaHessianService;
import com.ytoec.uninet.util.HessianUtil;

/**
 * 将图片发送到图片服务，返回图片URI给页面
 * @author xupf
 *
 */
@Controller
public class MediaUploadAction extends ActionSupport {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7139308492288292526L;

	private static Logger logger = LoggerFactory.getLogger(MediaUploadAction.class);

	// 输入参数:上传过来的文件路径
	private File file;
	//文件名（注意变量名）
	private String fileFileName;
	// 输入参数:企业ID
	private String companyId;
	// 输入参数:图片类别
	private String category;
	

	// 输出参数:原图保存路径
	private String ipath;

	// 输出参数
	private String json;
	
	private static String ERROR = "error";

	public MediaUploadAction() {

	}

	@Override
	public String execute() throws Exception {
		
		if(file == null)
		{
			logger.error("upload file is null !");
			ipath = ERROR;
			return "success";
		}

		//图片服务URL
		String url = HessianUtil.getMediaHessianService();
		
		//获得图片服务本地代理
		HessianProxyFactory factory = new HessianProxyFactory();
		MediaHessianService mediaHessianService = (MediaHessianService) factory.create(
				MediaHessianService.class, url);
		
		//封装ImageHessianModel
		byte[] buffer = FileUtils.readFileToByteArray(file);
		MediaHessianModel model = new MediaHessianModel();
		model.setCategory(category);
		model.setCompanyId(Integer.valueOf(companyId));
		model.setFile(buffer);
		model.setFileName(fileFileName);
		
		//调用图片服务
		try{
			ipath = mediaHessianService.transferImage(model);
		}catch(Exception e)
		{
			if(ipath == null ||ipath.equals(""))
			{
				logger.error("调用图片服务失败！");
				ipath ="error";
			}
		}	
		
		return "success";
		
	}
	/* 输入参数set方法 */
	public void setFile(File file) {
		this.file = file;
	}
	
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 此方法必须有，否则页面报错
	 * @param json
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/* 输出参数 */
	public String getIpath() {
		return ipath;
	}

	public String getJson() {
		return json;
	}
	
}
