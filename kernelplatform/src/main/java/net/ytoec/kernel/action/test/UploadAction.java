package net.ytoec.kernel.action.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

//文件上传Action    
public class UploadAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 上传文件存放路径
	private final static String UPLOADDIR = "/upload";
	// 上传文件集合
	private List<File> file;
	// 上传文件名集合
	private List<String> fileFileName;
	// 上传文件内容类型集合
	private List<String> fileContentType;

	private Logger logger = LoggerFactory.getLogger(UploadAction.class);

	public List<File> getFile() {
		return file;
	}

	public void setFile(List<File> file) {
		this.file = file;
	}

	public List<String> getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}

	public List<String> getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(List<String> fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String execute() throws Exception {
		try {
			long startTime = System.currentTimeMillis();
			logger.error("start upload file:" + startTime + "," + new Date());
			for (int i = 0; i < file.size(); i++) {
				// 循环上传每个文件
				uploadFile(i);
			}
			logger.error("end upload file:"
					+ (System.currentTimeMillis() - startTime) + ","
					+ new Date());
		} catch (Exception e) {
			logger.error("upload file", e);
		}
		return "success";

	}

	// 执行上传功能
	private void uploadFile(int i) throws FileNotFoundException, IOException {
		try {
			InputStream in = new FileInputStream(file.get(i));
			String dir = ServletActionContext.getRequest().getRealPath("/");
			System.out.println(dir);
			// String dir = "C:/";
			File uploadFile = new File(dir, this.getFileFileName().get(i));
			OutputStream out = new FileOutputStream(uploadFile);
			byte[] buffer = new byte[1024 * 1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		} catch (IOException ex) {
			logger.error("upload", ex);
		}
	}
}