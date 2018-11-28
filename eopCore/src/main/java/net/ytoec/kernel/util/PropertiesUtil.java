package net.ytoec.kernel.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import net.ytoec.kernel.constains.StringConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties properties;
	static {
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("eop.properties");
		} catch (IOException e) {
			logger.error("init PropertiesUtil error.", e);
		}
	}
	
	/**
	 * 根据key获取对应的值
	 */
	public static String getValue(String key){
		return properties.getProperty(key);
	}
	
	/**
	 * 获取“我的应用”模块中“最新动态”的显示数量
	 * @return
	 */
	public static Integer getAppNewsSize(){
		return Integer.parseInt(properties.getProperty("app_news_size"));
	}
	
	/**
	 * 获取“我的应用”模块中“常见问题”的显示数量
	 * @return
	 */
	public static Integer getAppFAQSize(){
		return Integer.parseInt(properties.getProperty("app_faq_size"));
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
		return getFileUploadBasePath() + StringConstant.FORWARD_SLASH + "temp";
	}
	
	/**
	 * 获取验证码长度
	 * 
	 * @return
	 */
	public static int getRandomVldNumLength() {
		return Integer.parseInt(properties.getProperty("randomVldNum.length"));
	}

	public static int getRandomVldNumWidth() {
		return Integer.parseInt(properties.getProperty("randomVldNum.width"));
	}

	public static int getRandomVldNumHeight() {
		return Integer.parseInt(properties.getProperty("randomVldNum.height"));
	}

	public static int getRandomVldNumLine() {
		return Integer.parseInt(properties.getProperty("randomVldNum.line"));
	}
	
	// 根据key读取value
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.load(in);
			String value = props.getProperty(key);
			logger.debug(key + value);
			return value;
		} catch (Exception e) {
			
			return null;
		}
	}

	// 读取properties的全部信息
	@SuppressWarnings("rawtypes")
	public static void readProperties(String filePath) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				logger.debug(key +" "+ Property);
			}
		} catch (Exception e) {
		}
	}

	// 写入properties信息
	public static void writeProperties(String filePath, String parameterName,
			String parameterValue) {
		Properties prop = new Properties();
		try {
			InputStream fis = new FileInputStream(filePath);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(fis);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(filePath);
			prop.setProperty(parameterName, parameterValue);
			// 以适合使用 load 方法加载到 Properties表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			System.err.println("Visit " + filePath + " for updating "
					+ parameterName + " value error");
		}
	}
	public static String getputh() {
			String classpath = Thread.currentThread().getContextClassLoader()
			.getResource("/").toString();
		classpath = classpath.replace("file:/", "/");
		return classpath;
	}
	
    public static void main(String[] args) {
		readValue("d:\\timer.properties", "start");
		writeProperties("d:\\timer.properties", "start", "2012-02-02 10:00:00");
		writeProperties("d:\\timer.properties", "end", "2012-05-02 10:00:00");
		readProperties("d:\\timer.properties");
		logger.debug("OK");
	}
}