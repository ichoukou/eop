package net.ytoec.kernel.interceptor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.leavemessage.SendMessageAction;
import net.ytoec.kernel.action.login.MainPageAction;
import net.ytoec.kernel.action.order.OrderAction;
import net.ytoec.kernel.action.questionnaire.QuestionnaireAction;
import net.ytoec.kernel.action.user.RegionAction;
import net.ytoec.kernel.action.user.UserAction;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.EncryptionDecryption;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@SuppressWarnings("serial")
public class FilterInterceptor extends MethodFilterInterceptor {
	private static Logger logger = LoggerFactory.getLogger(FilterInterceptor.class);
	private static EncryptionDecryption ed = null;

	private final static String[] ignoreFields = { "telAreaCode", "telCode",
			"telExtCode", "sex", "cardType", "cardNo", "userLevel",
			"createUser", "createUser", "createUser", "createTime",
			"serialVersionUID", "updateTime", "remark", "deptName", "deptCode",
			"DeptPhone", "DeptAddr", "HrCanceled", "Canceled", "Dr",
			"appProvider" };

	/**
	 * 根据name读指定cookie值, 如果不存在返回 <code>null</code>
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> readCookie() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return Collections.EMPTY_MAP;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User();
		Field[] fields = user.getClass().getDeclaredFields();
		StringBuilder userFieldsBuilder = new StringBuilder();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (StringUtil.in(fieldName, ignoreFields)) {
				continue;
			} else {
				if (userFieldsBuilder.length() > 0) {
					userFieldsBuilder.append(",");
				}
				userFieldsBuilder.append(fieldName);
			}
		}
		String[] userFields = userFieldsBuilder.toString().split(",");
		try {
			if (ed == null) {
				ed = new EncryptionDecryption(ConfigUtilSingle.getInstance()
						.getCOOKIE_ENCODE_KEY());
			}
			for (Cookie cookie : cookies) {
				if (StringUtil.in(cookie.getName(), userFields)) {
					Field field = user.getClass().getDeclaredField(
							cookie.getName());
					String fieldValue = cookie.getValue();
					if (StringUtils.isNotBlank(fieldValue)) {
						fieldValue = ed.decrypt(fieldValue);
					}
					Class<?> typeCls = field.getType();
					// Object valObj = typeCls.cast(fieldValue);
					Object valObj = new DefaultTypeConverter().convertValue(
							fieldValue, typeCls);
					field.setAccessible(true);
					field.set(user, valObj);
				} else if ("isLogin".equalsIgnoreCase(cookie.getName())) {
					map.put("isLogin", ed.decrypt(cookie.getValue()));
				} else if ("isRemeber".equals(cookie.getName())) {
					map.put("isRemeber", ed.decrypt(cookie.getValue()));
				} else if ("hasFCang".equals(cookie.getName())) {
					map.put("hasFCang", ed.decrypt(cookie.getValue()));
				} else if ("action".equalsIgnoreCase(cookie.getName())) {
					map.put("action", ed.decrypt(cookie.getValue()));
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

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		Object action = (Action) invocation.getAction();
		HttpServletRequest request = ServletActionContext.getRequest();
		Object menuFlagStrObject = request.getParameter("menuFlag");
        String menuFlag= null;
        if(menuFlagStrObject != null){
            menuFlag = menuFlagStrObject.toString();
            request.setAttribute("menuFlag", menuFlag);
        }
		Map<String, Object> map = this.readCookie();
		User user = map.get("user") == null ? null : (User) map.get("user");
		//若userType为null,则返回登陆界面
		if(user.getId()==null || user.getUserType()==null) {
			return Action.LOGIN;
		}
		String usertype = user.getUserType();
		String userState = user.getUserState();
		String field003 = user.getField003();
		String methodName = invocation.getProxy().getMethod();

		// 获取拦截器 只对功能 用户激活绑定 对象：卖家，业务账号 起作用

		if ("1".equals(usertype) || "11".equals(usertype)
				|| "12".equals(usertype) || "13".equals(usertype)) {

			if ("1".equals(userState)) {
				invocation.invoke();// 用户已经绑定网点不进行拦截
			} else if ("TBA".equals(userState) && "9".equals(field003)) {
				// 用户已经完善信息但是未绑定
				if ((action instanceof QuestionnaireAction && methodName.equals("list"))
				        || (action instanceof UserAction && (methodName.equals("toAddSubAccount") || methodName.equals("addSubAccount")))
						|| (action instanceof OrderAction && (methodName.equals("ecAccount") || methodName.equals("exportOrder")))
						|| (action instanceof SendMessageAction && (methodName.equals("send") || methodName.equals("reply")))) {
					// 如果完善信息，但未绑定，问题件管理，电子对账，发新消息,这3个功能进行拦截
				} else {
					invocation.invoke();// 不进行拦截
				}

				// System.out.println("拦截器在Action执行后" + new Date());
			}else{
			    if(StringUtils.isEmpty(menuFlag)){
			        request.setAttribute("menuFlag", "user_myacc");
			    }
			    if (action instanceof UserAction && methodName
						.equals("toEdit")) {
					return "toEditIndex";
				}
				return "non_index";
			}

		} else {
			// System.out.println("拦截器在Action执行前" + new Date());
			if ("2".equals(usertype) || "21".equals(usertype)
					|| "22".equals(usertype) || "23".equals(usertype)) {
				// 网点用户：未激活可使用消息管理
				// 不拦截左边菜单栏
				if (action instanceof MainPageAction
						|| action instanceof RegionAction) {
				} else {
					if ((user.getUserState()).equals("TBA")) {
						// 未激活网点用户首次登录时进入信息完善页面.用户需要设置密码、邮箱、用户账户和真实姓名等信息
//					    request.setAttribute("menuFlag", "user_myacc");
					    return "siteFirstLogin";
					}else{
						return invocation.invoke();
					}
				}
			}else
				invocation.invoke();// 不进行拦截
			
		}

		return "non_index";
	}
}