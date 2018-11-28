package net.ytoec.kernel.action.pay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dao.SMSOtherWaitDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSOtherWaitService;
import net.ytoec.kernel.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.oscache.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * admin 上传文件类
 * 作用：供其他模块发送短信
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class AdminUploadAction extends AbstractActionSupport{

	private File file; //上传的文件
    private String fileFileName; //文件名称
    private String fileContentType; //文件类型

	private static Logger logger = Logger.getLogger(AdminUploadAction.class);
    
    private String messageContent; //手机内容
    private String smsType;//模块类型
 // 文件打包下载部分
	private String downLoadPaths;
	private OutputStream res;
	private ZipOutputStream zos;
    
	@Inject
    private SMSOtherWaitService<SMSObject> smsOtherWaitService;
	@Inject
	private SMSOtherWaitDao<SMSObject> sMSOtherWaitDao;
    
    
    /**
     * 上传文件
     * @return
     * @throws Exception
     */
    public String upload() throws Exception {
        String realpath = ServletActionContext.getServletContext().getRealPath(File.separator + "upload");
        
        logger.error("realpath: "+realpath);
        if (file != null) {
        	// 1.接收客户端上传的文件
            File savefile = new File(new File(realpath), fileFileName);
            String extension=fileFileName.substring(fileFileName.lastIndexOf("."));
         if(extension.equals(".xls")){
            if (!savefile.getParentFile().exists())
            {
                savefile.getParentFile().mkdirs();
            }
            
            FileUtils.copyFile(file, savefile);
            logger.error(fileFileName+"文件上传成功"); 
                       
            
            //2.将上传的文件导入到临时表中ec_core_phone_temp(备注：临时表的列头可随意扩展)
            //DbMySqlHelper.loadDataInfile(savefile, "ec_core_phone_temp", "dest_mobile,receive_name");
            
            //3.将上传的文件删除掉
            //savefile.delete();
            //logger.error(fileFileName+"文件删除成功");
//            BufferedReader br=new BufferedReader(
//    				new InputStreamReader(new FileInputStream(savefile),"utf-8"));// 编码方式为utf-8，txt保存时编码方式也要选择
//    		String line;
//    		boolean flag;
//    		try{
//	    		while((line=br.readLine())!=null){
//	    			String phone=line.trim();
//	    		    flag=sMSOtherWaitDao.insertPhone(phone);
//	    		}
//    		}
//    		catch(IOException ex){
//    			logger.debug("读取文件失败", ex);
//    		}finally{
//    			br.close();
//    		}
          //1. 删除ec_core_sms_wait表中所有status==8待发送短信
            boolean flag=true;
            boolean flag2=true;
    		flag = sMSOtherWaitDao.deleteByStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
    		if(flag==false) {
    			ActionContext.getContext().put("message1", "删除待发送短信失败了！");
    			logger.error("删除ec_core_sms_wait表中所有status==8待发送短信失败了");
    			return "cuccess";
    		}
    		try{
    			String[][] result = getData(savefile, 1);
    		
            int rowLength = result.length;
            SMSObject sms=new SMSObject();
            List<SMSObject> SMSlist=new ArrayList<SMSObject>();
            //初始化序列值
        	int maxIdSequence=1;
            for(int i=0;i<rowLength;i++) {
            	sms=new SMSObject();
            	Random r1=new Random();
            	int x = r1.nextInt(89999+1000);
            	int x1= r1.nextInt(12345+3000);
            	sms.setSequenceID(x+x1);
            	int count=1;
                for(int j=0;j<result[i].length-1;j++) {
                   if(count==1){
                	   if(result[i][j].trim().isEmpty()){
                		   ActionContext.getContext().put("message1", "第"+(i+1)+"行手机号不得为空,请重新上传！");
                		   return "success";
                	   }else if(result[i][j].trim().length()>25){
                		   ActionContext.getContext().put("message1", "第"+(i+1)+"行手机号过长,请重新上传！");
                		   return "success";
                	   }else{
	                	   sms.setDestMobile(result[i][j].trim());
	                	   count --;
                	   }
                   }else{
                	   if(result[i][j].trim().isEmpty()){
                		   ActionContext.getContext().put("message1", "第"+(i+1)+"行短信内容不得为空,请重新上传！");
                		   return "success";
                	   }else if(result[i][j].trim().length()>=999){
                		   ActionContext.getContext().put("message1", "第"+(i+1)+"行短信内容过长,请重新上传！");
                		   return "success";
                	   }else{
                		 //计算实际发送的短信数
                   		int amount = result[i][j].trim().length();
                   		if(amount<=70) {
                   			amount = 1;
                   		}else{
                   			amount = amount/67+1;
                   		}  
                   		   sms.setPkTotal(amount);
	                	   sms.setMessageContent(result[i][j].trim());
	                	   count ++;
                	   }
                   }
                }
                SMSlist.add(sms);
                if(SMSlist.size() == 100){
                	for(SMSObject smslist:SMSlist){
                		//flag=sMSOtherWaitDao.insertPhone(smslist);
                		int smsId=smsOtherWaitService.insertPhone(smslist);
                    	//更新当前插入记录的序列
                    	flag2=smsOtherWaitService.updatesequenceID(smslist);
                    	if(!flag){
                        	ActionContext.getContext().put("message1", "上传失败！");
                        	return "success";
                        }
                    	if(!flag2){
                    		ActionContext.getContext().put("message1", "更新序列失败！");
                        	return "success";
                    	}
                    }
                	SMSlist.removeAll(SMSlist);
                }
                
            }
            
            
            for(SMSObject smslist:SMSlist){
            	try{
            		smsOtherWaitService.insertPhone(smslist);
            		//更新当前插入记录的序列
            		flag2=smsOtherWaitService.updatesequenceID(smslist);
            	}catch(Exception e){
            		logger.error("上传失败！"+e.getCause().getMessage());
            		ActionContext.getContext().put("message1", "上传失败！");
                	return "success";
            	}
            }
    		}
    		catch(IOException ex){
    			logger.debug("读取文件失败"+ ex.getCause().getMessage());
    			ActionContext.getContext().put("message1", "读取文件失败！");
    		}
        
            ActionContext.getContext().put("message1", "上传成功"); 
            //3.将上传的文件删除掉
            savefile.delete();
        }
        else{
      	    ActionContext.getContext().put("message1", "文件类型错误！");
        }
        }else {
        	ActionContext.getContext().put("message1", ""); 
        }
        return "success";
        
    }
    
    /**
	 * 文件下载入口
	 * */
	public String downLoadZip() throws Exception {
		
			preProcess();// 进行预处理
		
		writeZip(downLoadPaths);// 处理
		afterProcess();// 后处理关闭流
		// return "downLoadZip";
		// return toOrderImoprt();
		return "success";
	}
	
	/**
	 * 文件下载前的准备，设置文件头等等
	 * */
	public void preProcess() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		res = response.getOutputStream();
		String fname = "admin上传短信模板.zip";
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
		} else {
			fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
		}
		response.reset();// 清空输出流
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fname);// 设定输出文件头
		response.setContentType("application/zip");
		zos = new ZipOutputStream(res);
	}
	/**
	 * 根据文件的地址来压缩文件
	 * 
	 * @param downLoadPaths文件地址数组
	 * */
	public void writeZip(String downLoadPaths) throws IOException {
		byte[] buf = new byte[4096];
		int len;
		zos.setEncoding("gb2312");
		
			String classpath = FileUtil.getClassRoot();
			logger.error("classpath:" + classpath + downLoadPaths);
			File file = new File(classpath + downLoadPaths);
			ZipEntry ze = new ZipEntry(file.getName());// apache的ant.jar的ZipEntry
			zos.putNextEntry(ze);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while ((len = bis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.flush();
			zos.closeEntry();
			bis.close();
		
	}
	/**
	 * 关闭输入输出流，清理垃圾
	 * */
	public void afterProcess() throws IOException {
		zos.close();
		res.close();
	}
	
	
    //1. 删除ec_core_sms_wait表中所有status==8待发送短信 
    //2. 将临时表ec_core_phone_temp中的数据去除重复的手机号后导入待发送短信表ec_core_sms_wait
    //3. 更新短信内容
    public String updateContent() {
    	boolean bool = smsOtherWaitService.updateMessageContent(this.smsType);//smsType模块类型  
    	if(bool)
    	    ActionContext.getContext().put("message2", "短信内容操作成功"); 
    	else
    		ActionContext.getContext().put("message2", ""); 
    	
    	return "toOrderImoprt";
    }
    
    
    /**

     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行

     * @param file 读取数据的源Excel

     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1

     * @return 读出的Excel中数据的内容

     * @throws FileNotFoundException

     * @throws IOException

     */
   
    public static String[][] getData(File file, int ignoreRows)

           throws FileNotFoundException, IOException {
    	List<String[]> result = new ArrayList<String[]>();

        int rowSize = 0;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(

               file));

        // 打开HSSFWorkbook

        POIFSFileSystem fs = new POIFSFileSystem(in);

        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFCell cell = null;

        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

            HSSFSheet st = wb.getSheetAt(sheetIndex);

            // 第一行为标题，不取

            for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

               HSSFRow row = st.getRow(rowIndex);

               if (row == null) {

                   continue;

               }

               int tempRowSize = row.getLastCellNum() + 1;

               if (tempRowSize > rowSize) {

                   rowSize = tempRowSize;

               }

               String[] values = new String[rowSize];

               Arrays.fill(values, "");

               boolean hasValue = false;

               for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                   String value = "";

                   cell = row.getCell(columnIndex);

                   if (cell != null) {

                      // 注意：一定要设成这个，否则可能会出现乱码

//                      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                	  
                      switch (cell.getCellType()) {

                      case HSSFCell.CELL_TYPE_STRING:

                          value = cell.getStringCellValue();

                          break;

                      case HSSFCell.CELL_TYPE_NUMERIC:

                          if (HSSFDateUtil.isCellDateFormatted(cell)) {

                             Date date = cell.getDateCellValue();

                             if (date != null) {

                                 value = new SimpleDateFormat("yyyy-MM-dd")

                                        .format(date);

                             } else {

                                 value = "";

                             }

                          } else {

                             value = new DecimalFormat("0").format(cell

                                    .getNumericCellValue());

                          }

                          break;

                      case HSSFCell.CELL_TYPE_FORMULA:

                          // 导入时如果为公式生成的数据则无值

                          if (!cell.getStringCellValue().equals("")) {

                             value = cell.getStringCellValue();

                          } else {

                             value = cell.getNumericCellValue() + "";

                          }

                          break;

                      case HSSFCell.CELL_TYPE_BLANK:

                          break;

                      case HSSFCell.CELL_TYPE_ERROR:

                          value = "";

                          break;

                      case HSSFCell.CELL_TYPE_BOOLEAN:

                          value = (cell.getBooleanCellValue() == true ? "Y"

                                 : "N");

                          break;

                      default:

                          value = "";

                      }

                   }

                   if (columnIndex == 0 && value.trim().equals("")) {

                      break;

                   }

                   values[columnIndex] = rightTrim(value);

                   hasValue = true;

               }

  

               if (hasValue) {

                   result.add(values);

               }

            }

        }

        in.close();

        String[][] returnArray = new String[result.size()][rowSize];

        for (int i = 0; i < returnArray.length; i++) {

            returnArray[i] = (String[]) result.get(i);

        }

        return returnArray;
    }
    
    /**

     * 去掉字符串右边的空格

     * @param str 要处理的字符串

     * @return 处理后的字符串

     */

     public static String rightTrim(String str) {

       if (str == null) {

           return "";

       }

       int length = str.length();

       for (int i = length - 1; i >= 0; i--) {

           if (str.charAt(i) != 0x20) {

              break;

           }

           length--;

       }

       return str.substring(0, length);

    }
     public static void main(String[] args) throws Exception {

         File file = new File("C:\\Users\\Administrator\\Downloads\\test1.xls");

         String[][] result = getData(file, 1);

         int rowLength = result.length;

         for(int i=0;i<rowLength;i++) {
        	 int count=1;
             for(int j=0;j<result[i].length-1;j++) {
                if(count==1){
                	System.out.print(result[i][j]+"\t\t");
             	   count --;
                }else{
                	System.out.print(result[i][j]+"\t\t");
             	   count ++;
                }

             }

         }

      }

    
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
    
    public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	public String getSmsType() {
		return smsType;
	}

    public void setSmsType(String smsType) {
	    this.smsType = smsType;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AdminUploadAction.logger = logger;
	}

	public String getDownLoadPaths() {
		return downLoadPaths;
	}

	public void setDownLoadPaths(String downLoadPaths) {
		this.downLoadPaths = downLoadPaths;
	}

	public OutputStream getRes() {
		return res;
	}

	public void setRes(OutputStream res) {
		this.res = res;
	}

	public ZipOutputStream getZos() {
		return zos;
	}

	public void setZos(ZipOutputStream zos) {
		this.zos = zos;
	}

	public SMSOtherWaitService<SMSObject> getSmsOtherWaitService() {
		return smsOtherWaitService;
	}

	public void setSmsOtherWaitService(
			SMSOtherWaitService<SMSObject> smsOtherWaitService) {
		this.smsOtherWaitService = smsOtherWaitService;
	}

	public SMSOtherWaitDao<SMSObject> getsMSOtherWaitDao() {
		return sMSOtherWaitDao;
	}

	public void setsMSOtherWaitDao(SMSOtherWaitDao<SMSObject> sMSOtherWaitDao) {
		this.sMSOtherWaitDao = sMSOtherWaitDao;
	}
 }
