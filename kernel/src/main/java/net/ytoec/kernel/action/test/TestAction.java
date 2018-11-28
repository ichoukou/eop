package net.ytoec.kernel.action.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.AbstractInterfaceAction;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dto.BranchMailNoDTO;
import net.ytoec.kernel.dto.QuestionnaireResultsUpdateDTO;
import net.ytoec.kernel.service.QuestionnaireService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 测试接口
 * 
 * @author mgl
 * @2011-8-18
 */
@Controller
@Scope("prototype")
public class TestAction extends AbstractInterfaceAction {

	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(TestAction.class);
	private static final long serialVersionUID = -115598032813210843L;
	
	@Autowired
    private QuestionnaireService<QuestionnaireResultsUpdateDTO> questionnaireService;
	
    public static String requestSwitch(String sw){
    	List ips=new ArrayList();
//		ips.add("127.0.0.1:8080");
//		ips.add("10.1.198.71/kernel");
		ips.add("10.1.205.17:7810");
		ips.add("10.1.205.17:7811");
		ips.add("10.1.205.17:7812");
		ips.add("10.1.205.17:7813");
		ips.add("10.1.205.18:7810");
		ips.add("10.1.205.18:7811");
	    ips.add("10.1.205.18:7812");
		ips.add("10.1.205.18:7813");
		for(int i=0;i<ips.size();i++){
    	XmlSender xmlSender = new XmlSender();
    	String url = "http://"+ips.get(i)+"/test.action";
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);   
		xmlSender.setRequestParams("switch=" + ("close".equals(sw)?"0":"1"));
		String result = xmlSender.send();
		logger.debug(result);
		if(!"true".equals(result))
			return "false"; 
		}
    	return "true";
    } 
	/**
	 * 解析接口调用,金刚调用核心平台.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String testServlet() throws Exception {
		
		if(request.getParameter("download")!=null){
			try {
				
				String path=Thread.currentThread().getContextClassLoader()
				.getResource("/").getPath().toString();
		         System.out.println(path);
//				  String path = request.getSession().getServletContext().getRealPath("/");
				  // 设置为下载application/x-download
				  response.setContentType("application/x-download");
				  // 即将下载的文件在服务器上的绝对路径
				  String filenamedownload =path+"test.xml";
				  // 下载文件时显示的文件保存名称
				  String filenamedisplay = "test.xml";
				  // 中文编码转换
				  filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");
				  response.addHeader("Content-Disposition", "attachment;filename="
				    + filenamedisplay);
				  try {
				   java.io.OutputStream os = response.getOutputStream();
				   java.io.FileInputStream fis = new java.io.FileInputStream(
				     filenamedownload);
				   byte[] b = new byte[1024];
				   int i = 0;
				   while ((i = fis.read(b)) > 0) {
				    os.write(b, 0, i);
				   }
				   fis.close();
				   os.flush();
				   os.close();
				  } catch (Exception e) {
                     e.printStackTrace();
				  }
			  return null;
		}catch (Exception e) {
			e.printStackTrace();
		}
		}
		if (request.getParameter("jiemi")!=null) {
			String str=request.getParameter("jiemi");
//			super.print(decode(str,XmlSender.UTF8_CHARSET));
			response.setCharacterEncoding("UTF-8");
    		response.getWriter().print(decode(str,XmlSender.UTF8_CHARSET));
			return null;
		}

        logger.debug("now switch is "+ProcessorUtils.PROCESS_SWITCH);

		if(request.getParameter("switch")!=null){
			if("open".equals(request.getParameter("switch"))){
				requestSwitch("open");
			}else if("close".equals(request.getParameter("switch"))){
				requestSwitch("close");
			}else if("0".equals(request.getParameter("switch"))){
				ProcessorUtils.PROCESS_SWITCH=false;
				logger.debug("after switch is "+ProcessorUtils.PROCESS_SWITCH);
				super.print("true");
			}else if("1".equals(request.getParameter("switch"))) {
				ProcessorUtils.PROCESS_SWITCH=true;
				logger.debug("after switch is "+ProcessorUtils.PROCESS_SWITCH);
				super.print("true");
			}
			
		}
        if(request.getParameter("toTB")!=null){
    		String logisticsInterface = request
			.getParameter("logistics_interface");
	        String dataDigest = request.getParameter("data_digest");
        	XmlSender xmlSender = new XmlSender();
        	String url = "http://110.75.120.136/consign/Logistics_test_receive_message.do?_input_charset=UTF-8";
        	url="http://wuliu.daily.taobao.net/user/express_message_receiver.do?_input_charset=UTF-8";
    		xmlSender.setUrlString(url);
    		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);   
    		xmlSender.setRequestParams("logistics_interface=" + logisticsInterface + "&data_digest=" + dataDigest+ "&type=online");
    		String result = xmlSender.send();
    		logger.debug(result);
    		response.getWriter().print(URLDecoder.decode(result, "UTF-8"));
    		return null;
        }
        if(request.getParameter("toKG")!=null){
    		String logisticsInterface = request
			.getParameter("logistics_interface");
	       String dataDigest = request.getParameter("data_digest");
        	XmlSender xmlSender = new XmlSender();
        	String url = "http://jingangtest.yto56.com.cn/ordws/"+request.getParameter("toKG");
//        	url="http://10.1.5.11/ordws/TaoBao14OrderServlet";
        	xmlSender.setUrlString(url);
    		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);   
    		xmlSender.setRequestParams("logistics_interface=" + logisticsInterface + "&data_digest=" + dataDigest);
    		String result = xmlSender.send();
    		logger.debug(result);
    		response.getWriter().print(result);
    		return null;
        }
		if (request.getParameter("textContent")!=null) {
			
			String str=request.getParameter("textContent");
			String jiami=request.getParameter("jiami");
			super.print("[UTF-8编码]<br>"
					+"明文：<br>"
					+encode(str,XmlSender.UTF8_CHARSET)
					+"<br>密文：<br>"
					+encode(Md5Encryption.MD5Encode(str+jiami),XmlSender.UTF8_CHARSET));

			return null;
		}
		return "success";
	}
	
	/**
	 * 核心平台调用金刚！
	 */
	
	private QuestionnaireResultsUpdateDTO ques;
	
	
    public QuestionnaireResultsUpdateDTO getQues() {
        return ques;
    }
    
    public void setQues(QuestionnaireResultsUpdateDTO ques) {
        this.ques = ques;
    }
    public void toJingangServlet() throws Exception {
	    if ("questionnaire".equals(request.getParameter("type"))) {

	        questionnaireService.updateQuestionnaireResults(ques,true);
        }
	    
	    
	    if ("mailNo".equals(request.getParameter("type"))) {
	        BranchMailNoDTO mailDto = new BranchMailNoDTO();
	        String mailNoStrings = request.getParameter("mailNoStrings");
	        
	        
	        String[] mailNo = mailNoStrings.split(";");
	        List<BranchMailNoDTO> list = questionnaireService.searchOrgCodeByMailNo(mailNo);
	        
	        if (list == null || Collections.EMPTY_LIST == list) {
	            logger.error("查询失败");
	            return;
	        }
	        StringBuilder builder = new StringBuilder();
	        for (int i = 0; i < list.size(); i++) {
                mailDto = (BranchMailNoDTO)list.get(i);
                builder.append("mailNo:"+mailDto.getMailNo()+"  orgCode:" +mailDto.getOrgCode()+"   "+mailDto.getFailed()+ "<br>");
                logger.debug("mailNo:"+mailDto.getMailNo()+"  orgCode:" +mailDto.getOrgCode()+"   "+mailDto.getFailed());
            }
	        logger.debug("结果数："+list.size());
	        super.print(builder.toString());
        }
	    
	}

	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	private static String decode(String arg, String charset) {
		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String viewMemData() throws IOException{

		 Set<Entry<String, Integer>> set = Resource.requestMap.entrySet();   
		 for(Entry<String, Integer> entry: set)
	        {  
         super.print("内存数据:<br>"
   				+"requestMap键为："
   				+entry.getKey()
   				+"<br>"
   				+"requestMap值为: "
   				+entry.getValue());  		
	}
		 return "success";
	}
}
