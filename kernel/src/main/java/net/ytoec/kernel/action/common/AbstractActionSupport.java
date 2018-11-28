package net.ytoec.kernel.action.common;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadContractService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.EncryptionDecryption;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

public class AbstractActionSupport extends ActionSupport implements ServletRequestAware, ServletResponseAware, ParameterAware, SessionAware, ApplicationAware {

    private static Logger                 logger           = Logger.getLogger(AbstractActionSupport.class);
    private static final long             serialVersionUID = 7537643286383030985L;
    protected HttpServletRequest          request;
    protected HttpServletResponse         response;
    protected Map<String, Object>         session; 
    protected Map<String, String[]>       parameters;
    protected Map<String, Object>         application;

    protected static Integer              pageNum          = 10;                                           // 每页数据记录数

    @Inject
    private UserService<User>             userService;
    @Inject
    private UserThreadService<UserThread> userThreadService;
    @Inject
    private UserThreadContractService<UserThreadContract> userThreadContractService;

    private JsonResponse jsonRes;
    private String menuFlag;

    private static EncryptionDecryption ed = null;

    private final static String[] ignoreFields = {"telAreaCode","telCode","telExtCode","sex","cardType","cardNo"
                             ,"userLevel","createUser","createUser","createUser","createTime","serialVersionUID"
                             ,"updateTime","remark","deptName","deptCode","DeptPhone"
                             ,"DeptAddr","HrCanceled","Canceled","Dr","appProvider"};
    private static AbstractActionSupport aas = null;
    
    public static AbstractActionSupport getInstance(){
        if(aas == null){
            aas = new AbstractActionSupport();
        }
        return aas;
    }
    
    /**
     * 管理员的用户列表
     * 
     * @param currentUser
     * @return
     */
    protected List<User> getClientUser(User currentUser) {
        List<User> vipList = new ArrayList<User>();
        /**
         * 管理员用户获取所有卖家包括平台用户
         */
        if (currentUser.getUserType() != null
            && (currentUser.getUserType().equals("3") || currentUser.getUserType().equals("5"))) {
            /**
             * vipList为所有卖家用户
             */
            vipList = userService.getUserListByUserType("1");
            if (userService.getUserListByUserType("4") != null) {// 获取平台用户
                List<User> list = userService.getUserListByUserType("4");
                for (User user : list) {
                    vipList.add(user);
                }
            }
        }
        return vipList;
    }

