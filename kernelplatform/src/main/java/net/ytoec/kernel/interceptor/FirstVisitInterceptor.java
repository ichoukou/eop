/**
 * SessionInterceptor.java
 * Wangyong
 * 2011-8-8 上午10:35:26
 */
package net.ytoec.kernel.interceptor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.questionnaire.QuestionnaireAction;
import net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.EncryptionDecryption;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * struts2方法拦截器。
 * 
 * @author mgl
 * @2012-3-20 net.ytoec.kernel.interceptor
 */
@Scope("prototype")
public class FirstVisitInterceptor extends MethodFilterInterceptor {
	private static Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    private static EncryptionDecryption ed = null;

    private final static String[] ignoreFields = {"telAreaCode","telCode","telExtCode","sex","cardType","cardNo"
                             ,"userLevel","createUser","createUser","createUser","createTime","serialVersionUID"
                             ,"updateTime","remark","deptName","deptCode","DeptPhone"
                             ,"DeptAddr","HrCanceled","Canceled","Dr","appProvider"};

	/**
	 * 
	 */
	private static final long serialVersionUID = 2389999261109796738L;
	@SuppressWarnings("all")
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception { 
	        Map map = this.readCookie();
			User user = map.get("user") == null ? null : (User) map.get("user");
			UserCustom _uc=Resource.getFirstVisit(user);
			String methods = invocation.getProxy().getMethod();
			Object action = (Action) invocation.getAction();
			HttpServletRequest request = ServletActionContext.getRequest();
			Object menuFlagStrObject = request.getParameter("menuFlag");
			String menuFlag= null;
			if(menuFlagStrObject != null){
				menuFlag = menuFlagStrObject.toString();
				request.setAttribute("menuFlag", menuFlag);
			}
			if (action instanceof QuestionnaireAction&&"index".equals(methods)){
			    if(_uc==null||!UserCustom.VISITED.equals(_uc.getField003())){
			    	//加菜单标识参数
					if("1".equals(user.getUserType())||"11".equals(user.getUserType())||"12".equals(user.getUserType())||"13".equals(user.getUserType())){
						String step = request.getParameter("step");
						if(StringUtils.isEmpty(menuFlag))
							request.setAttribute("menuFlag", "chajian_question");
						if(step !=null && step.equals("2")){
							return "instruction_vip_step2";
						}else {
							return "instruction_vip_step1";
						}
					}
					else if("2".equals(user.getUserType())||"21".equals(user.getUserType())||"22".equals(user.getUserType())||"23".equals(user.getUserType())){
						if(StringUtils.isEmpty(menuFlag))
							request.setAttribute("menuFlag", "chajian_question");
						return "instruction_site";
					}
				}
			}
			if(action instanceof QuestionnaireMonitorAction&&"index".equals(methods)){
			  //加菜单标识参数
			    if(_uc==null||!UserCustom.VISITED.equals(_uc.getField002())){
			    	request.setAttribute("menuFlag", "chajian_monitor_index");
					return "instruction";						
				}
			}
	        return invocation.invoke();  
	}
	
	/**
	 * 根据name读指定cookie值, 如果不存在返回 <code>null</code>
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
    protected Map<String ,Object> readCookie() {
	    HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String ,Object> map = new HashMap<String ,Object>();
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
                if(StringUtil.in(cookie.getName(), userFields)){
                        Field field = user.getClass().getDeclaredField(cookie.getName());
                        String fieldValue = cookie.getValue();
                        if(StringUtils.isNotBlank(fieldValue)){
                            fieldValue = ed.decrypt(fieldValue);
                        }
                        field.setAccessible(true);
                        Class<?> typeCls = field.getType();
                      //Object valObj = typeCls.cast(fieldValue);
                        Object valObj = new DefaultTypeConverter().convertValue(fieldValue, typeCls);
                        field.set(user, valObj);
                }
                else if("isLogin".equalsIgnoreCase(cookie.getName())){
                    map.put("isLogin", ed.decrypt(cookie.getValue()));
                }
                else if("isRemeber".equals(cookie.getName())){
                    map.put("isRemeber", ed.decrypt(cookie.getValue()));
                }
            } // for
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("读取cookie重装user时出现异常。");
            e.printStackTrace();
        }
        map.put("user", user);
		return map;
	} // readCookie
	
}
