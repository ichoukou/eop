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

import net.ytoec.kernel.action.alipay.AlipayAction;
import net.ytoec.kernel.action.edm.CoreEdmAction;
import net.ytoec.kernel.action.hessian.MediaUploadAction;
import net.ytoec.kernel.action.home.PieResultAction;
import net.ytoec.kernel.action.home.TendencyAction;
import net.ytoec.kernel.action.login.LoginAction;
import net.ytoec.kernel.action.login.MainPageAction;
import net.ytoec.kernel.action.login.NoInterceptAction;
import net.ytoec.kernel.action.order.OrderAction;
import net.ytoec.kernel.action.order.PosttempAction;
import net.ytoec.kernel.action.order.WayBillPrintAction;
import net.ytoec.kernel.action.questionnaire.AttentionMailAction;
import net.ytoec.kernel.action.questionnaire.QuestionnaireAction;
import net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction;
import net.ytoec.kernel.action.remote.CommonOrderAction;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.TaoBaoServiceApplyAction;
import net.ytoec.kernel.action.remote.UserInfoProcessAction;
import net.ytoec.kernel.action.sms.SmsSendCallBackAction;
import net.ytoec.kernel.action.test.TestAction;
import net.ytoec.kernel.action.test.UploadAction;
import net.ytoec.kernel.action.user.AssociationAccountAction;
import net.ytoec.kernel.action.user.RegionAction;
import net.ytoec.kernel.action.user.YtoUserAction;
import net.ytoec.kernel.action.waybill.WayBillAction;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.exception.BusinessException;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.EncryptionDecryption;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * struts2拦截器。
 * 
 * @author Wangyong
 * @2011-8-8 net.ytoec.kernel.interceptor
 */
@SuppressWarnings("serial")
public class SessionInterceptor extends AbstractInterceptor {

	private static EncryptionDecryption ed = null;

	private static Logger logger = Logger.getLogger(SessionInterceptor.class);

