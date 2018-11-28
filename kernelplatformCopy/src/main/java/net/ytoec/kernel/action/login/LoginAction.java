/**
 * LoginAction.java Wangyong 2011-8-4 下午03:09:20
 */
package net.ytoec.kernel.action.login;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.StringTool;
import net.ytoec.kernel.action.taobao.common.Util;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.StatsMessage;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.exception.BusinessException;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.service.StatsMessageService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 系统登录Action
 * 
 * @author Wangyong
 * @2011-8-4 net.ytoec.kernel.action.login
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class LoginAction extends AbstractActionSupport {

	private static Logger logger = LoggerFactory.getLogger(LoginAction.class);
	@Inject
	private UserService<User> userService;
	@Inject
	private UserCustomService<UserCustom> userCustomerService;
	@Inject
	private MailService<Mail> mailService;
	@Inject
	private MessageService<Message> messageService;
	@Inject
	private StatsMessageService<StatsMessage> statsMessageService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private SMSBuyersService smsBuyersService;

	private String userName;
	private String userPassword;
	private int isRemeber;

	private String loginMessage;
	private String codeString;

	private String loginName;
	private String responseString;
	private Integer validate;

	/** 未激活用户申请用户编码时传递的user对象 ，User对象中必须包含用户的账户和邮箱地址 */
	private User inactiveUser;
	private User user;
	/** 申请编码后返回的字符串 */
	private String applyString;

	private int taobaoLogin = 0;

	private Integer errorTimes = 0;

	private String randomStr;

	/**
	 * 验证是否通过. 通过json格式返回到前台<br>
	 * 默认为false；
	 */
	private String isValidate;
	/**
	 * ajax验证的错误提示信息<br>
	 * 如果验证通过可以不写值
	 */
	private String ajaxAlertText;

	/** 发送消息-内容 */
	private String content;
	/** 发送消息-联系方式 */
	private String linkway;

	// 临时登录返回信息
	private static String tempLoginMessage;

	private String resultResponse;
	private String associationResult;

	/**
	 * 快速入门返回值
	 */
	private String quickReadString;

	/*
	 * // 邮件内容格式 ${userNameText}： 您好!
	 * 您在易通平台使用找回密码功能后，系统为您自动生成了一个临时密码，请您尽快使用新密码登录系统，并修改密码。 新密码：${newPwd}
	 * (区分大小写) 系统地址：http://ec.yto56.net.cn
	 * --------------------------------------
	 * ------------------------------------------------
	 * 感谢您使用圆通速递易通平台，本邮件为系统邮件，请勿回复，谢谢!
	 */
	/**
	 * 找回密码的邮件内容HTML模板<br>
	 * 使用的时候请替换${userNameText}和${newPwd}为要通知的用户名和密码
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<B>${userNameText}</B>：<DIV><SPAN CLASS=\"tab-1\"></SPAN>您好!</DIV><DIV><SPAN CLASS=\"tab-1\"></SPAN>"
			+ "您在易通平台使用找回密码功能后，系统为您自动生成了一个临时密码，请您尽快使用新密码登录系统，并修改密码。</DIV>"
			+ "<DIV>新密码：<B>${newPwd}</B><SPAN CLASS=\"tab-1\" STYLE=\"FONT-WEIGHT: BOLD;\"></SPAN>(区分大小写)</DIV"
			+ "><DIV>易通地址：<A HREF=\"http://ec.yto.net.cn\" TARGET=\"_BLANK\">http://ec.yto.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "<DIV>感谢您使用圆通速递易通平台，本邮件为系统邮件，请勿回复，谢谢!</DIV></DIV></DIV></BODY></HTML>";

	/**
	 * 跳转到登录页面<br>
	 * 如果cookie用户对象不为空并且选择了记住密码，就把用户名和密码写到request对象里. 否则写入空值<br>
	 * 
	 * @return
	 */
	public String goLogin() {
		// 清空一下垃圾数据，防止cookie混乱导致的菜单问题。
		clearCookieUser();
		User user = readCookieUser();
		if (user != null
				&& "true".equals(ObjectUtils.toString(readCookie("isRemeber")))) {
			try {
				String name = user.getUserName();
				String field002 = user.getField002();
				if (!StringUtils.isEmpty(field002)) {
					String str[] = field002.split(",");
					if (name != null && str.length > 3) {
						String pwd1 = str[1];
						String pwd2 = str[3];
						pwd2 = new String(Base64.decodeBase64(pwd2
								.getBytes(CharEncoding.ISO_8859_1)));
						pwd1 = new String(Base64.decodeBase64(pwd1
								.getBytes(CharEncoding.ISO_8859_1)));

						request.setAttribute("name", name); // 把用户解析传到登录页面记住用户名和密码
						request.setAttribute("pwd", pwd2 + pwd1); // 把用户解析传到登录页面记住用户名和密码
					}
				}
			} catch (Exception e) {
				logger.error("跳转到登陆页面出错", e);
			}
		} else {
			request.setAttribute("name", null);
			request.setAttribute("pwd", null);
		}
		return "goLogin";
	}

	/**
	 * 转到更多商家logon界面
	 * 
	 * @return
	 */
	public String goShops() {
		return "shops";
	}

	/**
	 * 跳转到退出页面
	 * 
	 * @return
	 * @throws IOException
	 */
	public String loginOut() throws IOException {
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		// 淘宝用户在退出登录时，需退出授权。此为安全退出地址。
		String logoff = TaoBaoAction.getlogoffURL(ConfigUtilSingle
				.getInstance().getSIGN_METHOD_MD5(), ConfigUtilSingle
				.getInstance().getTOP_APPKEY());
		// 淘宝服务URL
		String fuwuURL = TaoBaoAction.getTaobaofuwuURL();
		// if (session.get(Constants.SESSION_USER) != null) {
		User cookie_user = super.readCookieUser();
		if (cookie_user != null && cookie_user.getId() != null) {
			/*
			 * StatsMessage statsMessage = statsMessageService
			 * .getStatsMessageListbySR(cookie_user.toString()); if
			 * (statsMessage != null && cookie_user.toString().equals(
			 * statsMessage.getStatsReason())) {
			 */
			StatsMessage statsMessage = new StatsMessage();
			statsMessage.setUserId(cookie_user.getId());
			statsMessage.setLogoutTime(new Date());
			statsMessageService.editStatsMessage(statsMessage);
			// }
		}

		// session.put(Constants.SESSION_USER, null);
		clearCookieUser(); // 清空cookie
		if (readCookie("taobaoLogin") != null
				&& StringUtils
						.equals(readCookie("taobaoLogin").toString(), "1")) {
			if (!StringUtil.isBlank(userType)) {
				if (userType.equals("1")) {
					// response.sendRedirect(fuwuURL);
					resultResponse = fuwuURL;
				}
			} else {
				// response.sendRedirect(logoff);
				resultResponse = logoff;
			}
		} else {
			resultResponse = "";
		}
		tempLoginMessage = "";
		return "loginOut";
	}

	/**
	 * 验证登录时输入的验证码
	 * 
	 * @return
	 */
	public String checkVldCode() {
		if (StringUtils.isNotBlank(codeString)) {
			String vldCode = (String) request.getSession().getAttribute("c");
			if (StringUtils.isNotBlank(vldCode)
					&& codeString.equalsIgnoreCase(vldCode)) {
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "验证码正确！", "");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "验证码错误！", "");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "验证码为空！", "");
		}
		return "jsonRes";
	}

	/**
	 * 验证登录
	 * 
	 * @return
	 * @throws IOException
	 * @throws BusinessException
	 */
	public String doLogin() throws IOException, BusinessException {
		tempLoginMessage = loginMessage = null;
		taobaoLogin = 0;// 非淘宝登录
		Object errorTimesObj = readCookie("errorTimes");
		Object reqRandomObj = request.getAttribute("randomStr");
		if (errorTimesObj != null) {
			errorTimes = Integer.parseInt(errorTimesObj.toString());
		}
		if (userName != null && userPassword != null) {
			/**
			 * 更改登录为用户输入用户名或者用户编码都可以进行登录系统
			 */
			User currentUser = userService.loginUserCheck(userName,
					userPassword);
			
			List<String> testers = new ArrayList<String>();
			testers.add(userName);
			//testers.add("4QrcOUm6Wau+VuBX8g+IPg==");
				///	.getTestBuyerNameList();

			if (currentUser != null) {

				if (!"1".equals(currentUser.getUserType())) {

					// 先登录再激活；未激活网点用户首次登录时进入信息完善页面.用户需要设置密码、邮箱、用户账户和真实姓名等信息
					if ("1".equals(currentUser.getUserState())
							|| "TBA".equalsIgnoreCase(currentUser
									.getUserState())) {
						// 网点可以关闭卖家的电子对账
						// 这里当卖家登录的时候查看是否被关闭了该功能;如果关闭页面不显示'电子对账'功能
						String userType = currentUser.getUserType();
						if ("12".equals(userType) || "13".equals(userType)) {
							UserThread userThread = new UserThread();
							userThread.setUserCode(currentUser.getUserCode());

							List<UserThread> utList = userThreadService
									.searchUsersByCode(userThread);
							if (utList.size() > 0) {
								userThread = utList.get(0);
								currentUser.setSwitchEccount(userThread
										.getSwitchEccount());
							}
						} else if ("4".equals(userType)) {
							// 平台用户. 判断平台用户是否有业务/分仓账号. 如果有就存1到cookie, 否则不存或存0
							List<User> subList = userService.pingTaiSelect(
									currentUser, 1);
							if (subList.size() > 0) {
								super.writeCookie("hasFCang", "1", -1); // 有分仓账号.
																		// cookie有效时间为浏览器回话级别
							} else {
								super.writeCookie("hasFCang", "0", -1);
							}
						}
						writeCookie("taobaoLogin", String.valueOf(taobaoLogin));
						saveCookie(currentUser, isRemeber);
						UserCustom param = new UserCustom();
						param.setRelationalQuery(UserCustom.RELATIONAL);
						param.setType("1");
						param.setUserName(userName);
						List<UserCustom> uc = userCustomerService
								.searchUserCustom(param);
						if (uc != null && uc.size() > 0) {
							// 标识用户激活第一步已经完成。
							super.writeCookie("infostate", "1");
						}
						if (isRemeber == 1) {
							writeCookie("password", userPassword, -1);
						}
						this.recordLoginTime(currentUser);
						isRemeber = 0;
						errorTimes = 0;
						super.writeCookie("sitePopup", "0");
						writeCookie("errorTimes", errorTimes.toString());

						/* 将当前登陆时间写入cookie add by qinghua.yang begin */
						String time = DateUtil.format(new Date(),
								"yyyy-MM-dd HH:mm:ss");
						writeCookie("lastShowTime", time);
						// writeCookie("lastShowTimeTemp", time);//记载第一次登陆时间
						/* 将当前登陆时间写入cookie add by qinghua.yang end */

						return "doLogin";
					} else {
						loginMessage = "此帐号已禁用，请联系易通客服";
					}
				} else {
					User user = userService.searchUsersByLoginName(userName);
					if (testers.contains(userName)
							|| StringUtils.isNotEmpty(user.getChildType())) {

						// 先登录再激活；未激活网点用户首次登录时进入信息完善页面.用户需要设置密码、邮箱、用户账户和真实姓名等信息
						if ("1".equals(currentUser.getUserState())
								|| "TBA".equalsIgnoreCase(currentUser
										.getUserState())) {
							// 网点可以关闭卖家的电子对账
							// 这里当卖家登录的时候查看是否被关闭了该功能;如果关闭页面不显示'电子对账'功能

							UserThread userThread = new UserThread();
							userThread.setUserCode(currentUser.getUserCode());

							List<UserThread> utList = userThreadService
									.searchUsersByCode(userThread);
							if (utList.size() > 0) {
								userThread = utList.get(0);
								currentUser.setSwitchEccount(userThread
										.getSwitchEccount());
							}
							writeCookie("taobaoLogin",
									String.valueOf(taobaoLogin));
							saveCookie(currentUser, isRemeber);
							UserCustom param = new UserCustom();
							param.setRelationalQuery(UserCustom.RELATIONAL);
							param.setType("1");
							param.setUserName(userName);
							List<UserCustom> uc = userCustomerService
									.searchUserCustom(param);
							if (uc != null && uc.size() > 0) {
								// 标识用户激活第一步已经完成。
								super.writeCookie("infostate", "1");
							}
							if (isRemeber == 1) {
								writeCookie("password", userPassword, -1);
							}
							this.recordLoginTime(currentUser);
							isRemeber = 0;
							errorTimes = 0;
							super.writeCookie("sitePopup", "0");
							writeCookie("errorTimes", errorTimes.toString());

							/* 将当前登陆时间写入cookie add by qinghua.yang begin */
							String time = DateUtil.format(new Date(),
									"yyyy-MM-dd HH:mm:ss");
							writeCookie("lastShowTime", time);
							// writeCookie("lastShowTimeTemp", time);//记载第一次登陆时间
							/* 将当前登陆时间写入cookie add by qinghua.yang end */
							return "doLogin";
						} else {
							loginMessage = "此帐号已禁用，请联系易通客服";
						}
					} else {
						loginMessage = "卖家账号请从“淘宝卖家登录”入口登录";
					}
				}
			} else {
				User user = userService.searchUsersByLoginName(userName);
				if (user != null) {

					if ("1".equals(user.getUserType())
							&& StringUtils
									.isNotBlank(user.getTaobaoEncodeKey())
							&& StringUtils.isEmpty(user.getChildType())
							&& !testers.contains(userName)) {
						loginMessage = "卖家账号请从“淘宝卖家登录”入口登录";
					} else {
						loginMessage = "密码不正确";
					}
				} else
					loginMessage = "用户不存在";
			}
		}
		tempLoginMessage = loginMessage;
		if (reqRandomObj != null) {
			request.setAttribute("randomStr", null);
		}
		errorTimes++;
		writeCookie("errorTimes", errorTimes.toString());
		return "loginError";
	}

	/**
	 * 记录当前用户登录时间
	 * 
	 * @param user
	 * @throws BusinessException
	 */
	private void recordLoginTime(User user) throws BusinessException {
		User newUser = userService.get(user);
		newUser.setLoginTime(new Date());
		// 记录用户登录时间
		StatsMessage statsMessage = new StatsMessage();
		statsMessage.setUserId(user.getId());
		statsMessage.setStatsType("0");
		statsMessage.setStatsReason(user.toString());
		statsMessage.setRemark(user.getUserName() + ":登录系统,ip:"
				+ request.getLocalAddr() + ",ip:" + request.getRemoteHost()
				+ ",ip:" + request.getRemoteAddr());
		statsMessage.setLoginTime(new Date());
		statsMessageService.addStatsMessage(statsMessage);
		if (userService.updateUser(newUser)) {
		} else {
			throw new BusinessException("记录登录时间错误!");
		}

	}

	/**
	 * 保存Cookie
	 * 
	 * @param user
	 * @param isRemeber
	 * @throws UnsupportedEncodingException
	 */
	private void saveCookie(User user, int isRemeber)
			throws UnsupportedEncodingException {

		writeCookieUser(user, isRemeber);
	} // saveCookie

	/**
	 * 找回密码 如果用户邮箱存在的话，就随机生成一个密码字符串作为新密码发送给用户，并且将新密码保存到数据库中。
	 * 
	 * @return
	 */
	public String findPassword() {
		User user = userService.searchUsersByLoginName(loginName);
		responseString = "邮件发送失败，稍候请重新操作！";
		boolean res = false;
		if (user.getMail() == null || user.getMail().equals("")) {
			responseString = "您的邮箱不存在，请联系管理员！";
		} else if (this.sendMail(user)) {
			responseString = "系统已将密码发送至您“" + user.getMail() + "”的邮箱，请查收";
			res = true;
		}
		loginName = null;
		putMsg(JsonResponse.INFO_TYPE_SUCCESS, res, responseString, "");
		return "jsonRes";
	}

	/**
	 * 验证该用户是否存在，存在的时候他对应的邮箱是否存在。
	 * 
	 * @return
	 */
	public String checkFindPass() {
		if (loginName != null) {
			isValidate = "false";
			ajaxAlertText = "";
			User user = userService.searchUsersByLoginName(loginName);
			if (user != null) {
				isValidate = "true";// 表示邮件存在
			} else {
				isValidate = "false";// 表示账号不存在
				ajaxAlertText = "账号不存在!";
			}
		}
		loginName = null;
		putMsg(JsonResponse.INFO_TYPE_MSG, "true".equals(isValidate),
				ajaxAlertText, "");
		return "jsonRes";
	}

	/**
	 * 生成一个随机六位的密码串,在发送邮件成功的情况下，并将新密码更新到该用户下
	 * 
	 * @return
	 */
	private boolean sendMail(User user) {
		Mail mail = new Mail();
		mail.setFromMail("yto.ec.core@gmail.com");
		mail.setFromMailText("核心系统");
		mail.setSendToMail(user.getMail());
		mail.setSubject("易通平台密码信息");
		// mail.setSubject("密码找回，你注意查收");
		String randomPass = StringTool.randomChars(6);// 生成一个随机六位的密码串
		String content = mailContentTemp.replace("${userNameText}",
				user.getUserNameText()).replace("${newPwd}", randomPass);
		mail.setContent(content);
		boolean flag = mailService.sendMail(mail);
		if (flag) {
			user.setUserPassword(Md5Encryption.MD5Encode(randomPass));
			userService.updateUser(user);
		}
		return flag;
	}

	private static int taobaoLoginTimes = 0;

	/**
	 * 淘宝用户登录<br>
	 * 淘宝用户在登录第三方应用时，通过验证后第三方将保留用户的信息。
	 * 
	 * @return
	 */
	public String taobaoLogin() throws IOException, BusinessException {
		User currentUser = super.readCookieUser();
		Object objLogin = super.readCookie("isLogin");
		Object objAction = super.readCookie("action"); // 当前动作
		// cookie['action'].value == \"bindedAccount\
		boolean isAssociationAccount = false; // 当前操作是否是绑定关联账号
		if (currentUser != null && objLogin != null && objAction != null) {
			// 已经登录 && 当前操作是 绑定账号
			if ("true".equalsIgnoreCase(objLogin.toString())
					&& "bindedAccount".equalsIgnoreCase(objAction.toString())) {
				isAssociationAccount = true; // 如果当前用户存在而且是登录状态就认为是绑定关联账号
			}
		}
		// 淘宝登录时记录用户登录方式
		tempLoginMessage = null;
		taobaoLogin = 1;
		resultResponse = null;
		String customerId = null;
		request.setCharacterEncoding("UTF-8");
		String appkey = request.getParameter("top_appkey");
		String sign = request.getParameter("top_sign");
		if (sign == null)
			sign = request.getParameter("sign");
		String topSession = request.getParameter("top_session");
		String parameters = request.getParameter("top_parameters");
		// 保存淘宝参数至session中,发货使用
		session.put("sessionKey", topSession);

		logger.debug(appkey + ":        :" + sign + ":        :" + topSession
				+ ":        :" + parameters);
		if (appkey != null && sign != null && topSession != null
				&& parameters != null) {

			try {
				// 首先验证淘宝用户登录签名
				boolean verifyTop = Util.verifyTopResponse(parameters,
						topSession, sign, appkey, ConfigUtilSingle
								.getInstance().getTOP_SECRET());
				if (verifyTop) {
					logger.debug("验证淘宝用户登录签名:" + verifyTop);
					// 时间戳验证(本应用设置时间误差为15分钟)
					boolean validateTime = Util.validateTimestamp(
							Util.resolvingParameters(parameters, "ts"), 30);

					if (validateTime) {
						String visitorId = Util.resolvingParameters(parameters,
								"visitor_id");
						// logger.error("visitor_id："+visitorId);
						resultResponse = Util.getResult(ConfigUtilSingle
								.getInstance().getOFFICALURL(), TaoBaoAction
								.getTaoBaoUser(ConfigUtilSingle.getInstance()
										.getFORMAT_XML(), ConfigUtilSingle
										.getInstance().getTAOBAOUSERMETHOD(),
										ConfigUtilSingle.getInstance()
												.getSIGN_METHOD_MD5(),
										ConfigUtilSingle.getInstance()
												.getTOP_APPKEY(), topSession,
										visitorId));
						InputStream inputStream = Util
								.getInputStream(resultResponse);
						User tbu = Util.createObject(inputStream);

						if (tbu == null
								|| StringUtils.isEmpty(tbu.getShopAccount())) {
							logger.error("appkey:" + appkey + "sign:" + sign
									+ "topSession:" + topSession
									+ "parameters:" + parameters);
							logger.error("解析淘宝参数，构造user失败" + ",resultResponse:"
									+ resultResponse);
							return "loginError";
						}

						List<String> testers = ConfigUtilSingle.getInstance()
								.getTestBuyerNameList();
						/**
						 * 判断用户是卖家
						 */
						String hasShop = tbu.getHasShpo();
						if (!testers.contains(tbu.getShopAccount())
								&& ("false").equals(hasShop)
								&& StringUtils.equals(ConfigUtilSingle
										.getInstance().isForbidBuyer(), "true")) {
							/**
							 * @author wangyong 2012-01-10
							 *         此处因为response相应了一个地址。所以在退出时不能显示loginMessage的值
							 *         。做一修改，跳到一个新的页面上提示用户“对不起，您不是淘宝卖家，
							 *         无法使用淘宝帐号登录”
							 */
							taobaoLoginTimes = 1;
							String logoff = TaoBaoAction.getlogoffURL(
									ConfigUtilSingle.getInstance()
											.getSIGN_METHOD_MD5(),
									ConfigUtilSingle.getInstance()
											.getTOP_APPKEY());
							response.sendRedirect(logoff);
							return "taobaoLoginError";
						}

						if (isAssociationAccount) {
							customerId = TaoBaoAction.getCustomerId(topSession);
							if (customerId.equalsIgnoreCase(currentUser
									.getTaobaoEncodeKey())) {
								associationResult = "selfAccount"; // 不能绑定自己的账号
							} else {
								User tempUser = userService
										.getUserByCustomerId(customerId);
								if (tempUser != null
										&& StringUtils.isNotBlank(tempUser
												.getUserCode())) {
									tbu.setUserCode(tempUser.getUserCode());
									tbu.setUserState("1");
									userService.updateUser(tbu);
								}
								boolean isCanBind = (StringUtils
										.isNotBlank(currentUser.getUserCode()) && (currentUser
										.getUserCode().equalsIgnoreCase(
												tbu.getUserCode()) || StringUtils
										.isBlank(tempUser.getUserCode())))
										|| (StringUtils.isBlank(currentUser
												.getUserCode()) && StringUtils
												.isBlank(tempUser.getUserCode()));
								if (!isCanBind) {
									associationResult = "difUserCode"; // 不同网点的账号不能关联
									associationResult = "您的店铺与["
											+ tempUser.getUserName()
											+ "]的客户编码不一致，请调整一致后重新关联。<br/>"
											+ "<br><br>["
											+ currentUser.getUserName()
											+ "]的客户编码："
											+ StringUtils.defaultIfBlank(
													currentUser.getUserCode(),
													"暂未绑定网点")
											+ "<br><br>["
											+ tempUser.getUserName()
											+ "]的客户编码："
											+ StringUtils.defaultIfBlank(
													tempUser.getUserCode(),
													"暂未绑定网点");
								}
							}
							if (StringUtils.isNotBlank(associationResult)) {
								return "toAssociationAccount";
							}
						}

						// 如果用户未插入用户信息入库，则插入；如果已经插入过，则跳过。
						boolean result = addUser(tbu, topSession,
								isAssociationAccount);
						if (isAssociationAccount) {
							if (!result) {
								associationResult = "repeatShop"; // 该店铺已经关联啦，无须再次关联
							}
							return "toAssociationAccount";
						}
						logger.debug("淘宝用户账户：" + tbu.getShopAccount());
						resultResponse += "淘宝用户账户：" + tbu.getShopAccount();
						List<User> uList = userService.searchUsersByTaoBao(tbu
								.getShopAccount());
						// 存在的多个相同的用户
						if (uList.size() > 1) {
							throw new RuntimeException(
									"用户数据异常! 存在多个相同用户数据! 请联系管理员! ");
						}
						// 没有对应的用户数据
						if (uList.size() < 1) {
							throw new RuntimeException(
									"用户数据异常! 不存在的用户! 请联系管理员! ");
						}
						// 存在唯一匹配的用户
						if (uList.size() >= 1) {
							User tbUser = uList.get(0);
							// 先登录
							// 拦截器里会判断用户是否激活，如果没激活就会跳转到激活页面
							if ("1".equals(tbUser.getUserState())
									|| "TBA".equals(tbUser.getUserState())) {

								// 这里当卖家登录的时候查看是否被关闭了该功能;如果关闭页面不显示'电子对账'功能
								UserThread userThread = new UserThread();
								userThread.setUserCode(tbUser.getUserCode());

								List<UserThread> utList = userThreadService
										.searchUsersByCode(userThread);
								if (utList.size() > 0) {
									userThread = utList.get(0);
									tbUser.setSwitchEccount(userThread
											.getSwitchEccount());
								}
								writeCookie("taobaoLogin",
										String.valueOf(taobaoLogin));
								saveCookie(tbUser, isRemeber);
								this.recordLoginTime(tbUser);
								isRemeber = 0;
								UserCustom param = new UserCustom();
								param.setRelationalQuery(UserCustom.RELATIONAL);
								param.setType("1");
								param.setUserName(tbUser.getUserName());
								List<UserCustom> uc = userCustomerService
										.searchUserCustom(param);
								if (uc != null && uc.size() > 0) {
									// 标识用户激活第一步已经完成。
									super.writeCookie("infostate", "1");
								}

								// 创建同步淘宝订单任务
								smsBuyersService.createTaskByTaobao(
										tbUser.getId(), topSession);
								/* 将当前登陆时间写入cookie add by qinghua.yang begin */
								String time = DateUtil.format(new Date(),
										"yyyy-MM-dd HH:mm:ss");
								writeCookie("lastShowTime", time);
								// writeCookie("lastShowTimeTemp",
								// time);//记载第一次登陆时间
								/* 将当前登陆时间写入cookie add by qinghua.yang end */
								return "doLogin"; // 进入主页面
							}
							// 无效的用户
							if ("0".equals(tbUser.getUserState())) {
								throw new RuntimeException("用户已失效!");
							} else {
								throw new RuntimeException(
										"用户数据异常! 未知状态码! 请联系管理员! ");
							}
						}
					} else {
						String time = Util.validateTimestampResult(
								Util.resolvingParameters(parameters, "ts"), 30);
						List<String> receiverList = ConfigUtilSingle
								.getInstance().getSendEmailList();
						/** 发送邮件通知 */
						if (!receiverList.isEmpty()) {
							String subject = "验证淘宝用户时间撮失败";
							String content = "验证淘宝用户时间撮失败<br>"+time+"淘宝登录验证参数如下<br>appkey："
									+ appkey
									+ "；<br>top_sign："
									+ sign
									+ ";<br>top_session:"
									+ topSession
									+ ";<br>top_parameters:" + parameters;
							for (String receiver : receiverList) {
								Mail mail = new Mail();
								mail.setSubject(subject);
								mail.setSendToMail(receiver);
								mail.setContent(content);
								mailService.sendMail(mail);
							}
						}
						logger.error("validateTime false");
						logger.error("用户customerId：:" + customerId);
					}
				} else {
					List<String> receiverList = ConfigUtilSingle.getInstance()
							.getSendEmailList();
					/** 发送邮件通知 */
					if (!receiverList.isEmpty()) {
						String subject = "验证淘宝用户登录签名失败";
						String path = request.getLocalAddr()+request.getServletPath();
						Enumeration<String> enumParams = request.getParameterNames();
						int i=0;
						while(enumParams.hasMoreElements()) {
							String name = enumParams.nextElement();
							String value = request.getParameter(name);
							if(i==0) {
								path += "?"+(name+"="+value);
							} else {
								path += "&"+(name+"="+value);
							}
							i++;
						}
						String content = "验证淘宝用户登录签名失败<br>淘宝登录验证参数如下<br>appkey："
							+ appkey
							+ "；<br>top_sign："
							+ sign
							+ ";<br>top_session:"
							+ topSession
							+ ";<br>top_parameters:" + parameters+"<br>"
							+ "回调请求地址是："+path;
						
						for (String receiver : receiverList) {
							Mail mail = new Mail();
							mail.setSubject(subject);
							mail.setSendToMail(receiver);
							mail.setContent(content);
							mailService.sendMail(mail);
						}
					}
				}
			} catch (Exception e) {
				logger.error("login false", e);
				logger.error("解析淘宝参数后的结果：" + resultResponse);
				logger.error("用户customerId：:" + customerId);
			}
		} else {
			if (taobaoLoginTimes == 1) {
				loginMessage = "对不起，您不是淘宝卖家，请使用淘宝卖家账号登录";
				taobaoLoginTimes--;
			} else {
				logger.error("top 参数空");
			}
		}

		return "loginError";
	}

	/**
	 * #关联账号#关联淘宝店铺账号 会调用该方法<br>
	 * 该方法会先调用淘宝接口退出已登录的用户, 然后返回回调地址, 经session拦截进入登录页<br>
	 * 登录页要判断#session.user是否有值. 如果有值就调用应用入口的方法
	 */
	public void toTB() {
		String logooff;
		try {
			logooff = TaoBaoAction.getlogoffURL(ConfigUtilSingle.getInstance()
					.getSIGN_METHOD_MD5(), ConfigUtilSingle.getInstance()
					.getTOP_APPKEY());
			// 设置标识符：当前动作是绑定关联账号.
			// login.jsp会根据这个值判断是否要跳转到淘宝页面验证
			// 绑定成功后要在页面清除这个cookie
			super.writeCookie("action", "bindedAccount", -1);
			response.sendRedirect("http://container.api.taobao.com/container?appkey=12655339&encode=utf-8");
		} catch (Exception e) {
			logger.error("登出error", e);
		}
	}

	/**
	 * 插入用户信息入库
	 * 
	 * @param user
	 * @param isAssociationAccount
	 *            本次操作是否为添加关联账号
	 */
	private boolean addUser(User user, String session,
			boolean isAssociationAccount) {
		if (user != null) {
			String customerId = TaoBaoAction.getCustomerId(session);
			User tempUser = userService.getUserByCustomerId(customerId);
			if (tempUser != null && tempUser.getUserName() != null) {
				user = tempUser;
			} else {
				user.setUserSource("2");// 设置为淘宝用户
				user.setUserType("1"); // 设置淘宝用户的权限角色为vip
				user.setUserState("TBA");
				user.setUserName(user.getShopAccount());
				user.setUserNameText(user.getShopAccount()); // 设置淘宝用户的显示名称为淘宝账号
				user.setShopName(user.getShopAccount());

				// 用户验证通过后要去淘宝验证用户账号 ，取回用户账号 的密文，存入用户表；供对账用
				user.setTaobaoEncodeKey(customerId);

			}

			// 如果是添加关联账号 ，就拷贝目标账号 的信息给关联账号
			if (isAssociationAccount) {
				User currentUser = super.readCookieUser();
				currentUser = userService.get(currentUser);
				List<Integer> userIdList = Resource.getUserRelationUserIdList(currentUser);
				if (user.getId()!=null && userIdList!=null && userIdList.contains(user.getId())) {
					logger.info("账号已经关联");
					return false;
				}
				user.setUserState(currentUser.getUserState());
				user.setAddressCity(currentUser.getAddressCity());
				user.setAddressDistrict(currentUser.getAddressDistrict());
				user.setAddressProvince(currentUser.getAddressProvince());
				user.setAddressStreet(currentUser.getAddressStreet());
				user.setCreateUser(currentUser.getId());
				user.setField001(currentUser.getField001());
				user.setField002(currentUser.getField002());
				user.setField003(currentUser.getField003());
				user.setHasShpo(currentUser.getHasShpo());
				user.setMobilePhone(currentUser.getMobilePhone());
				user.setRemark(currentUser.getRemark());
				user.setSite(currentUser.getSite());
				user.setTelePhone(currentUser.getTelePhone());
				user.setUserCode(currentUser.getUserCode());
				user.setUserLevel(currentUser.getUserLevel());
				user.setUserPassword(currentUser.getUserPassword());
				user.setCardType(currentUser.getCardType());
				if (StringUtils.isEmpty(user.getMail())) {
					user.setMail(currentUser.getMail());
				}
				boolean rs = userService.bindedAssociationAccount(currentUser,
						user);
				if (rs) {
					user = userService.getUserByCustomerId(customerId);
					//更新缓存和cookies
					Resource.setRelationAccountCustoms(currentUser);
					Resource.setRelationAccountCustoms(user);
					Resource.setUserRelationUserIdList(currentUser);
					Resource.setUserRelationUserIdList(user);
					super.writeCookieUser(currentUser);
				}
				return rs;

			} else if (userService.searchUsersByTaoBao(user.getShopAccount())
					.size() < 1) {
				return userService.addUser(user);
			} else {
				logger.error("add 账号 已存在：" + user.getShopAccount());
				return false;
			}
		}

		return false;
	}

	/**
	 * 用户第一次登录系统时如果忘记了用户编码，可以向管理员申请用户编码。管理员收到消息后向用户邮箱发送一条邮件告之。<br>
	 * 申请的消息中包含用户的邮箱地址和用户账户。
	 * 
	 * @return
	 */
	public String applyForCode() {
		inactiveUser = user;
		if (inactiveUser != null) {
			try {
				// applyString = userService.applyForCode(inactiveUser, content,
				// linkway);
				List<User> adminList = (List<User>) userService
						.getUserListByUserType("3");
				Integer receiveUserId = adminList.get(0).getId();

				if (StringUtils.isEmpty(content)) {
					logger.error("参数异常! 消息内容为空!");
				}
				User u = readCookieUser();
				String subject = u.getUserName() + "申请网点编码";// 设置消息主题
				content = "消息内容：[" + content + "]";
				if (messageService.sendAdvise(inactiveUser, subject, content))
					applyString = "消息发送成功，我们将会通过您留下的联系方式联系您！";
				else
					applyString = "发送失败";
			} catch (Exception e) {
				logger.error("申请客户编码失败", e);
			}
		} else {
			applyString = "用户信息为空! 请联系管理员!";
		}
		// inactiveUser = null;
		return "applyForCode";
	}

	/**
	 * 快速入门执行
	 * 
	 * @return
	 */
	public String quickRead() {
		quickReadString = "";
		return "quickRead";
	}

	public String showPasswordUI() {
		return "showPasswordUI";
	}

	public String updateCode() {
		String code = (String) request.getSession().getAttribute("c");
		codeString = code;
		return "updateCode";
	}

	/**
	 * 跳转到激活页面<br>
	 * 所有未激活或者第一次登陆的用户都会先进入激活页面
	 * 
	 * @return
	 */
	public String toActive() {
		return "toActive";
	}

	public String jiaocai() {
		return "jiaocai";
	}

	public String audioMenu() {
		return "audioMenu";
	}

	/**
	 * 申请客户编码视频
	 * 
	 * @return
	 */
	public String audio1() {
		return "audio1";
	}

	/**
	 * cookie测试
	 * 
	 * @return
	 */
	public String testCookie() {
		return "testCookie";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public String getCodeString() {
		return codeString;
	}

	public void setCodeString(String codeString) {
		this.codeString = codeString;
	}

	public int getIsRemeber() {
		return isRemeber;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setIsRemeber(int isRemeber) {
		this.isRemeber = isRemeber;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public Integer getValidate() {
		return validate;
	}

	public void setValidate(Integer validate) {
		this.validate = validate;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	public String getAjaxAlertText() {
		return ajaxAlertText;
	}

	public void setAjaxAlertText(String ajaxAlertText) {
		this.ajaxAlertText = ajaxAlertText;
	}

	public String getResultResponse() {
		return resultResponse;
	}

	public void setResultResponse(String resultResponse) {
		this.resultResponse = resultResponse;
	}

	public User getInactiveUser() {
		return inactiveUser;
	}

	public void setInactiveUser(User inactiveUser) {
		this.inactiveUser = inactiveUser;
	}

	public String getApplyString() {
		return applyString;
	}

	public void setApplyString(String applyString) {
		this.applyString = applyString;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setLinkway(String linkway) {
		this.linkway = linkway;
	}

	public String getAssociationResult() {
		return associationResult;
	}

	public void setAssociationResult(String associationResult) {
		this.associationResult = associationResult;
	}

	public static int getTaobaoLoginTimes() {
		return taobaoLoginTimes;
	}

	public static void setTaobaoLoginTimes(int taobaoLoginTimes) {
		LoginAction.taobaoLoginTimes = taobaoLoginTimes;
	}

	public void setErrorTimes(Integer errorTimes) {
		this.errorTimes = errorTimes;
	}

	public Integer getErrorTimes() {
		return errorTimes;
	}

	public void setRandomStr(String randomStr) {
		this.randomStr = randomStr;
	}

	public String getRandomStr() {
		return randomStr;
	}

	public String getQuickReadString() {
		return quickReadString;
	}

	public void setQuickReadString(String quickReadString) {
		this.quickReadString = quickReadString;
	}

}
