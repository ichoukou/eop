package com.ytoec.uninet.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class HessianUtil {
	
	private static Logger logger=LoggerFactory.getLogger(HessianUtil.class);
	
	private static Properties properties ;
	
	static{
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("hessian.properties");
		} catch (IOException e) {
			logger.error("init PropertiesUtil error.", e);
		}
	}
	
	/**
	 * 获取上传图片大小限制，单位：Byte
	 * 
	 * @return
	 */
	public static int getUploadFileSizeLimit() {
		return Integer.parseInt(properties.getProperty("uploadFile.sizeLimit"));
	}
	
	/**
	 * 得到media 根url
	 * 
	 * @return
	 */
	public static String getDomain() {
		return properties.getProperty("domain");
	}
	/**
	 * 得到media 根url
	 * 
	 * @return
	 */
	public static String getMediaPath() {
		return properties.getProperty("mediaPath");
	}

	/**
	 * web服务器上传文件给file文件服务器的url
	 * 
	 * @return
	 */
	public static String getMediaHessianService() {
		return properties.getProperty("mediaHessianService");
	}
	
	/**
	 * 上传文件基本目录
	 * 
	 * @return
	 */
	public static String getFileUploadBasePath() {
		return properties.getProperty("fileUploadBasePath");
	}
	
	/**
	 * 压缩图片宽度
	 * 
	 * @return
	 */
	public static int getMediaDeskWidth() {
		int width = Integer.parseInt(properties.getProperty("deskWidth"));
		return width;
	}

	/**
	 * 压缩图片高度
	 * 
	 * @return
	 */
	public static int getMediaDeskHeight() {
		int height = Integer.parseInt(properties.getProperty("deskHeight"));
		return height;
	}
	
	/**
	 * 上传文件临时目录
	 * 
	 * @return
	 */
	public static String getFileUploadTempPath() {
		return getFileUploadBasePath() + "/" + "temp";
	}
	
	
}