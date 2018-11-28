package net.ytoec.kernel.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class ShortCutServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(ShortCutServlet.class);
    /**
     * 
     */
    private static final long serialVersionUID = -1095839708359002563L;
    
    public void service(HttpServletRequest request, HttpServletResponse response){
        String fileName = "易通快捷方式" + ".url";
        String readPath = request.getSession().getServletContext().getRealPath("");
        String filePath = readPath+"/"+fileName;
        //String content = FileUtil.readFileByUTF8(new File(filePath));
        log.error(filePath);
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        String outFileName = "易通.url";
        try {
            outFileName = new String(outFileName.getBytes("gbk"),"iso8859-1");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        response.addHeader("Content-Disposition", "attachment; fileName=\"" + outFileName + "\"");
        File inFile = new File(filePath);
        InputStream inStream = null;
        ServletOutputStream servletOS = null;
        if(inFile.length() != 0){
            response.setContentLength((int)inFile.length());
            try{
                inStream = new FileInputStream(inFile);
                byte[] buf = new byte[1024];
                servletOS = response.getOutputStream();
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    servletOS.write(buf, 0, readLength);
                }
                inStream.close();
                servletOS.flush();
                servletOS.close();
                response.flushBuffer();
            }
            catch(Exception e){
                log.error("下载快捷方式时出错：");
                e.printStackTrace();
            }
            finally{
                if(inStream != null){
                    inStream = null;
                }
                if(servletOS != null){
                    servletOS = null;
                }
            }
        } 
    }
}
