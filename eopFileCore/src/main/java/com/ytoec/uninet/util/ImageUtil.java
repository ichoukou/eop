package com.ytoec.uninet.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ImageUtil {

	private static Log log = LogFactory.getLog(ImageUtil.class);

	private ImageUtil() {
	}

	/*
	 * 给定范围获得随机颜色
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}


	/**
	 * 图片压缩
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param width 目标文件宽度
	 * @param height 目标文件高度
	 * @throws Exception
	 */
	public static void resizeImage(String srcFile, String destFile,
			int width, int height) throws Exception{

		try {
			File sFile = new File(srcFile);
			//判断原文件是否为标准文件
			if (!sFile.isFile())
			{				
				log.error(sFile+ " is not image file error in CreateThumbnail!");
				throw new Exception("原文件不是标准文件"); //抛出异常，以便调用方处理
				
			}
			// 文件扩展名
			String extentionName = FilenameUtils.getExtension(srcFile);
			// 缩略图文件
			File saveFile = new File(destFile);
			if(!saveFile.getParentFile().exists()) //目录不存在
			{
				saveFile.getParentFile().mkdirs();
			}

			BufferedImage bi = ImageIO.read(sFile);
			// 假设图片宽 高 最大为130 80,使用默认缩略算法
			Image itemp = bi.getScaledInstance(width, height,
					BufferedImage.SCALE_DEFAULT);

			/* 等比例压缩 :以输入的较小值为准计算压缩比例*/
			double ratio = 0.0;
			if (height < width)
			{
				ratio = Double.valueOf(height) / bi.getHeight();
			}					
			else
			{
				ratio = Double.valueOf(width) / bi.getWidth();
			}				

			AffineTransformOp op = new AffineTransformOp(
					AffineTransform.getScaleInstance(ratio, ratio), null);
			itemp = op.filter(bi, null);
			ImageIO.write((BufferedImage) itemp, extentionName, saveFile);

		} catch (IOException e) {
			log.error("压缩图片异常：",e);
		}
		
	}

}
