package com.ytoec.uninet.service.impl;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianProxyFactory;
import com.ytoec.uninet.model.MediaHessianModel;
import com.ytoec.uninet.service.MediaHessianService;
import com.ytoec.uninet.util.DateUtil;
import com.ytoec.uninet.util.HessianUtil;
import com.ytoec.uninet.util.ImageUtil;
import com.ytoec.uninet.util.RandomNumUtil;


/**
 * 保存图片，产生缩略图
 * @author xupf
 *
 */
public class MediaHessianServiceImpl implements MediaHessianService {
	private static Logger log = LoggerFactory.getLogger(MediaHessianServiceImpl.class);
	/** 缩略图文件后缀 */
	private static final String ICON = "s0.jpg";
	
	/**
	 * 图片服务器本地代理
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public static MediaHessianService getMediaService() throws MalformedURLException {
		// 图片服务URL
		String url = HessianUtil.getMediaHessianService();
		// 获得图片服务本地代理
		HessianProxyFactory factory = new HessianProxyFactory();
		MediaHessianService mediaHessianService = (MediaHessianService) factory
		.create(MediaHessianService.class, url);
		return mediaHessianService;
	}
	
	/**
	 * 保存图片到临时文件目录，生成缩略图
	 * @param model 图片信息
	 * @return 图片URI
	 */
	public String transferImage(MediaHessianModel model) {
		// 随机文件名前缀
		String prefix = RandomNumUtil.getUUIDString();
		// 文件扩展名
		String extentionName = FilenameUtils.getExtension(model.getFileName());
		String fileName = prefix + "." + extentionName;

		// 图片存放URI
		String fileUri = getFileUri(model.getCategory(), model.getCompanyId()) + "/" + fileName;

		// 图片文件全路径
		String fileUrl = getTempPath() + fileUri;

		// 创建File实例
		File file = new File(fileUrl);
		// 检查存放文件的目录
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// 保存图片
		try {
			FileUtils.writeByteArrayToFile(file, model.getFile());
		} catch (IOException e1) {
			log.error("保存源文件失败！", e1);
		}
		// 缩略图全路径
		String destFile = fileUrl + "." + ICON;
		try {
			ImageUtil.resizeImage(fileUrl, destFile, HessianUtil.getMediaDeskWidth(), HessianUtil.getMediaDeskHeight());
		} catch (Exception e2) {
			log.error("缩略图生成失败！", e2);
		}
		return fileUri;
	}
	/**
	 * 上传文件正式目录
	 * @return
	 */
	public String getBasePath() {
		return HessianUtil.getFileUploadBasePath();
	}
	/**
	 * 上传文件临时目录
	 * @return
	 */
	public String getTempPath(){
		return HessianUtil.getFileUploadTempPath();
	}

	/**
	 * 获取图片存放路径
	 * 
	 * @param category
	 *            分类目录
	 * @param companyId
	 *            公司id
	 * @return URI
	 */
	private String getFileUri(String category, int companyId) {
		StringBuilder imgPath = new StringBuilder("/");

		imgPath.append(category);// 图片分类目录
		imgPath.append("/");
		imgPath.append(companyId); // 企业图片空间根目录
		imgPath.append("/");
		imgPath.append(DateUtil.format(new Date(), "yyyyMMdd"));

		return imgPath.toString();
	}

	/**
	 * 将文件移动到图片根目录
	 * @param urls 源文件文件路径
	 * @throws IOException 
	 */
	public void copyFileToBasePath(String[] urls) {
		if(urls != null){
			for(int i = 0 ;i < urls.length; i++){
				//原始大小文件copy
				File srcFile = new File(getTempPath()+urls[i]);
				File destFile = new File(getBasePath()+urls[i]);
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					log.error("复制第"+i+"个源文件["+srcFile+"]出错!",e);
				}
				//缩略文件copy
				srcFile = new File(getTempPath()+urls[i]+"."+ICON);
				destFile = new File(getBasePath()+urls[i]+"."+ICON);
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					log.error("复制第"+i+"个缩略文件["+srcFile+"]出错!",e);
				}
			}
		}				
	}
	
	/**
	 * 文件是否在根目录，存在返回true,否则返回false
	 * @param url : 相对路径
	 */
	public boolean exist(String url) {
		boolean flag = false;
		File file = new File(getBasePath()+url);
		if(file.exists()){
			flag = true;
		}
		return flag;
	}
}
