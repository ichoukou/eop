<%@page import="java.net.URLEncoder"%>
<%@page import="net.ytoec.kernel.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@page import="javax.swing.filechooser.FileSystemView"%>
<%@page import="net.ytoec.kernel.util.FileUtil"%>
<%
    String fileName = "易通快捷方式" + ".url";
    String readPath = request.getRealPath("");
    String filePath = readPath+"\\"+fileName;
    //System.out.println(readPath);
    
    String content = FileUtil.readFileByUTF8(new File(filePath));
    //System.out.println(content);
    
    FileSystemView fsv = FileSystemView.getFileSystemView();
    String deskTopPath = fsv.getHomeDirectory().getPath();
    String shortCutPath = deskTopPath+"\\"+fileName;
    //System.out.println(deskTopPath);
    
    /* response.reset();
    response.setContentType("application/force-download");
    response.setCharacterEncoding("UTF-8");
    String outFileName = new String(fileName.getBytes("gbk"),"iso8859-1");
    response.addHeader("Content-Disposition", "attachment; fileName=\"" + outFileName + "\"");
    File inFile = new File(filePath);
    if(inFile.length() != 0){
	    response.setContentLength((int)inFile.length());
	    InputStream inStream = null;
	    ServletOutputStream servletOS = null;
		try{
		    inStream = new FileInputStream(inFile);
			byte[] buf = new byte[1024];
			servletOS = response.getOutputStream();
			int readLength;
			while (((readLength = inStream.read(buf)) != -1)) {
				servletOS.write(buf, 0, readLength);
			}
		}
		catch(Exception e){
		    //System.out.println(e.printStackTrace());
		}
		finally{
		    if(inStream != null){
				inStream.close();
				inStream = null;
		    }
		    if(servletOS != null){
				servletOS.flush();
				servletOS.close();
				servletOS = null;
		    }
			response.flushBuffer();
		}
    }  */
    response.setContentType("application/x-download");//设置为下载application/x-download  
    String filedownload = "/易通快捷方式.url";//即将下载的文件的相对路径  
    String outFileName = new String(fileName.getBytes("gbk"),"iso8859-1");
    response.addHeader("Content-Disposition","attachment;filename=" + outFileName);  
       
    try  
    {  
    RequestDispatcher dis = application.getRequestDispatcher(filedownload);  
    if(dis!= null)  
    {  
    dis.forward(request,response);  
    }  
    response.flushBuffer();  
    }  
    catch(Exception e)  
    {  
    e.printStackTrace();  
    }  
    finally  
    {  
       
    }  
    
     //以下为服务端测试代码。 
//     File outFile = new File(shortCutPath);
//     FileWriter fwriter=new FileWriter(outFile);
//     request.setCharacterEncoding("UTF-8");
//     fwriter.write(content);
//     fwriter.close();

%>

