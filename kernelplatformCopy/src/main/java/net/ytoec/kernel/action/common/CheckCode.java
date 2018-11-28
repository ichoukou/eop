/**
 * CheckCode.java
 * Wangyong
 * 2011-8-4 下午12:26:07
 */
package net.ytoec.kernel.action.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 验证码实现类
 * @author Wangyong
 * @2011-8-4 net.ytoec.kernel.action.common
 */
@SuppressWarnings("restriction")
public class CheckCode extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");
		String checkCode = StringTool.randomChars(4);// 生成验证码
		request.getSession().setAttribute("c", checkCode);// 验证码存入session中
		int width = 65;
		int height = 20;
		int startX = 8;
		int startY = 15;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics2D g = bi.createGraphics();
		//Random random = new Random();

		g.setBackground(Color.decode("#a5daf2"));
		g.clearRect(0, 0, width, height);
		g.setFont(new Font("Agency FB", Font.HANGING_BASELINE, 16));
//		for (int i = 0; i < 155; i++) {
//			int x = random.nextInt(width);
//			int y = random.nextInt(height);
//			int xl = random.nextInt(12);
//			int yl = random.nextInt(12);
//			g.drawLine(x, y, x + xl, y + yl);
//		}
//		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.setColor(Color.black);
		g.drawString(checkCode, startX, startY);
		g.dispose();
		JPEGImageEncoder encoder = null;
		JPEGEncodeParam param = null;
		try {
			encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
			param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(0.9f, false);
			encoder.encode(bi);
		} catch (Exception e) {
			
		} finally {
			bi = null;
			g = null;
			checkCode = null;
			encoder = null;
			param = null;
		}

	}
}
