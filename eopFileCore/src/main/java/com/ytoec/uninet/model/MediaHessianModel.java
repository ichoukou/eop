package com.ytoec.uninet.model;

import java.io.Serializable;

/**
 * 从web服务器传输对象到file服务器对象模型
 * @author michael
 *
 */
public class MediaHessianModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -451763091479263353L;
	/**
	 * 文件内容
	 */
	private byte[] file;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 上传分类
	 */
	private String category;
	/**
	 * 公司id
	 */
	private int companyId;
	
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}	
}