    /**
     * 网点从直客信息中获取用户列表
     */
    protected List<UserThread> getZhiKeUser(User currentUser) {
        List<UserThread> vipThreadList = new ArrayList<UserThread>();
        List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
        String usertype = currentUser.getUserType();
        if ("2".equals(usertype) || "21".equals(usertype) || "22".equals(usertype) || "23".equals(usertype)) {
            
            contractList = userThreadContractService.getContractersByUserNameAndType(currentUser.getUserName(),currentUser.getUserType());
            if(contractList.size() == 0){
            	//承包区，客服，财务没有分配客户的子账号
	           	 if(currentUser.getParentId() != null){
	           		 return null;
	           	 }
	           	 
            	 vipThreadList = userThreadService.searchUsersBySite(currentUser.getSite());
            	 
            	 //当网点登陆的时候，点击财务管理的功能，显示客户列表的时候要去除掉已经是承包区的客户
            	 if("financialManager".equals(currentUser.getFinancialManager())){
            		 //去除没有打开电子对账的直客
            		 for(int i=0; vipThreadList != null && i<vipThreadList.size(); i++){
            			 if(!"0".equals(vipThreadList.get(i).getSwitchEccount())){
        					 vipThreadList.remove(i);
        					 i--;
        					 continue;
        				 }
            		 }
            		 //已经承包出去的承包区客户
            		 List<UserThreadContract> siteContractList = userThreadContractService.searchContractsBysiteId(currentUser.getId()+"","2");
            		 if(siteContractList != null && siteContractList.size() != 0){
            			 for(int j=0; j<vipThreadList.size(); j++){
                			 for(int k=0; k<siteContractList.size(); k++){
                				 if(siteContractList.get(k).getConractAreaId().equals(vipThreadList.get(j).getId())){
                					 vipThreadList.remove(j);
                					 j--;
                					 break;
                				 }
                			 }
                		 }
            		 }
            		 
            		 //去除未拥有已经激活账号（客户编码已经被卖家绑定）
            		 if("unlikefreight".equals(currentUser.getUnlikefreight())){
            			 for(int j=0; j<vipThreadList.size(); j++){
                			 UserThread ut = vipThreadList.get(j);
                			 List<User> userList = userService.searchUsersByCodeTypeState(ut.getUserCode(), "1", "1");
                			 if(userList!=null && userList.size()>0){
             					Set<String> maijiaName = new HashSet<String>();
             					for(User u:userList){
             						if(u.getUserName()!=null && !(u.getUserName().equals("")))
             							maijiaName.add(u.getUserName());
             					}
             					if(maijiaName.size() == 0){
             						vipThreadList.remove(j);
             						j--;
             						continue;
             					}
                			 }else{
                				vipThreadList.remove(j);
          						j--;
          						continue;
                			 }
                		 }
            		 }
            			
            	 }
            }else{
            	//承包区，客服，财务分配客户的子账号
            	 for(int i=0; i<contractList.size(); i++){
            		 UserThread ut = userThreadService.getByIdAndState(contractList.get(i).getConractAreaId());
            		 if("financialManager".equals(currentUser.getFinancialManager())){
            			//去除没有打开电子对账的直客
                		 if(ut != null && !"0".equals(ut.getSwitchEccount())){
                			 contractList.remove(i);
        					 i--;
        					 continue;
        				 }
            		 }
            		 
            		//去除未拥有已经激活账号（客户编码已经被卖家绑定）
            		 if("unlikefreight".equals(currentUser.getUnlikefreight())){
            			 if(ut != null && ut.getUserCode() != null &&  !"".equals(ut.getUserCode())){
            				 List<User> userList = userService.searchUsersByCodeTypeState(ut.getUserCode(), "1", "1");
                			 if(userList!=null && userList.size()>0){
             					Set<String> maijiaName = new HashSet<String>();
             					for(User u:userList){
             						if(u.getUserName()!=null && !(u.getUserName().equals("")))
             							maijiaName.add(u.getUserName());
             					}
             					if(maijiaName.size() == 0){
             						contractList.remove(i);
             						i--;
             						continue;
             					}
                			 }else{
                				 contractList.remove(i);
            					 i--;
            					 continue;
                			 }
            			 }else {
            				 contractList.remove(i);
        					 i--;
        					 continue;
            			 }
            			 
            		 }
            		 
            		
            		 if(ut != null){
            			 vipThreadList.add(ut);
            		 }
            	 }
            	
            }
        }
        return vipThreadList;
    }
    
    /**
     *  面单打印处网点获取已授权的直客列表
     */
    protected List<UserThread> getZhiKeUserAtOrderPrint(User currentUser) {
        List<UserThread> vipThreadList = new ArrayList<UserThread>();
        List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
        String usertype = currentUser.getUserType();
        if ("2".equals(usertype) || "21".equals(usertype) || "22".equals(usertype) || "23".equals(usertype)) {
            
            contractList = userThreadContractService.getContractersByUserNameAndType(currentUser.getUserName(),currentUser.getUserType());
            if(contractList.size() == 0){
            	//承包区，客服，财务没有分配客户的子账号
	           	 if(currentUser.getParentId() != null){
	           		 return null;
	           	 }
	           	 
            	 vipThreadList = userThreadService.searchUsersBySite(currentUser.getSite());
            	 
            	 //当网点登陆的时候，点击财务管理的功能，显示客户列表的时候要去除掉已经是承包区的客户
            	 if("financialManager".equals(currentUser.getFinancialManager())){
            		 //已经承包出去的客户
            		 List<UserThreadContract> siteContractList = userThreadContractService.searchContractsBysiteId(currentUser.getId()+"","2");
            		 if(siteContractList != null && siteContractList.size() != 0){
            			 for(int j=0; j<vipThreadList.size(); j++){
            				 if(!"0".equals(vipThreadList.get(j).getSwitchEccount())){
            					 vipThreadList.remove(j);
            					 j--;
            					 continue;
            				 }
                			 for(int k=0; k<siteContractList.size(); k++){
                				 if(siteContractList.get(k).getConractAreaId().equals(vipThreadList.get(j).getId())){
                					 vipThreadList.remove(j);
                					 j--;
                					 break;
                				 }
                			 }
                		 }
            		 }
            		
            	 }
            }else{
            	//承包区，客服，财务分配客户的子账号
            	 for(int i=0; i<contractList.size(); i++){
            		 UserThread ut = userThreadService.getByIdAndState(contractList.get(i).getConractAreaId());
            		 if(ut != null){
            			 vipThreadList.add(ut);
            		 }
            	 }
            	
            }
        }
        return vipThreadList;
    }