	private final static String[] ignoreFields = { "telAreaCode", "telCode",
			"telExtCode", "sex", "cardType", "cardNo", "userLevel",
			"createUser", "createUser", "createUser", "createTime",
			"serialVersionUID", "updateTime", "remark", "deptName", "deptCode",
			"DeptPhone", "DeptAddr", "HrCanceled", "Canceled", "Dr",
			"appProvider" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com
	 * .opensymphony.xwork2.ActionInvocation)
	 */
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {

		// 获取拦截器对象
		Object action = (Action) actionInvocation.getAction();
		/*
		 * @2012-02-16/ChenRen 用cookie代替session User user = (User) // 获取session
		 * Map session = actionInvocation.getInvocationContext().getSession();
		 * session.get(Constants.SESSION_USER);
		 */

		
		try {
			// 不进行拦截的action
			if (action instanceof LoginAction
					|| action instanceof NoInterceptAction
					|| action instanceof TestAction
					|| action instanceof TaoBaoOrderAction
					|| action instanceof TaoBaoServiceApplyAction
					|| action instanceof UserInfoProcessAction
					|| action instanceof UploadAction
					|| action instanceof MediaUploadAction
					|| action instanceof RegionAction
					|| action instanceof CommonOrderAction
					|| action instanceof AlipayAction
					|| action instanceof SmsSendCallBackAction
					|| action instanceof CoreEdmAction
					|| action instanceof PieResultAction
					|| action instanceof TendencyAction
					|| action instanceof WayBillPrintAction
					

			// || action instanceof PrintTempAction // 打印模板
			) {
				return actionInvocation.invoke();
			}
			//会员模块，数据初始化功能，不拦截
			if (action instanceof YtoUserAction) {
				String method = actionInvocation.getProxy().getMethod();
				if ("init".equalsIgnoreCase(method)) {
					return "init";
				}
			}
			
			Map<String, Object> map = this.readCookie();
	        User user = map.get("user") == null ? null : (User) map.get("user");

	        HttpServletRequest request = ServletActionContext.getRequest();
//	        if(user != null){
//	            request.setAttribute("user", user);
//	        }
	        String isLogin = map.get("isLogin") == null ? "false" : map.get("isLogin").toString();
	        // request.setAttribute("isLogin", isLogin);
	        // request.setAttribute("isRemeber", map.get("isRemeber"));
	        // request.setAttribute("action", map.get("action"));
			// 如果用户为空 || 登录状态为未登录 跳到LOGIN页面
			if (user == null || !"true".equalsIgnoreCase(isLogin)) {
				return Action.LOGIN;
			} else {
				String usertype = user.getUserType();

				// 同时拥有财务、客服账号的角色权限跟主账号一样
				if ("13".equals(usertype)) {
					usertype = "1";
				} else if ("23".equals(usertype)) {
					usertype = "2";
				} else if ("41".equals(usertype)) {
					// 平台用户客服子账号权限=卖家客服子账号
					usertype = "11";
					// 平台用户客服账号对个性化配置没有权限
					if (action instanceof AssociationAccountAction) {
						return "no_permission";
					}
				}

				if ("11".equals(usertype)) {
					// 客服账号只对部分action有权限
					if (action instanceof OrderAction) {
						String method = actionInvocation.getProxy().getMethod();
						if ("toECAccount".equalsIgnoreCase(method) // 跳转到电子对账页面
								|| "ecAccount".equalsIgnoreCase(method) // 对账
								|| "output2csv3".equalsIgnoreCase(method)) // 导出
						{
							return "no_permission";
						}
					}
				}

				if ("11".equals(usertype) || "12".equals(usertype)
						|| "13".equals(user.getUserType())) {
					// 子账号木有多店铺权限
					if (action instanceof AssociationAccountAction) {
						String method = actionInvocation.getProxy().getMethod();
						if ("toBindedAssociationAccount"
								.equalsIgnoreCase(method) // 跳转到多店铺页面
								|| "cancelAssociationAccount"
										.equalsIgnoreCase(method)) // 取消多店铺关联
						{
							return "no_permission";
						}
					}
				}

				if ("1".equals(usertype) || "12".equals(usertype)) {
					if (action instanceof OrderAction
							&& "toECAccount".equalsIgnoreCase(actionInvocation
									.getProxy().getMethod())) {
						// 卖家的电子对账功能被关闭
						// //如果用户状态为未激活 并且电子对账标志不为0 主动设置为0 以便跳转到激活页面
						// if (StringUtils.isEmpty(user.getSwitchEccount())) {
						// user.setSwitchEccount("0");
						// } else
						if (!"0".equals(user.getSwitchEccount())) {
							return "no_permission";
						}
					}
					if (action instanceof PosttempAction
							&& "toPosttempView2"
									.equalsIgnoreCase(actionInvocation
											.getProxy().getMethod())) {
						// 卖家的运费模板功能被关闭
						if (!"0".equals(user.getSwitchEccount()))
							return "no_permission";
					}
					if (action instanceof WayBillAction
							&& "mjunlikefreight"
									.equalsIgnoreCase(actionInvocation
											.getProxy().getMethod())) {

						// 卖家的特殊账单功能被关闭
						if (!"0".equals(user.getSwitchEccount())) {
							return "no_permission";
						} else if ("TBA".equalsIgnoreCase(user.getUserState())) {
							if ("9".equals(user.getField003())) {
								// 只填写了基本信息，未激活, 填写用编码页面
								return "g_toActive4taobaoStep2";
							} else {
								// 没有填写基本信息
							    request.setAttribute("menuFlag", "user_myacc");
								return "g_toActive4taobao";
							}
						}
					}
				}

				/**
				 * @see 此处有修改，做搜索引擎时，需要配置数据库中用户权限。
				 */
				boolean hasPermission = Resource.hasPermission(usertype, action
						.getClass().getName());
				if (!hasPermission)
					return "no_permission";
				// 卖家
				if ("1".equals(usertype) || "11".equals(usertype)
						|| "12".equals(usertype) || "13".equals(usertype)) {
				}
				// 网点用户
				else if ("2".equals(usertype)) {
					if ((user.getUserState()).equals("TBA")) {
						// 未激活网点用户首次登录时进入信息完善页面.用户需要设置密码、邮箱、用户账户和真实姓名等信息
					    Object menuFlagStrObject = request.getParameter("menuFlag");
			            String menuFlag= null;
			            if(menuFlagStrObject != null){
			                menuFlag = menuFlagStrObject.toString();
			                request.setAttribute("menuFlag", menuFlag);
			            }
					    return "siteFirstLogin";
					}
				}
				// 平台用户
				else if ("4".equals(usertype)) {
					// 不拦截左边菜单栏
					if (action instanceof MainPageAction
							|| action instanceof RegionAction) {
					} else {
						String hasFCang = ObjectUtils.toString(map
								.get("hasFCang"));
						if (!"1".equals(hasFCang)) {
							// 如果平台用户没有业务账号. 有些action没有权限访问, 要跳转到新增业务账号的页面
							if (action instanceof WayBillAction // 高级查询/智能查件/特殊账单
									|| action instanceof QuestionnaireMonitorAction // 运单监控/我的关注
									|| action instanceof AttentionMailAction // 我的关注
									|| action instanceof QuestionnaireAction // 问题件管理
									|| action instanceof OrderAction // 电子对账/我要发货
									|| action instanceof PosttempAction // 运费模板
							) {
								String url = request.getServletPath() + "?"
										+ request.getQueryString();
								url = url.startsWith("/") ? url.substring(1)
										: url;
								request.setAttribute("url", url); // 在新建业务账号页面保存上一次访问路径

								return "g_remindSubAccount";// 分有分仓，跳转到提示创建业务子账号页面。
								// return "g_toAddPlatformSubAccount"; // 没有分仓,
								// 跳转到新增业务账号页面
							}
						}
					}
				}
				return actionInvocation.invoke();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			exception(actionInvocation, ex, "对不起，打开失败。");
			return "exception";
		}
	}

	
//	/**
//     * 重装cookie
//     * @param cookies
//     * @return
//     */
//    @SuppressWarnings("rawtypes")
//    public Cookie[] reloadCookies(HttpServletRequest request){
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
		    logger.error("session拦截器读取cookie时request中Cookie为null,"+request.getRequestURI()+","+request.getQueryString());
		    //cookies=reloadCookies(request);
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
			    if(cookie == null){
			        continue;
			    }
			    String cookieName = cookie.getName();
				if (StringUtil.in(cookieName, userFields)) {
					Field field = user.getClass().getDeclaredField(
							cookie.getName());
					String fieldValue = cookie.getValue();
					if (StringUtils.isNotBlank(fieldValue)) {
						fieldValue = ed.decrypt(fieldValue);
						Class<?> typeCls = field.getType();
	                    // Object valObj = typeCls.cast(fieldValue);
	                    Object valObj = new DefaultTypeConverter().convertValue(
	                            fieldValue, typeCls);
	                    field.setAccessible(true);
	                    field.set(user, valObj);
					}
					
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

	/**
	 * 异常处理
	 * 
	 * @param invocation
	 * @param e
	 * @param message
	 */
	private void exception(ActionInvocation invocation, Exception e,
			String message) {
		ValueStack ac = invocation.getInvocationContext().getValueStack();
		ac.set("exception", new BusinessException(message));
		ac.set("exceptionStack", e.getStackTrace());
		ac.set("exceptionStack_", e.toString());
		e.printStackTrace();
	}

}
