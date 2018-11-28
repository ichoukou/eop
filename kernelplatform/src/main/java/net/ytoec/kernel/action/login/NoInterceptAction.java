package net.ytoec.kernel.action.login;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.MessageUserService;
import net.ytoec.kernel.service.QuestionDtoService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.bean.Column;
import com.ytoec.cms.service.ArticleService;
import com.ytoec.cms.service.ColumnService;
import com.ytoec.uninet.util.HessianUtil;

/**
 * 该action里的方法不会被拦截<br>
 * 主要在这里定义一些不需要登录验证(不能被拦截)的方法; 如注册时的ajax用户名/用户编号验证<br>
 * 
 * @author ChenRen
 * @date 2011-8-22
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class NoInterceptAction extends AbstractActionSupport {

	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;

	@Inject
	private UserCustomService<UserCustom> userCustomService;
	@Inject
	private MessageService<Message> messageService;
	
	@Inject
	private QuestionDtoService<DtoQuestion> dtoQuestionService;
	
	@Inject
	private MessageUserService<MessageUser> messageUserService;
	
	@Inject
    private ArticleService<Article> articleService;
	
	@Inject
	private ColumnService<Column> columnService;
	
	private User user;
	
	private Article article;

	private String password;

	private String rePassword;

	private static final long serialVersionUID = 2018317964699197119L;
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
	/** 点击我知道了传过来的参数 用来判断跳转到哪个action */
	private String nextAction;
	private String methodName;
	
	//未读消息数目
	private Integer unReadMessageNum = 0;
	//未读问题件
	private Integer unReadQuestionNum = 0;
	//卖家未处理问题件
	private Integer unHandleQuestionNum = 0;
	
	private String jsonResult;
	
	private String flag; //弹出消息框表示  1弹出
	
	private Integer currentPage = 1; //当前页默认为1
	
	private static Integer pageNum = 15; //每页显示的数据条数
	
	private Pagination pagination; //分页对象
	
	private List<Article> articleList;

	private List<String> types;
	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	private String newIcon;
	
    public Article getArticle() {
        return article;
    }

    
    public void setArticle(Article article) {
        this.article = article;
    }

    public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	// === actions ===
	/**
	 * 检查用户名的唯一性<br>
	 * 该方法不会进行登录验证.<br>
	 * 如果要登录后才能操作, 参照{@link net.ytoec.kernel.action.user.UserAction#checkName}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkName() throws Exception {
		// 验证通过true，否则false
		isValidate = userService.checkName(user) ? "true" : "false";
		return "jsonData";
	} // checkName

	/**
	 * 用户编码验证<br>
	 * 该方法不会进行登录验证.<br>
	 * 如果要登录后才能操作, 参照
	 * {@link net.ytoec.kernel.action.user.UserAction#checkUserCode}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkUserCode() throws Exception {
		// 用户编码为空
		if (StringUtils.isEmpty(user.getUserCode())) {
			isValidate = "false";
			ajaxAlertText = "参数错误：用户编码为空!";
		} else {
			// User cuser = (User) session.get(Constants.SESSION_USER);
			User cuser = super.readCookieUser();

			UserThread userThread = new UserThread();
			userThread.setUserCode(user.getUserCode());
			userThread.setUserName(cuser.getShopAccount());

			String msg = userThreadService.checkUserCode(userThread);
			/*
			 * // 验证通过 if(msg.toUpperCase().indexOf("TBA") > -1 ) { isValidate =
			 * "true"; // 验证通过不会显示服务器信息, 所以不要定义ajaxAlertText值 } else { // 验证失败.
			 * 返回服务器提示信息 isValidate = "false"; ajaxAlertText = msg; }
			 */
			if ("true".equalsIgnoreCase(msg)) {
				isValidate = "true";
			}
		}
		return "jsonCheckUserCode";
	} // checkUserCode

	/**
	 * 网点编码是否存在验证<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkSite() throws Exception {
		// 用户编码为空
		// if(StringUtils.isEmpty(user.getSite() ) ) {
		if (user.getSite() == null) {
			isValidate = "false";
			ajaxAlertText = "参数错误：网点编码为空!";
		} else {

			String msg = userService.checkSite(user);
			// 验证通过
			if (StringUtils.isEmpty(msg)) {
				isValidate = "true";
				// 验证通过不会显示服务器信息, 所以不要定义ajaxAlertText值
			}
			// 验证失败. 返回服务器提示信息
			else {
				isValidate = "false";
				ajaxAlertText = msg;
			}
		}
		return "jsonData";
	} // checkSite

	/**
	 * 激活账号 绑定淘宝用户<br>
	 * 激活失败返回信息到页面; 成功就自动登录并跳转到后台页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String bindTaoBaoUser() throws Exception {

		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}

		// User session_user = (User) session.get(Constants.SESSION_USER);
		User session_user = super.readCookieUser();

		// -> 修改userthread表里的数据
		// @ ChenRen/2011-11-02
		UserThread userThread = new UserThread();
		userThread.setUserName(session_user.getShopAccount());
		userThread.setUserCode(user.getUserCode());

		List<UserThread> listuh = userThreadService
				.searchUsersByCode(userThread);
		if (listuh.size() != 0) {
			userThread = listuh.get(0);
			userThread.setUserState("1"); // 设置用户状态为正常

			userThreadService.editUser(userThread);
		}
		// ->

		// --> @2011-11-14/ChenRen 增加
		String nametext = userThread.getUserName();
		if (!StringUtils.isEmpty(nametext)) {
			user.setUserNameText(nametext);
		}
		// -->
		user.setSite(userThread.getSiteCode());
		if (userService.bindTaoBaoUser(user)) {
			// User session_user = (User) session.get(Constants.SESSION_USER);
			session_user.setUserCode(user.getUserCode());
			session_user.setUserState("1"); // 设置用户状态
			session_user.setSite(user.getSite());
			String pwd = user.getUserPassword();
			if (!StringUtils.isEmpty(pwd)) {
				session_user.setUserPassword(Md5Encryption.MD5Encode(pwd));
			}
			session_user.setMobilePhone(user.getMobilePhone());
			session_user.setTelePhone(user.getTelePhone());
			session_user.setMail(user.getMail());

			// 记录登录时间
			session_user.setLoginTime(new Date());
			userService.updateUser(session_user);

			// 重新设置用户的session
			// session.remove(Constants.SESSION_USER);
			// session.put(Constants.SESSION_USER, session_user);
			// super.session.put(Constants.LASTLOGINTIME,
			// session_user.getLoginTime());
			super.writeCookieUser(session_user);
			isValidate = "true";
		} else {
			isValidate = "false";
		}

		return "jsonData";
	} // bindTaoBaoUser

	/**
	 * 弹出层提交，第一步完善基本信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String beforerBindTaoBaoUserStep1() throws Exception {
		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		} else {
			Integer id = user.getId();
			User userSource = userService.getUserById(id);
			userSource.setUserPassword(user.getUserPassword());
			userSource.setAddressProvince(user.getAddressProvince());
			userSource.setAddressCity(user.getAddressCity());
			userSource.setAddressDistrict(user.getAddressDistrict());
			userSource.setAddressStreet(user.getAddressStreet());
			userSource.setField001(user.getField001());
			userSource.setShopName(user.getShopName());
			userSource.setMobilePhone(user.getMobilePhone());

			user = userSource;
			bindTaoBaoUserStep1();
			if ("success".equals(ajaxAlertText)) {
				isValidate = "true";
			} else {
				isValidate = "false";
			}
			return "jsonData";
		}
	}

	/**
	 * 激活账号 绑定淘宝用户：第一步 完善用户信息<br>
	 * 用户填写除了用户编码以外的字段进行操作， 保存成功进入下一个页面提示用户填写用户编码； 否则，还是在当前页面。
	 * 
	 * @return
	 * @throws Exception
	 */
	public String bindTaoBaoUserStep1() throws Exception {
		if (user == null) {
			//throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		    putMsg(JsonResponse.INFO_TYPE_ERROR, false, "对象参数信息为空!", "");
		}
		User session_user = super.readCookieUser();

		try {
			if ("1".equals(session_user.getUserType())
					&& StringUtils.isNotBlank(session_user.getChildType())) {
				user.setShopAccount(user.getUserName());
			}
			if (userService.bindTaoBaoUserStep1(user)) {
				String pwd = user.getUserPassword();
				if (!StringUtils.isEmpty(pwd)) {
					session_user.setUserPassword(Md5Encryption.MD5Encode(pwd));
				}
				session_user.setMobilePhone(user.getMobilePhone());
				session_user.setTelePhone(user.getTelePhone());
				session_user.setMail(user.getMail());
				session_user.setShopName(user.getShopName());
				session_user.setAddressCity(user.getAddressCity());
				session_user.setAddressDistrict(user.getAddressDistrict());
				session_user.setAddressProvince(user.getAddressProvince());
				session_user.setAddressStreet(user.getAddressStreet());
				session_user.setField001(user.getField001());// 保存激活时用户所属省的ID号
				session_user.setField003("9");// 9表示用户只填写了基本信息，还没有激活

				// 重新设置用户的session
				// session.remove(Constants.SESSION_USER);
				// session.put(Constants.SESSION_USER, session_user);
				super.writeCookieUser(session_user);
				// 返回操作提示到页面. 让用户选择下一步的操作

				UserCustom userCustom = new UserCustom();
				userCustom.setUserName(session_user.getUserName());
				userCustom.setBindedUserName(user.getUserName()); // 目标对象的
				userCustom.setCustomerId(session_user.getTaobaoEncodeKey()); // 目标对象的
				userCustom.setType("1");
				userCustom.setRelationalQuery(UserCustom.RELATIONAL);
				userCustomService.add(userCustom); // 往个性化配置表中插入一条记录
				Resource.setRelationAccountCustoms(session_user); // 更新缓存
				//标识用户激活第一步已经完成。
				super.writeCookie("infostate","1");

				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "基本信息保存成功！", "");
				ajaxAlertText = "success";
				//return "g_toActive4taobao";

			} else {
				ajaxAlertText = "信息保存失败! 请联系管理员!";
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "信息保存失败! 请联系管理员!", "");
				//return "g_toActive4taobao";
			}
		} catch (Exception e) {
			ajaxAlertText = e.getMessage();
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "信息保存失败! 请联系管理员!", "");
			//return "g_toActive4taobao";
		}
		return "jsonRes";

	} // bindTaoBaoUser

	public String toBindTBUserStep1() throws Exception {
		// user = (User) session.get(Constants.SESSION_USER);
		user = super.readCookieUser();
		user = userService.get(user); // 把数据库中的对象查询传到页面// 因为cookie中值不全
		// return "g_toActive4vip";
		return "g_toActive4taobao";
	}

	public String toBindTBUserStep2() throws Exception {
		user = super.readCookieUser();
		user = userService.get(user); // 把数据库中的对象查询传到页面// 因为cookie中值不全
		// user = (User) session.get(Constants.SESSION_USER);
		return "g_toActive4taobaoStep2";
		// return "g_toActive4vipStep2";
	}

	/**
	 * 绑定用户编码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toBindUC() throws Exception {
		request.setAttribute("url", "/leaveMessage_index.action");
		return toBindTBUserStep2();
	}

	/**
	 * 绑定用户编码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toBindUC2() throws Exception {
		// User session_user = (User) session.get(Constants.SESSION_USER);
		User session_user = super.readCookieUser();
		// 拼接当前请求url. 激活成功后跳转到当前页面
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("url", "/user!toEdit.action?user.id="
				+ session_user.getId());
		user = session_user;
		return toBindTBUserStep2();
	}

	/**
	 * 激活账号 绑定淘宝用户<br>
	 * 激活失败返回信息到页面; 成功就自动登录并跳转到后台页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String bindTaoBaoUserStep2() throws Exception {
	    boolean isAutoGen = false;
		if (user == null || StringUtils.isBlank(user.getUserCode())) {
			//throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		    putMsg(JsonResponse.INFO_TYPE_ERROR, false, "对象参数信息为空!", "");
		}
		//自动绑定网点
        else if(StringUtils.equals(user.getUserCode(), "autoGen") && StringUtils.isNotEmpty(user.getSite())){
            isAutoGen = true;
            user.setUserCode(userThreadService.generateUserCode(user.getSite()));
        }
		

		User session_user = super.readCookieUser();
		UserThread userThread = new UserThread();
		userThread.setUserCode(user.getUserCode());
		List<UserThread> listuh = userThreadService
				.searchUsersByCode(userThread);
		if (listuh == null || listuh.isEmpty()) {
			isValidate = "false";
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "客户编码不存在!", "");
			return "jsonRes";
		}
		userThread = listuh.get(0);
		if(isAutoGen && StringUtils.isNotEmpty(session_user.getShopName())){
            userThread.setUserName(session_user.getShopName());
        }
		userThread.setUserState("1"); // 设置用户状态为正常
		if (user.getId() == null) {
			user.setId(session_user.getId());
		}
		user.setSite(userThread.getSiteCode());
		user.setShopAccount(session_user.getShopAccount());

		if (userService.bindTaoBaoUserStep2(user)) {
			userThread.setUserCodeUpteTime(DateUtil
					.valueOfStandard("2011-11-19")); // 更新时间戳为易通上线日期:2011-11-19

			userThreadService.editUser(userThread);

			session_user.setUserCode(user.getUserCode());
			session_user.setUserState("1"); // 设置用户状态
			session_user.setSite(user.getSite());
			// 记录登录时间
			session_user.setLoginTime(new Date());

			if(readCookie("infostate") == null || !"1".equals(readCookie("infostate"))){
			  //标识用户激活第一步已经完成。
                super.writeCookie("infostate","1");
			}
			super.writeCookieUser(session_user);
			isValidate = "true";

			sendWelcomeAdvise(session_user);
			// 发送用户，网点
			String userNameText = session_user.getUserNameText();
			if (userNameText == null || "".equals(userNameText))
				userNameText = session_user.getUserName();
			sendActivateMessage(session_user, userThread.getSiteCode(),
					userNameText);
			putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "网点绑定成功!", "");
		} else {
			isValidate = "false";
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "网点绑定失败!", "");
		}

		return "jsonRes";
	} // bindTaoBaoUser

	/**
	 * 激活账号 <br>
	 * 激活失败返回信息到页面; 成功就自动登录并跳转到后台页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String activateUser() throws Exception {

		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}

		if (userService.activateUser(user)) {
			isValidate = "true";
		} else {
			isValidate = "false";
		}

		/*
		 * 新增信息的时候user对象不为空. 然后调用list方法会根据user对象里的属性进行条件查询，所以把user置空。
		 */
		user = null;
		return "jsonCheckUserCode";
	} // add

	private String response;
	private String loginName;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * 网点用户没有完善信息之前首先完善用户信息。网点在完善信息之后发送一条消息告知和一条建议消息的通知使用。
	 * 
	 * @author wangyong
	 * @return
	 */
	public String completeSite() {
		User currUser = super.readCookieUser();
		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}
		if (!this.checkSellerActivate()) {
			response = "手机或电话号码不符合规则！";
			return INPUT;
		}
		if(currUser.getId()!=null)
			user.setId(currUser.getId());
		if(currUser.getUserName()!=null)
			user.setUserName(currUser.getUserName());
		if(currUser.getUserNameText()!=null)
			user.setUserNameText(currUser.getUserNameText());
		if (userService.completeSite(user)) {
			sendWelcomeMessage(currUser);
			sendWelcomeAdvise(currUser);

			Integer id = user.getId();
			User userSource = userService.getUserById(id);
			super.writeCookieUser(userSource);

			response = "激活成功，请重新登录";
		} else {
			response = "激活失败";
		}
		return "completeSite";
	}

	/**
	 * 发送一条系统消息，告知网点
	 * 
	 * @param receiveUserId
	 * @return
	 */
	public boolean sendWelcomeMessage(User receiveUser) {
		boolean flag = false;
		String messageContent = "恭喜您激活易通平台，为了方便您和您的客户进行电子对账和运单监控：<BR>1、请先为您的客户在金刚系统中申请“客户编码”，并选择类型为“电子商务客户”。&nbsp;&nbsp;&nbsp; <BR>2、通知客户使用淘宝账号登录易通平台，并把客户编码告诉给客户，让客户进行账号激活。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 具体看“<A  onclick=\"window.open('mainPage_toHelp.action')\"><B>帮助支持网点版</B></A>”链接";
		String messageTheme = "欢迎光临";
		flag = messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
		return flag;
	}

	/**
	 * 用户激活 发送一条系统消息，告知用户 ,网点
	 * 
	 * @param receiveUserId
	 * @return
	 */
	public boolean sendActivateMessage(User receiveUser, String site,
			String userNameText) {
		boolean flag = false;
		String messageContent = "恭喜您激活成功，请联系您的网点进行运费模板设置，方便您进行电子对账。";
		String messageTheme = "欢迎光临";
		flag = messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
		// 发送网点
		List<User> wdList = userService.searchUsersBySiteAndUserType(site, "2");
		if (wdList.size() > 0) {
			messageContent = "恭喜"
					+ userNameText
					+ "客户已激活并成为您的客户，为了方便您使用电子对账功能，请为您的客户添加运费模板&nbsp;&nbsp;&nbsp; 请点击“<A href='javascript:opendzdz(0);' target='main'><B>运费模板</B></A>“链接";
			messageTheme = "客户激活消息【系统】";
			flag = messageService.sendSystemMessage(wdList.get(0), messageTheme, messageContent);
		}
		return flag;
	}

	public boolean sendWelcomeAdvise(User receiveUser) {
		boolean flag = false;
		String messageContent = "您好，欢迎登录易通电子商务物流平台！欢迎在建议意见栏提出宝贵的建议意见。";
		String messageTheme = "欢迎提出宝贵的建议意见";
		flag = messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
		return flag;
	}

	/**
	 * 验证登录账号知否唯一
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkLoginName() throws Exception {
		User user = userService.searchUsersByUserName(loginName);
		if (user != null && user.getUserName() != null) {
			response = "1";// 该账号已存在
		} else {
			response = "0";// 该账号可用
		}
		return "checkLoginName";
	}

	public String iKnow() throws SecurityException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		User currentUser = super.readCookieUser();
		UserCustom uct = Resource.getFirstVisit(currentUser);
		if (uct != null) {
			// 如果已经有个性化配置记录则执行更新操作
			uct.getClass().getMethod(methodName, String.class).invoke(uct, "1");// 执行方法从前台通过methodName传过来
			userCustomService.edit(uct);

		}
		// else{
		// //没有则创建一条个性化配置记录 TODO
		//
		// }
		// 刷新缓存
		Resource.setFirstVisit(currentUser);
		// 操作完成后跳转对应的ACTION中
		return nextAction;
	}

	// 验证手机号码和固定电话的格式
	private boolean checkSellerActivate() {
		Boolean flag = null;
		Pattern pattern = null;
		Matcher matcher = null;
		if (null != this.user) {
			if ((null != user.getMobilePhone() && !"".equals(user
					.getMobilePhone().trim()))
					|| (null != user.getTelePhone() && !"".equals(user
							.getTelePhone().trim()))) {
				if (null != user.getMobilePhone()
						&& !"".equals(user.getMobilePhone().trim())) {
					String mobilePhone = user.getMobilePhone().trim();
					pattern = Pattern.compile("^^(13|14|15|18)[0-9]{9}$");
					matcher = pattern.matcher(mobilePhone);
					if (matcher.matches()) {
						flag = true;
					} else {
						this.addFieldError("user.mobilePhone",
								"您输入的移动电话号码格式不正确!");
						flag = false;
					}
				}
				if (null != user.getTelePhone()
						&& !"".equals(user.getTelePhone().trim())) {
					String telePhone = user.getTelePhone().trim();
					pattern = Pattern
							.compile("(([0+]\\d{2,3}-)?(0\\d{2,3})-)(\\d{7,8})(-(\\d{2,}))?");
					matcher = pattern.matcher(telePhone);
					if (matcher.matches()) {
						flag = true;
					} else {
						this.addFieldError("user.telePhone", "您输入的固定电话号码格式不正确!");
						flag = false;
					}
				}
			} else {
				this.addFieldError("user.mobilePhone",
						"手机号码和固定电话必须填写一个（易通承诺不会将您的信息透露给第三方）!");
				this.addFieldError("user.telePhone",
						"手机号码和固定电话必须填写一个（易通承诺不会将您的信息透露给第三方）!");
				flag = false;
			}
			if ((!StringUtils.isNotBlank(user.getAddressProvince())
					|| !StringUtils.isNotBlank(user.getAddressCity())
					|| !StringUtils.isNotBlank(user.getAddressDistrict()) || !StringUtils
					.isNotBlank(user.getAddressStreet()))) {
				this.addFieldError("user.addressProvince", "请输入完整的地址!");
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 实时获取未读问题件和未读消息数目
	 * @return
	 */
	public String getUnReadNum(){
		user = super.readCookieUser();
		unReadMessageNum = 0;
		Integer userId = null;
		if(user!=null && user.getUserType()!=null){
			if(user.getUserType().equals("1") || user.getUserType().equals("2") || user.getUserType().equals("3") || user.getUserType().equals("4"))
				userId = user.getId();
			else
				userId = user.getParentId();
		}
		if(userId!=null){
			List<MessageUser> list = messageUserService.getByStatusAndUser(userId, 0);
			if(list != null && list.size() > 0)
				unReadMessageNum = list.size();
			try {
				unReadQuestionNum = dtoQuestionService.noneReadQuestion(user, "0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			unReadMessageNum=unReadQuestionNum=0;
		}
		jsonResult = "";
		jsonResult += unReadMessageNum.toString() + ",";
		jsonResult += unReadQuestionNum.toString();
		return "getUnReadNum";
	}
	/**
     * 顶部“帮助中心”链接功能
     * @return
     */
    public String toHelp() {
        try{
        user = super.readCookieUser();
        String readerType = "1";
        if(user != null && StringUtils.isNotBlank(user.getUserType())){
            readerType = user.getUserType().startsWith("2") ? "2" : "1";
        }
//      List<Article> articles = articleService.getArticlesByCol("col_help_center", readerType);
        List<Article> articles = articleService.getArticlesByCol("help_home", readerType);
        if(articles.size() > 0){
            article = articles.get(0);
            article.setContent(addPathToImg(article.getContent()));
        }}catch(Exception e){
            article = new Article();
        }
        return "toHelp";
    }
    
	/**
     * 每个栏目的帮助链接
     * @return
     */
    public String eachHelp() {
        user = super.readCookieUser();
        String readerType = "1";
        if(user != null && StringUtils.isNotBlank(user.getUserType())){
            readerType = user.getUserType().startsWith("2") ? "2" : "1";
        }
        if(article != null && StringUtils.isNotBlank(article.getRemark())){
            try{
            List<Article> articles = articleService.getArticlesByCol(article.getRemark(), readerType);
            if(articles.size() > 0){
                article = articles.get(0);
                article.setContent(addPathToImg(article.getContent()));
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return "each_help";
    }
    
    /**
     * 给文章内容中的图片等文件添加域名，以便显示
     * 
     * @param content
     * @return
     */
    private String addPathToImg(String content) {
        if (!StringUtils.isNotEmpty(content)) {
            return "";
        }
        List<String> imgUris = getImgUris(content, false);
        for (String uri : imgUris) {
            content = content.toLowerCase().replace(uri, getAbsImgUrl(uri));
        }
        return content;
    }
    
    /**
     * 获取新增的或修改的图片uri以上传（原有的附件不作任何操作）
     * 
     * @param content 文章内容
     * @param onlyTemp 是否只取临时图片URI(需要当前上传的图片)
     * @return
     */
    private List<String> getImgUris(String content, boolean onlyTemp) {
        List<String> imgUris = new ArrayList<String>();
        if (!StringUtils.isNotEmpty(content)) {
            return imgUris;
        }
        if (content.toLowerCase().contains("<img")) {
            int start = content.toLowerCase().indexOf("<img");
            int end = content.toLowerCase().indexOf("/>", start);
            String tmpAbsURL = HessianUtil.getMediaPath() + "/temp";
            while (start > -1 && end > -1) {
                start = content.toLowerCase().indexOf("src=", start) + 5;
                end = content.toLowerCase().indexOf("\"", start);
                String uri = content.substring(start, end);
                start = content.toLowerCase().indexOf("<img", end);
                end = content.toLowerCase().indexOf("/>", start);
                if (!uri.contains(tmpAbsURL) && onlyTemp) {
                    continue;
                }
                if (uri.contains(tmpAbsURL)) {
                    uri = uri.replace(tmpAbsURL, "");
                }
                imgUris.add(uri);
            }
        }
        return imgUris;
    }

    /**
     * 给图片添加服务器域名，以便页面显示
     * 
     * @param uri
     * @return
     */
    private String getAbsImgUrl(String uri) {
        if (StringUtils.isNotEmpty(uri) && uri.startsWith("/")) {
            return HessianUtil.getMediaPath() + uri;
            // return HessianUtil.getDomain() + uri;
        } else {
            return uri;
        }
    }

    /**
     * 页面加载的时候，通过ajax方式，进行读取，获取new.png
     * 
     * */
    public String getIcon(){
    	
    	 user = super.readCookieUser();
    	 String username = null;
    	 UserCustom userCustom = null;
    	 
    	 if(user.getUserType().equals("1")
    			 ||user.getUserType().equals("2")
    			 ||user.getUserType().equals("11")
    			 ||user.getUserType().equals("12")
    			 ||user.getUserType().equals("13")
    			 ||user.getUserType().equals("14")
    			 ||user.getUserType().equals("21")
    			 ||user.getUserType().equals("22")
    			 ||user.getUserType().equals("23")
    			 ||user.getUserType().equals("24"))
    	 {
    		 username = user.getUserName();
    		 userCustom = new UserCustom();
        	 userCustom.setUserName(username);
    	 }else{
    		 return "type";
    	 }
    	List<UserCustom> userList = userCustomService.searchUserCustom(userCustom);
    	types = new ArrayList<String>();
    	if(!CollectionUtils.isEmpty(userList)){
    		for(UserCustom custom:userList){
    			types.add(custom.getType());
    		}
    	}
    	
		return "type";
    	
    }
    /**
     * 点击左侧菜单栏，删除new.png
     * 
     * */
    public String remove(){
    	
    	 user = super.readCookieUser();
    	 String username = null;
    	 
    	 if(user.getUserType().equals("1")
    			 ||user.getUserType().equals("2")
    			 ||user.getUserType().equals("11")
    			 ||user.getUserType().equals("12")
    			 ||user.getUserType().equals("13")
    			 ||user.getUserType().equals("14")
    			 ||user.getUserType().equals("21")
    			 ||user.getUserType().equals("22")
    			 ||user.getUserType().equals("23")
    			 ||user.getUserType().equals("24"))
    	 {
    		 
    		 username = user.getUserName();
    	 }
    	 
    	 UserCustom userCustom = new UserCustom();
    	 userCustom.setUserName(username);
    	
    	 List<UserCustom> userList = userCustomService.searchUserCustom(userCustom);
    	
    	 if(!CollectionUtils.isEmpty(userList)){
     		
     		for(UserCustom custom:userList){
     			if(custom.getType().equals(newIcon)){
     				userCustomService.remove(custom);
     			}
     			
     		}
     	}
    	
		return "newIcon";
    }
    
    
    /**
     * 视频
     * @return
     */
    public String audio() {
        return "audio";
    }
    
    /**
     * 用户第一登陆进来loginDate=logintime，而后续刷新不等于
     * @return
     */
    public String firstLoginOrF5(){
    	String loginDate = "";
    	String logintime = "";
    	
    	try{
    		loginDate = (String) super.readCookie("lastShowTime");
    		logintime = (String) super.readCookie("lastShowTimeTemp"); 
    		
    	}catch(Exception e){
    		System.out.println(e);
    	}
    	if(logintime.equals(loginDate)){
    		writeCookie("lastShowTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			putMsg(JsonResponse.INFO_TYPE_MSG,true,"","");
    	}else{
    		putMsg(JsonResponse.INFO_TYPE_MSG,false,"","");
    	}
    	
    	return "jsonRes";
    }
    
    /**
     * 每隔5分钟弹出消息层
     * @return
     */
    public String checkMsgAlert(){
    	String loginDate = (String) super.readCookie("lastShowTime");
    	if (StringUtils.isBlank(loginDate)) {
            return "jsonRes";
        }
    	Date now = new Date();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	double mins = 0;
    	try {
			mins = DateUtil.minuteInterval(now,sdf.parse(loginDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(mins >= 5){
			writeCookie("lastShowTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			putMsg(JsonResponse.INFO_TYPE_MSG,true,"","");
		}
		else{
			putMsg(JsonResponse.INFO_TYPE_MSG,false,"","");
		}
    	return "jsonRes";
    }
    
    public String moreNews(){
    	user = super.readCookieUser();
    	pagination = new Pagination(currentPage,pageNum);
    	Map<String, Object> params = new HashMap<String, Object>();
    	String columnCode = "yitong_01";
    	Column col = columnService.retrive(columnCode);
    	if(user.getUserType()!=null){
	    	if(user.getUserType().equals("1") || user.getUserType().equals("11") || user.getUserType().equals("12") || user.getUserType().equals("13")){
	    		params.put("columnId", col.getColumnId());
	    		params.put("readerType", 1);
	    		pagination = articleService.getPageList(pagination, params);
	    		articleList = pagination.getRecords();
	    		
	    		return "moreNews";
	    	}
	    	
	    	if(user.getUserType().equals("2") || user.getUserType().equals("21") || user.getUserType().equals("22") || user.getUserType().equals("23")){
	    		params.put("columnId", col.getColumnId());
	    		params.put("readerType", 2);
	    		pagination = articleService.getPageList(pagination, params);
	    		articleList = pagination.getRecords();
	    		
	    		return "moreNews";
			}
	    	
	    	if(user.getUserType().equals("4") || user.getUserType().equals("41")){
	    		params.put("columnId", col.getColumnId());
	    		params.put("readerType", 1);
	    		params.put("status", 2);
	    		pagination = articleService.getPageList(pagination, params);
	    		articleList = pagination.getRecords();

	    		return "moreNews";
	    	}
	    	params.put("columnId", col.getColumnId());
	    	pagination = articleService.getPageList(pagination, params);
    		articleList = pagination.getRecords();
    	}
    	return "moreNews";
    }

   /**
    * 联系我们
    * @return
    */
    public String contractUs(){
    	
    	return "contractus";
    }
    /**
     * 关于易通
     * @return
     */
    public String onYto(){
    	
    	return "onyto";
    }
    
	// === getter && setter ===
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public String getAjaxAlertText() {
		return ajaxAlertText;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public Integer getUnReadMessageNum() {
		return unReadMessageNum;
	}

	public void setUnReadMessageNum(Integer unReadMessageNum) {
		this.unReadMessageNum = unReadMessageNum;
	}

	public Integer getUnReadQuestionNum() {
		return unReadQuestionNum;
	}

	public void setUnReadQuestionNum(Integer unReadQuestionNum) {
		this.unReadQuestionNum = unReadQuestionNum;
	}

	public Integer getUnHandleQuestionNum() {
		return unHandleQuestionNum;
	}

	public void setUnHandleQuestionNum(Integer unHandleQuestionNum) {
		this.unHandleQuestionNum = unHandleQuestionNum;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}


	public UserCustomService<UserCustom> getUserCustomService() {
		return userCustomService;
	}


	public void setUserCustomService(UserCustomService<UserCustom> userCustomService) {
		this.userCustomService = userCustomService;
	}


	public List<String> getTypes() {
		return types;
	}


	public void setTypes(List<String> types) {
		this.types = types;
	}


	public String getNewIcon() {
		return newIcon;
	}


	public void setNewIcon(String newIcon) {
		this.newIcon = newIcon;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public Pagination getPagination() {
		return pagination;
	}


	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}


	public Integer getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}


	public List<Article> getArticleList() {
		return articleList;
	}


	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}
		
	
	
}