    /**
     * 把当前{@link User}对象写入cookie<br>
     * 如果只是更新当前cookie用{@link #writeCookieUser(User)}
     * 
     * @param user
     * @param remeber 1保存密码,0不保存
     */
    protected void writeCookieUser(User user, int remeber) {
        if (user == null) return;

        try {
            
            Class<? extends User> userClass = user.getClass();
            Field[] fields =  userClass.getDeclaredFields();
            for(Field field : fields){
                String fieldName = field.getName();
                if(StringUtil.in(fieldName, ignoreFields)){
                    continue;
                }
                field.setAccessible(true);
                Object valObj = field.get(user);
                if(valObj == null){
                    continue;
                }
                String fieldValue = valObj.toString();
                
                writeCookie(fieldName,fieldValue,-1);
            }
            
            writeCookie("isLogin", "true", -1); // -1 表示不设置cookie的maxAge
            writeCookie("isRemeber", remeber == 1 ? "true" : "false");
            
            
        } catch (Exception e) {
            logger.error("user写入cookie出错", e);
        }
    }

    /**
     * 更新cookie用户<br>
     * 
     * @param user
     */
    protected void writeCookieUser(User user) {
        // 如果isRemeber不存在, 就默认0(不保存用户密码)
        this.writeCookieUser(user, NumberUtils.toInt(ObjectUtils.toString(readCookie("isRemeber")), 0));
    }
    
    /**
     * 将cookie值加密后存入
     * @param name
     * @param value
     * @param age <0:不设置maxAge; 0:使用系统配置; >0 使用自定义maxAge
     */
    protected void writeCookie(String name, String value, int age) {
        writeCookie(name, value, age,true);
    }
    
