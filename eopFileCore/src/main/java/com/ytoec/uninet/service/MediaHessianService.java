package com.ytoec.uninet.service;

import java.io.IOException;

import com.ytoec.uninet.model.MediaHessianModel;


public interface MediaHessianService {
	/**
	 * 把图片从web服务器传输到文件服务器，返回文件url
	 * @param model
	 * @return
	 */
	public String transferImage(MediaHessianModel model);
	
	/**
	 * 将文件移动到图片根目录
	 * @param url 源文件文件路径
	 */
	public void copyFileToBasePath(String[] urls) throws IOException;
	
	/**
	 * 判断文件是否存在正式根目录下
	 * @param url
	 * @return
	 */
	public boolean exist(String url);
}