    /**
     * 将cookie值加密后存入
     * @param name
     * @param value
     * @param isEncode 是否加密存储
     * @param age <0:不设置maxAge; 0:使用系统配置; >0 使用自定义maxAge
     */
    protected void writeCookie(String name, String value, int age,boolean isEncode) {
        try {
            if(isEncode){
                if(ed == null){
                    ed = new EncryptionDecryption(ConfigUtilSingle.getInstance().getCOOKIE_ENCODE_KEY());
                }
                if(StringUtils.isNotBlank(value)){
                    value = ed.encrypt(value);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("cookie值加密出现异常。",e);
            e.printStackTrace();
        }
        Cookie cookie = new Cookie(name, value);
        if (age == 0) {
            cookie.setMaxAge(ConfigUtilSingle.getInstance().getCOOKIE_EXPIRY_WEEK());
        } else if (age > 0) {
            cookie.setMaxAge(age);
        }
        response.addCookie(cookie);
    }

    /**
     * 将cookie值加密后存入
     * @param name
     * @param value
     * 默认按系统配置存储COOKIE_EXPIRY_WEEK()
     */
    protected void writeCookie(String name, String value) {
        writeCookie(name, value, ConfigUtilSingle.getInstance().getCOOKIE_EXPIRY_WEEK());
    }
    

//    /**
//     * 重装cookie
//     * @param cookies
//     * @return
//     */
//    @SuppressWarnings("rawtypes")
//    public Cookie[] reloadCookies(HttpServletRequest request){
//        //logger.error("Action超类读取cookie时request中Cookie为空,"+request.getRequestURI()+","+request.getQueryString());
//        List<Cookie> cookieList = new ArrayList<Cookie>();
//        Enumeration requestEnum = request.getHeaderNames();
//        String enumName = "";
//        String enumValue="";
//        while(requestEnum.hasMoreElements()){
//            enumName=requestEnum.nextElement().toString();
//            enumValue=request.getHeader(enumName);
//            if("cookie".equalsIgnoreCase(enumName) && StringUtils.isNotBlank(enumValue)){
//                String[] keyValArray = enumValue.split(";");
//                for(String keyVal : keyValArray){
//                    cookieList.add(new Cookie(keyVal.substring(0,keyVal.indexOf("=")),keyVal.substring(keyVal.indexOf("=")+1)));
//                }
//                break;
//            }
//        }
//        return cookieList.toArray(new Cookie[cookieList.size()]);
//    }
    

    /**
     * 读取用户cookie，返回用户对象。如果不存在返回null
     * 
     * @return
     */
    protected User readCookieUser() {
        if(request == null){
            request = ServletActionContext.getRequest();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.error("cookie is null");
            return null;
            ///cookies = reloadCookies(request);
        }
        User user = new User();
        Field[] fields =  user.getClass().getDeclaredFields();
        StringBuilder userFieldsBuilder = new StringBuilder();
        for(Field field : fields){
            String fieldName = field.getName();
            if(StringUtil.in(fieldName, ignoreFields)){
                continue;
            }
            else{
                if(userFieldsBuilder.length() > 0){
                    userFieldsBuilder.append(",");
                }
                userFieldsBuilder.append(fieldName);
            }
        }
        String[] userFields = userFieldsBuilder.toString().split(",");
        try {
            if(ed == null){
                ed = new EncryptionDecryption(ConfigUtilSingle.getInstance().getCOOKIE_ENCODE_KEY());
            }
            for (Cookie cookie : cookies) {
                if(cookie == null){
                    continue;
                }
                if(StringUtil.in(cookie.getName(), userFields)){
                        Field field = user.getClass().getDeclaredField(cookie.getName());
                        String fieldValue = cookie.getValue();
                        if(StringUtils.isNotBlank(fieldValue)){
                            fieldValue = ed.decrypt(fieldValue);
                        }
                        field.setAccessible(true);
                        Class<?> typeCls = field.getType();
                        if(cookie.getName().equals("loginTime")){
                    		SimpleDateFormat sdf = new SimpleDateFormat(
                    				"EEE MMM dd HH:mm:ss Z yyyy", new Locale("ENGLISH", "CHINA"));
                    		Date loginTime = sdf.parse(fieldValue);
                    		field.set(user, loginTime);
                        }else{
                        	Object valObj = new DefaultTypeConverter().convertValue(fieldValue, typeCls);
                        	field.set(user, valObj);
                        }
                }
                
            } // for
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("读取cookie重装user时出现异常。");
            e.printStackTrace();
        }
        return user;
    } // readCookieUser

    /**
     * 默认按加密方式处理
     * @param name
     * @return
     */
    public  Object readCookie(String name) {
        return readCookie(name,true);
    }
    
    
    /**
     * 根据name读指定cookie值, 如果不存在返回 <code>null</code>
     * 
     * @param name
     * @return
     */
    protected Object readCookie(String name,boolean isDecode) {
        if(request == null){
            request = ServletActionContext.getRequest();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null){ 
            logger.error("cookie is null");
            //cookies=reloadCookies(request);
            return null;
        }
        try {
            if(ed == null){
                ed = new EncryptionDecryption(ConfigUtilSingle.getInstance().getCOOKIE_ENCODE_KEY());
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("字符串加密器初始化出现异常。");
            e.printStackTrace();
        }
        for (Cookie cookie : cookies) {
            if (name.equalsIgnoreCase(cookie.getName())) {
                
                try {
                    if(isDecode)
                        return ed.decrypt(cookie.getValue());
                    else
                        return cookie.getValue();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.debug("cookie值解密出现异常。");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 清空cookie
     */
    protected void clearCookieUser() {
        boolean isRemeber = false; // 登陆页面是否选择了记住密码
        int expiry = 0;
        if(request == null){
            request = ServletActionContext.getRequest();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.error("cookie is null");
            //cookies = reloadCookies(request);
            return;
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            Object valObj = readCookie(name);
            String val = "";
            if(valObj != null){
                val = valObj.toString();
            }
            if("userName".equalsIgnoreCase(name) || "password".equals(name)){
                continue;
            }
            else if ("isRemeber".equalsIgnoreCase(name) && "true".equalsIgnoreCase(val)) {
                isRemeber = true;
                continue;
            }
            else{
                cookie = new Cookie(name,null);
                cookie.setMaxAge(expiry);
                response.addCookie(cookie);
            }
        }
        // 没有记住用户; 清空用户所有数据
        if (!isRemeber) {
            Cookie c_user = new Cookie("userName", null);
            c_user.setMaxAge(expiry);
            response.addCookie(c_user);
            Cookie c_pass = new Cookie("password", null);
            c_pass.setMaxAge(expiry);
            response.addCookie(c_pass);
            Cookie c_isRemeber = new Cookie("isRemeber", "false");
            c_isRemeber.setMaxAge(expiry);
            response.addCookie(c_isRemeber);
        }
        Cookie c_isLogin = new Cookie("isLogin", "false");
        Cookie c_hasFCang = new Cookie("hasFCang", "false");
        c_isLogin.setMaxAge(expiry);
        c_hasFCang.setMaxAge(expiry);
        response.addCookie(c_isLogin);
        response.addCookie(c_hasFCang);
        
    }

    @Override
    public void setServletResponse(HttpServletResponse arg0) {
        this.response = arg0;
    }

    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

 

    @Override
    public void setParameters(Map<String, String[]> arg0) {
        this.parameters = arg0;
    }

    @Override
    public void setApplication(Map<String, Object> arg0) {
        this.application = arg0;
    }

    public void putMsg(Integer infoType,boolean status,String content, String targetUrl) {
        putMsg(infoType,status,content,targetUrl,null);
    }

    @SuppressWarnings("rawtypes")
    public void putMsg(Integer infoType,boolean status,String content, String targetUrl,List dataList) {
        if(jsonRes == null){
            jsonRes = new JsonResponse();
        }
        jsonRes.setInfoContent(content);
        jsonRes.setInfoType(infoType);
        jsonRes.setTargetUrl(targetUrl);
        jsonRes.setStatus(status);
        jsonRes.setDataList(dataList);
    }
    
    /**
     * 订单导入专用，后续优化
     * @param infoType
     * @param status
     * @param content
     * @param targetUrl
     */
    public void putMsgForOrderImport(Integer infoType,boolean status,String content, String targetUrl,String successCount,String failedCount) {
        if(jsonRes == null){
            jsonRes = new JsonResponse();
        }
        jsonRes.setInfoContent(content);
        jsonRes.setInfoType(infoType);
        jsonRes.setTargetUrl(targetUrl);
        jsonRes.setStatus(status);
        jsonRes.setSuccessCount(successCount);
        jsonRes.setFailedCounct(failedCount);
    }
    
	public void setJsonRes(JsonResponse jsonRes) {
		this.jsonRes = jsonRes;
	}

	public JsonResponse getJsonRes() {
		return jsonRes;
	}

   

    public void setMenuFlag(String menuFlag) {
        this.menuFlag = menuFlag;
    }

    public String getMenuFlag() {
        return menuFlag;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session=session;
        
    }
}
