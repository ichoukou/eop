package net.ytoec.kernel.action.user;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.JsonUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserBean;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.dto.BranchMailNoDTO;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.dto.QuestionnaireResultsUpdateDTO;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.UserBeanService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadContractService;
import net.ytoec.kernel.service.UserThreadService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.taobao.api.domain.Order;

/**
 * 
 * @author ChenRen
 * @date 2011-7-26
 */
@Controller
@Scope("prototype")
public class UserAction extends AbstractActionSupport {

	private Logger logger = LoggerFactory.getLogger(UserAction.class);
	// === fields ===
	private static final long serialVersionUID = 1754742686308746474L;
	// private static Integer pageNum = 10; // 每页显示的记录数 // 2011-09-06/ChenRen
	// 使用父对象的

	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private UserThreadContractService<UserThreadContract> userThreadContractService;
	@Inject
	private UserBeanService<UserBean> userBeanService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;
	@Inject
	private PosttempUserDao<PosttempUser> puDao;
	@Inject
	private UserThreadContractDao<UserThreadContract> userThreadContractDao;
	@Inject
	private PosttempService<PosttempUser> posttempService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private QuestionnaireService<QuestionnaireResultsUpdateDTO> questionnaireService;
	@Inject
	private UserRelationService<UserRelation> userRelationService;

	private User user;
	private UserCustom userCustom;
	private List<User> userList;
	private List<UserThread> userThreadList;
	private List<UserBean> userBeanList;
	private List<User> associationAccountList;
	private String ids;
	private String addUserName;
	private String conractAreaId;
	private String accountType;
	private String showLayer;
	private String json;
	private String mobile;// 我要发货时修改发货人手机号码
	/** 运单号 */
	private String mailNo;
	/**
	 * 验证是否通过. 通过json格式返回到前台<br>
	 * 默认为false；
	 */
	private String isValidate;
	private String newPwd;
	private String oldPwd;

	private Integer currentPage = 1;
	private Pagination<User> pagination;

	/** 绑定店铺 */
	private List<User> bindUserList;
	/**
	 * ajax验证的错误提示信息<br>
	 * 如果验证通过可以不写值
	 */
	private String ajaxAlertText;

	/** 0表示要初始化user；我的VIP 的连接中的参数. 见left.jsp */
	private String type;
	/** 当前方法的请求地址（包括参数） */
	private String url;
	/** 新用户编码 */
	private String newUserCode;

	private String newAdress;

	// 承包区
	private List<UserThread> customersList;
	private List<PosttempUser> posttempUserList;
	private String editUserName;
	// 承包区上下区分显示 未分配List：noDeliveryList, 已经分配List:yesDeliveryList
	private List<UserThread> noDeliveryList;
	private List<UserThread> yesDeliveryList;

	private String check;

	private String surePwd;

	public String getSurePwd() {
		return surePwd;
	}

	public void setSurePwd(String surePwd) {
		this.surePwd = surePwd;
	}

	// === actions ===
	/**
	 * 检查用户名的唯一性<br>
	 * <br>
	 * 
	 * 变更历史：<br>
	 * &nbsp; 2011-08-22/ChenRen<br>
	 * &nbsp; 描述：将该方法设置成不推荐使用<br>
	 * &nbsp; 系统会对{@link UserAction}进行登录验证拦截. <br>
	 * &nbsp; 如果需要登录验证通过后才能进行"检查用户名的唯一性", 则使用该方法;<br>
	 * &nbsp; 否则参照：
	 * {@link net.ytoec.kernel.action.login.NoInterceptAction#checkName}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkName() throws Exception {
		// 验证通过true，否则false
		isValidate = userService.checkName(user) ? "true" : "false";
		return "jsonNameCheck";
	} // checkName

	/**
	 * 邮箱校验
	 */
	public void validEdit() {
		String regx = "^[\\w-]+@[\\w-]+((.com)+|(.cn)+|(.net)+|(.mobi)+|(.so NEW)+|(.org)+|(.name)+|(.me)+|(.co NEW)+|(.tel)+|(.info)+|(.biz)+|(.cc)+|(.tv)+|(.hk)+|(.asia)+|(.gov.cn)+|(.com.cn)+|(.net.cn)+|(.org.cn)+|(.公司)+|(.网络)+|(.中国)+)$";
		String userMail = user.getMail();
		Pattern pattern = Pattern.compile(regx);
		if (userMail != null) {
			Matcher matcher = pattern.matcher(userMail);
			if (!matcher.matches()) {
				this.addActionError("邮箱格式不正确");
			}
		}
	}

	/**
	 * 网点发邮件通知卖家使用面单打印功能
	 * 
	 * @return
	 */
	public String sendEmail() {
		User currentUser = super.readCookieUser();
		try {
			userService.sendEmail(currentUser);
			json = "true";
		} catch (JSONException e) {
			e.printStackTrace();
			json = "false";
		}
		return "sendEmail";
	}

	/**
	 * 跳转到新增的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAdd() throws Exception {
		user = null;
		return "toAdd";
	} // toAdd

	/**
	 * 跳转到新增的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toHelp() throws Exception {
		return "toHelp";
	} // toAdd

	/**
	 * 新增<br>
	 * 新增成功跳到列表页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {

		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}

		// User currentUser = (User) session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		// 网点用户
		if ("2".equals(currUser.getUserType())) {
			// 设置VIP用户的所属网点
			user.setSite(currUser.getSite());
		}

		if (!userService.addUser(user)) {
			this.addActionMessage("添加数据失败!");
			return ERROR;
		}
		/*
		 * 新增信息的时候user对象不为空. 然后调用list方法会根据user对象里的属性进行条件查询，所以把user置空。
		 */
		user = null;
		return list();
	} // add

	/**
	 * 我的客户从userThread表中提取数据。再从user表中查找数据。
	 * 
	 * @author wangyong
	 * @2012-01-05
	 */
	private String userCode;// 我的客户查询条件：直客编码
	private Integer userThreadId;// 直客id
	private String userName;// 我的客户查询条件：直客姓名
	private String userState = "1";// 激活状态：1、激活；TBA:未激活.默认查询已激活用户
	private Integer eccount;// 是否关闭电子对账功能(1:关闭；0：开启)
	private String responseJson;

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String list() throws Exception {
		// 直接通过url调用这个方法显示数据的时候，user为null
		/*
		 * 第一次通过左边菜单进来，user为空; 当去操作其他菜单，然后返回再操作的时候，user不为空。
		 * 因为界面使用了struts2标签，会自动保留搜索条件。 所以在左边菜单入口指定一个参数表示要初始化user
		 */

		// 保存当前请求路径（包括请求参数），保存到页面
		// ‘运费模板管理’操作完毕要返回这个url
		url = request.getRequestURI().substring(1) + "?1=1";
		Map map = request.getParameterMap();
		Set<Map.Entry> entrySet = map.entrySet();
		for (Map.Entry entry : entrySet) {
			Object objVal = entry.getValue();
			String val = "";
			if (objVal instanceof String[]) {
				val = ((String[]) objVal)[0];
			} else {
				val = objVal.toString();
			}
			// 对url中的中文进行utf-8编码。因为运费模板的action是重定向到该url
			url += "&" + entry.getKey() + "=" + URLEncoder.encode(val, "UTF-8");
		}

		if ("0".equals(type)) {
			type = null;
			user = new User();
		}
		if (user == null) {
			user = new User();
		}

		// User currUser = (User) session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		// 网点用户
		if ("2".equals(currUser.getUserType())) {
			// 设置VIP用户的所属网点
			user.setSite(currUser.getSite());
		}

		pagination = new Pagination(currentPage, pageNum);

		/**
		 * @author wangyong
		 * @2012-01-05
		 */
		// userList = userService.searchUsers(user, pagination, true);
		// pagination.setTotalRecords(userService.searchUsers(user, pagination,
		// false).size());

		List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
		contractList = userThreadContractService
				.getContractersByUserNameAndType(currUser.getUserName(),
						currUser.getUserType());
		if (contractList == null || contractList.size() == 0) {
			if (currUser.getParentId() != null) {// 承包区，客服，财务没有分配客户的时候
				userBeanList = null;
				pagination.setTotalRecords(0);
			} else { // 网点账号
				userBeanList = userBeanService.getUserList(null,
						currUser.getSite(), userCode, userName, userState,
						pagination, true);
				pagination.setTotalRecords(userBeanService.countUserList(null,
						currUser.getSite(), userCode, userName, userState,
						pagination, false));
			}

		} else {
			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < contractList.size(); i++) {
				ids.add(contractList.get(i).getConractAreaId());
			}
			userBeanList = userBeanService.getContractUserList(null, ids,
					pagination, userState, true);
			pagination.setTotalRecords(userBeanService.countContractUserList(
					null, ids, pagination, userState, false));
		}

		return "toList";
	} // list

	/**
	 * 开启或关闭电子对账
	 * 
	 * @return
	 */
	public String switchEccount() {
		UserThread ut = userThreadService.getUserById(userThreadId);
		ut.setSwitchEccount(eccount.toString());
		if (userThreadService.editUser(ut))
			responseJson = "true";
		else
			responseJson = "false";
		return "switchEccount";
	}

	/**
	 * 查询用户列表<br>
	 * 跳转到user_list_modelWindow.jsp<br>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String listModelWindow() throws Exception {
		// 直接通过url调用这个方法显示数据的时候，user为null
		/*
		 * 第一次通过左边菜单进来，user为空; 当去操作其他菜单，然后返回再操作的时候，user不为空。
		 * 因为界面使用了struts2标签，会自动保留搜索条件。 所以在左边菜单入口指定一个参数表示要初始化user
		 */
		if ("0".equals(type)) {
			type = null;
			user = new User();
		}
		if (user == null) {
			user = new User();
		}

		// User currUser = (User) session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		// 网点用户
		if ("2".equals(currUser.getUserType())) {
			// 设置VIP用户的所属网点
			user.setSite(currUser.getSite());
		}

		pagination = new Pagination(currentPage, 20);

		userList = userService.searchUsers(user, pagination, true);
		pagination.setTotalRecords(userService.searchUsers(user, pagination,
				false).size());

		return "toListModelWindow";
	} // list

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String listPosttempUser() throws Exception {
		if ("0".equals(type)) {
			type = null;
			user = new User();
		}
		if (user == null) {
			user = new User();
		}

		User currUser = super.readCookieUser();

		// 网点用户
		if ("2".equals(currUser.getUserType())
				|| "22".equals(currUser.getUserType())
				|| "23".equals(currUser.getUserType())) {
			// 设置VIP用户的所属网点
			user.setSite(currUser.getSite());
			Integer branchId = currUser.getId();
			if (!"2".equals(currUser.getUserType())) {
				// 获取主账号的网点
				User siteU = userService.getUserById(currUser.getParentId());
				branchId = siteU.getId();
			}
			user.setField002(branchId.toString()); // 设置用户的网点Id
		}

		pagination = new Pagination(currentPage, pageNum);

		List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
		contractList = userThreadContractService
				.getContractersByUserNameAndType(currUser.getUserName(),
						currUser.getUserType());

		if (contractList != null && contractList.size() != 0) { // 四种账号类型分配客户的子账户(客服，财务，客服、财务，承包区)的选择用户
			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < contractList.size(); i++) {
				ids.add(contractList.get(i).getConractAreaId());
			}

			// 相关匹配模板列表
			List<PosttempUser> puList = puDao.getPosttempUserByBranchId(Integer
					.parseInt(user.getField002()));
			// 直客表的客户数
			userThreadList = userThreadService.getContractUserList("0", ids,
					pagination, "1", true);

			for (PosttempUser posttempUser : puList) {
				for (UserThread u : userThreadList) {
					if (u.getId().equals(posttempUser.getVipId())) {
						// 设置用户的备用字段field003为false，标识当前用户已经分配过模板
						// 页面显示用户的时候判断该值，如果为false，不让网点选择该用户
						u.setBackup1("false");
					}
				}
			} // for posttempUser

			pagination.setTotalRecords(userBeanService.countContractUserList(
					"0", ids, pagination, "1", false));

		} else {// 网点账号的选择用户或四种账号类型没有分配客户
			if (currUser.getParentId() != null) {// 承包区，客服，财务没有分配客户的时候
				userThreadList = null;
				pagination.setTotalRecords(0);
				return "toListModelWindow";
			}
			// 网点账号的选择用户
			List<UserThreadContract> siteContractList = userThreadContractService
					.searchContractsBysiteId(currUser.getId() + "", "2");
			ArrayList<Integer> ids = new ArrayList<Integer>();
			if (siteContractList != null && siteContractList.size() != 0) {
				for (int k = 0; k < siteContractList.size(); k++) {
					ids.add(siteContractList.get(k).getConractAreaId());
				}
			}
			if (ids != null && ids.size() != 0) {
				user.setIds(ids);
			} else {
				user.setIds(null);
			}
			userThreadList = listPosttempUserThread(user, pagination, true);
			List<UserThread> utLists = listPosttempUserThread(user, pagination,
					false);
			if (utLists == null || "".equals(utLists)) {
				pagination.setTotalRecords(0);
			} else {
				pagination.setTotalRecords(utLists.size());
			}

		}

		return "toListModelWindow";
	} // list

	/**
	 * 跳转到用户编辑的页面
	 * 
	 * @return
	 */
	public String toEdit() throws Exception {
		User currUser = super.readCookieUser();
		if (currUser == null) {
			return StringUtils.EMPTY;
		}
		Integer id = currUser.getId();
		user = userService.getUserById(id);
		/**
		 * 如果是网点用户就判断该用户是否分配过模板
		 */
		// User currUser = (User) session.get(Constants.SESSION_USER);

		boolean x = false;

		if (("2").equals(currUser.getUserType())) {
			x = true;
		} else if (("21").equals(currUser.getUserType())
				|| ("22").equals(currUser.getUserType())
				|| ("23").equals(currUser.getUserType())) {
			x = true;
			id = userService.getUserById(currUser.getParentId()).getId(); // 取父账号的Id
		}
		if (x) {
			if (puDao.getPosttempUserByBranchId(id) != null
					&& puDao.getPosttempUserByBranchId(id).size() > 0) {
				user.setField003("true");// 已创建模板，不可修改发货地址
			}
		}

		// 获取绑定店铺用户
		bindUserList = new ArrayList<User>();
		for (String cur : Resource.getBindedCustomerIdList(currUser)) {
			User u = userService.getUserByCustomerId(cur);
			if (u != null) {
				if (StringUtils.isNotEmpty(u.getShopName()))
					bindUserList.add(u);
			}
		}

		return "toEdit";
	} // toEdit

	public String toEditIndex() throws Exception {
		User currUser = super.readCookieUser();
		if (currUser == null) {
			return StringUtils.EMPTY;
		}
		Integer id = currUser.getId();
		user = userService.getUserById(id);
		/**
		 * 如果是网点用户就判断该用户是否分配过模板
		 */
		// User currUser = (User) session.get(Constants.SESSION_USER);

		boolean x = false;

		if (("2").equals(currUser.getUserType())) {
			x = true;
		} else if (("21").equals(currUser.getUserType())
				|| ("22").equals(currUser.getUserType())
				|| ("23").equals(currUser.getUserType())) {
			x = true;
			id = userService.getUserById(currUser.getParentId()).getId(); // 取父账号的Id
		}
		if (x) {
			if (puDao.getPosttempUserByBranchId(id) != null
					&& puDao.getPosttempUserByBranchId(id).size() > 0) {
				user.setField003("true");// 已创建模板，不可修改发货地址
			}
		}

		// 获取绑定店铺用户
		bindUserList = new ArrayList<User>();
		for (String cur : Resource.getBindedCustomerIdList(currUser)) {
			User u = userService.getUserByCustomerId(cur);
			if (u != null) {
				if (StringUtils.isNotEmpty(u.getShopName()))
					bindUserList.add(u);
			}
		}

		return "toEdit";
	} // toEdit

	/**
	 * 编辑用户并保存<br>
	 * 编辑成功跳到列表页
	 * 
	 * @return
	 */
	public String edit() throws Exception {
		if (userService.editUser(user)) {
			ajaxAlertText = "修改成功!";
			super.writeCookieUser(userService.get(user));
			return toEdit();
		} else {
			return ERROR;
		}
	} // edit

	/**
	 * 我要发货页面修改用户发货地址<br>
	 * 
	 * 
	 * @return
	 */
	public String editAddress() throws Exception {
		User currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		String[] adress = newAdress.split(",");
		currentUser.setAddressProvince(adress[0]);
		currentUser.setAddressCity(adress[1]);
		currentUser.setAddressDistrict(adress[2]);
		currentUser.setAddressStreet(adress[3]);

		if (userService.editUser(currentUser)) {
			ajaxAlertText = "修改成功!";
			super.writeCookieUser(currentUser);
			return "jsonEditAddress";
		}
		return "jsonEditAddress";
	}

	/**
	 * 修改密码<br>
	 * 
	 * @return
	 */
	public String editPwd() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");

		// User currUser = (User) session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		user = new User();
		user.setUserName(currUser.getUserName());
		user.setUserPassword(oldPwd);

		boolean isSuccess = false;
		try {
			isSuccess = userService.edit_pwd(user, newPwd);
			if (isSuccess && readCookie("password") != null
					&& "1".equals(readCookie("isRemeber").toString())) {
				writeCookie("password", newPwd, -1);
			}
			isValidate = isSuccess ? "true" : "false";
			putMsg(JsonResponse.INFO_TYPE_MSG, isSuccess, "", "");
		} catch (Exception e) {
			isValidate = e.getMessage();
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "", "");
		}
		return "jsonRes";
	}

	public String getNewPwd() {
		return newPwd;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	/**
	 * 验证密码
	 * 
	 * @return
	 */
	public String checkPassword() {
		// User currUser = (User) session.get(Constants.SESSION_USER);
		if (StringUtils.isNotBlank(oldPwd)) {
			User currUser = super.readCookieUser();
			User user = new User();
			user.setUserName(currUser.getUserName());
			user.setUserPassword(oldPwd);
			boolean isSuccess = false;
			isSuccess = userService.checkPassword(user);
			isValidate = isSuccess ? "true" : "false";
			putMsg(JsonResponse.INFO_TYPE_MSG, isSuccess, isSuccess ? ""
					: "原密码不正确", "");
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "请填写原密码", "");
		}
		return "jsonRes";
	}

	/**
	 * ajax验证用户名是否可用
	 * 
	 * @return 可用：true,不可用：false
	 */
	public String checkUserName() {
		boolean isSuccess = true;
		if (user == null || StringUtils.isBlank(user.getUserName())) {
			isSuccess = false;
		}
		isSuccess = userService.checkName(user);
		isValidate = isSuccess ? "true" : "false";
		putMsg(JsonResponse.INFO_TYPE_MSG, isSuccess, "", "");
		return "jsonRes";
	}

	/**
	 * ajax验证CustomerId(即taobaoEncodeKey)是否可用
	 * 
	 * @return 可用：true,不可用：false
	 */
	public String checkCustomerId() {
		if (user == null || StringUtils.isBlank(user.getTaobaoEncodeKey())) {
			isValidate = "false";
		} else {
			String customerId = user.getTaobaoEncodeKey();
			user = userService.getUserByCustomerId(customerId);
			if (user == null) {
				customerId = user.getTaobaoEncodeKey();
				user = userService.getUserByCustomerId(customerId);
			}
			isValidate = (user != null && user.getId() != null) ? "false"
					: "true";
		}
		putMsg(JsonResponse.INFO_TYPE_MSG,
				StringUtils.equals(isValidate, "true"), "", "");
		return "jsonRes";
	}

	/**
	 * 验证一个用户账号是否已经被别的账号关联。
	 * 
	 * @return true-被别的账号关联，false-没有被关联
	 */
	public String checkUserIsRelated() {
		User currentUser = super.readCookieUser();
		List<Integer> list = Resource.getUserRelationUserIdList(currentUser);
		if (list != null && list.size() > 0) {
			StringBuffer content = new StringBuffer();
			for (Integer id : list) {
				User tempUser = userService.getUserById(id);
				if (StringUtils.isNotBlank(content.toString())) {
					content.append(content + "，");
				}
				content.append(tempUser.getUserName());
			}
			putMsg(JsonResponse.INFO_TYPE_ERROR, true, content.toString(), "");
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "", "");
		}
		return "jsonRes";
	}

	/**
	 * 撤消账号的被关联关系
	 * 
	 * @return
	 */
	public String cancelRelation() {
		User currentUser = super.readCookieUser();
		List<Integer> list = Resource.getUserRelationUserIdList(currentUser);
		if (list != null && list.size() > 0) {
			for (Integer id : list) {
				User user = userService.getUserById(id);
				boolean result = userRelationService.cancelRelation(
						currentUser, user);
				if (result) {
					// 更新缓存currentUser/user
					Resource.setRelationAccountCustoms(currentUser);
					Resource.setRelationAccountCustoms(user);
					Resource.setUserRelationUserIdList(currentUser);
					Resource.setUserRelationUserIdList(user);
				}

			}
			putMsg(JsonResponse.INFO_TYPE_ERROR, true, "关联关系撤消操作已成功！", "");
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "该账号没有被其它账号关联！", "");
		}
		return "jsonRes";
	}

	/**
	 * 修改网点编码<br>
	 * 
	 * @return
	 */
	public String editSite() throws Exception {
		if (userService.editSite(user)) {
			return toEdit();
		} else {
			return ERROR;
		}
	} // edit_site

	/**
	 * 查看此客户编码是否还有人使用，若无人使用把直客信息表中user_state改为TBA 此方法在解除绑定的方法中被调用。
	 * 
	 * @return
	 */
	public void updateUserStateOfUserThread() {
		User currentUser = super.readCookieUser();
		List<User> users = userService.searchUsersByCode(currentUser);
		if (users != null && users.size() > 1) {
		} else {
			UserThread userThread = new UserThread();
			userThread.setUserCode(currentUser.getUserCode());
			List<UserThread> userThreads = userThreadService
					.searchUsersByCode(userThread);
			if (userThreads != null && userThreads.size() > 0) {
				// 如果直客信息从金刚同步过来修改为TBA，若为易通系统自动生成则直接删除掉
				userThread = userThreads.get(0);
				if (StringUtils.isNotEmpty(userThread.getUserCode())
						&& StringUtils.startsWith(userThread.getUserCode(),
								"YT")) {
					userThreadService.delUserById(userThread.getId());
				} else {
					userThread.setUserCode("TBA");
					userThreadService.editUser(userThread);
				}
			}
		}
	}

	/**
	 * 验证用户编码<br>
	 * 验证前台输入的用户编码和当前用户的用户编码是否一致
	 */
	public String checkUserCode() throws Exception {
		if (StringUtils.isEmpty(user.getUserCode())) {
			isValidate = "false";
		} else {
			// User cu = (User) session.get(Constants.SESSION_USER);
			User cu = super.readCookieUser();
			isValidate = user.getUserCode().equals(cu.getUserCode()) ? "true"
					: "false";
		}
		return "jsonChechUserCode";
	}

	/**
	 * 验证用户编码<br>
	 * 验证前台输入的用户编码是否存在
	 */
	public String checkNewUserCode() throws Exception {
		if (user != null && StringUtils.isNotBlank(user.getUserCode())) {
			UserThread uh = new UserThread();
			uh.setUserCode(user.getUserCode());
			List<UserThread> list = userThreadService.searchUsersByCode(uh);
			if (list.size() > 0) {
				uh = list.get(0);
				String site = uh.getSiteCode();
				DtoBranch dtoBranch = Resource.getDtoBranchByCode(site);
				ajaxAlertText = dtoBranch == null ? "网点不存在" : StringUtils
						.defaultIfBlank(dtoBranch.getText(), "名称未填写");
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, dtoBranch != null,
						ajaxAlertText, "");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "网点不存在", "");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "用户编码错误", "");
		}
		return "jsonRes";
	}

	/**
	 * 修改用户编码<br>
	 * 1.修改user表中的数据 2.把userthread中的老数据修改成tba，新数据改成1 3.刷新session
	 */
	public String editUserCode() throws Exception {
		isValidate = "false";
		if (user == null && StringUtils.isBlank(user.getUserCode())) {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "用户编码为空，请重新操作！", "");
		} else {
			// 原用户编码要和当前登录用户的用户编码一致
			// User cu = (User) session.get(Constants.SESSION_USER);
			User cu = super.readCookieUser();
			if (!cu.getUserCode().equals(user.getUserCode())) {
				// 修改数据
				boolean flag = userService.editUserCode(cu, cu.getUserCode(),
						user.getUserCode());

				// 更新session
				if (flag) {

					updateUserStateOfUserThread();

					// user.setUserCode(user.getUserCode());
					List<User> uList = userService.searchUsersByCode(user);
					// 同时修改关联账户的网点编码
					for (User u : uList) {
						userService.editUserCode(cu, u.getUserCode(),
								user.getUserCode());
					}

					if (uList.size() > 0) {
						user = uList.get(0);
						cu.setUserCode(user.getUserCode());
						cu.setSite(user.getSite());
						super.writeCookieUser(cu);
						isValidate = "true";
					}
					putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "操作已成功！", "");
				} else {
					putMsg(JsonResponse.INFO_TYPE_ERROR, false,
							"数据更新失败，请重新操作！", "");
				}
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false,
						"新用户编码和原用户编码相同，请修改后重新操作！", "");
			}
		}

		return "jsonRes";
	}

	@SuppressWarnings("unchecked")
	public List<User> listPosttempUser(User entriy,
			Pagination<User> pagination, boolean flag) {
		if (StringUtils.isEmpty(entriy.getField002())) {
			// 设置网点Id是为了根据网点Id去查询已经分配过的模板的用户
			logger.error("参数异常! 没有指定用户的网点Id!");
			return Collections.EMPTY_LIST;
		}

		List<PosttempUser> puList = puDao.getPosttempUserByBranchId(Integer
				.parseInt(entriy.getField002()));
		List<User> uList = userService.searchUsers(entriy, pagination, flag);
		for (PosttempUser posttempUser : puList) {
			for (User u : uList) {
				if (u.getId().equals(posttempUser.getVipId())) {
					// 设置用户的备用字段field003为false，标识当前用户已经分配过模板
					// 页面显示用户的时候判断该值，如果为false，不让网点选择该用户
					u.setField003("false");
				}
			}
		} // for posttempUser

		return uList;
	}

	@SuppressWarnings("unchecked")
	public List<UserThread> listPosttempUserThread(User entriy,
			Pagination<User> pagination, boolean flag) {
		if (StringUtils.isEmpty(entriy.getField002())) {
			// 设置网点Id是为了根据网点Id去查询已经分配过的模板的用户
			logger.error("参数异常! 没有指定用户的网点Id!");
			return Collections.EMPTY_LIST;
		}

		List<PosttempUser> puList = puDao.getPosttempUserByBranchId(Integer
				.parseInt(entriy.getField002()));
		List<UserThread> uList = new ArrayList<UserThread>();
		if (entriy.getIds() != null && entriy.getIds().size() != 0) {
			uList = userThreadService.searchUsersBySiteCodeAndNameAndNotId("0",
					entriy.getIds(), entriy.getSite(), null, "1",
					entriy.getUserName(), pagination, flag);
		} else {
			uList = userThreadService.searchUsersBySiteCodeAndName("0",
					entriy.getSite(), null, "1", entriy.getUserName(),
					pagination, flag);
		}

		for (PosttempUser posttempUser : puList) {
			if (uList != null && !"".equals(uList)) {
				for (UserThread u : uList) {
					if (u.getId().equals(posttempUser.getVipId())) {
						// 设置用户的备用字段field003为false，标识当前用户已经分配过模板
						// 页面显示用户的时候判断该值，如果为false，不让网点选择该用户
						u.setBackup1("false");
					}
				}
			}

		} // for posttempUser

		return uList;
	}

	public String bindTaobaoAccount() {
		boolean flag = userService.bindTaoBaoAccount(user, type);
		isValidate = flag ? "true" : "false";
		return "jsonEditPwd";
	}

	/**
	 * 管理子账号
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toSubAccountList() {
		// User currUser = (User) super.session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		if (currUser == null) {
			logger.info("session用户不存在. 跳转到登陆页面.");
			return "login"; // 重定向到登录页
		}

		pagination = new Pagination(currentPage, pageNum);
		// pagination = new Pagination(currentPage, 2);
		Map map = userService.getUserByParentId(currUser.getId(), pagination);

		associationAccountList = (List) map.get("list");
		for (User tempUser : associationAccountList) {
			// if(StringUtils.equals("2", tempUser.getUserType())){
			List<UserThreadContract> utcList = userThreadContractService
					.getContractersByUserNameAndType(tempUser.getUserName(),
							null);
			if (utcList != null && utcList.size() > 0) {
				List<UserThread> userThreadList = new ArrayList<UserThread>();
				for (UserThreadContract utc : utcList) {
					UserThread tempUT = userThreadService.getUserById(utc
							.getConractAreaId());
					if (tempUT != null) {
						userThreadList.add(tempUT);
					}
				}
				tempUser.setUserThreadList(userThreadList);
			}
			// }
		}
		pagination.setTotalRecords(Integer.parseInt(map.get("totalRecords")
				.toString()));
		return "toSubAccountList";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toSubAccountListIndex() {
		// User currUser = (User) super.session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		if (currUser == null) {
			logger.info("session用户不存在. 跳转到登陆页面.");
			return "login"; // 重定向到登录页
		}

		pagination = new Pagination(currentPage, pageNum);
		Map map = userService.getUserByParentId(currUser.getId(), pagination);

		associationAccountList = (List) map.get("list");
		pagination.setTotalRecords(Integer.parseInt(map.get("totalRecords")
				.toString()));
		return "toSubAccountListIndex";
	}

	/**
	 * 平台用户管理业务账号
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public String toPlatformSubAccountList() {
		User currUser = super.readCookieUser();
		if (currUser == null) {
			logger.info("session用户不存在. 跳转到登陆页面.");
			return "login"; // 重定向到登录页
		}

		pagination = new Pagination(currentPage, super.pageNum);
		Map map = userService.pingTaiSelect(currUser, 0, pagination);

		associationAccountList = (List) map.get("list");
		pagination.setTotalRecords(Integer.parseInt(map.get("totalRecords")
				.toString()));
		return "toPlatformSubAccountList";
	}

	public String toDelSubAccount() {
		Integer userId = user.getId();
		boolean flag = userService.deleSubAccount(userId);

		isValidate = flag ? "true" : "false";
		putMsg(JsonResponse.INFO_TYPE_SUCCESS, flag, flag ? "操作已成功！" : "操作失败",
				"");
		return "toDelSubAccount";
	}

	/**
	 * 新增子账号
	 * 
	 * @return
	 */
	public String toAddSubAccount() {
		user = null;
		return "toAddSubAccount";
	}

	/**
	 * 新增子账号
	 * 
	 * @return
	 */
	public String toAddPlatformSubAccount() {
		user = null;
		return "toAddPlatformSubAccount";
	}

	/**
	 * 新增子账号<br>
	 * 新增成功跳到列表页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addSubAccount() throws Exception {
		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}
		User currUser = super.readCookieUser();
		if (currUser == null) {
			logger.info("cookie用户不存在. 跳转到登陆页面.");
			return "login";
		}
		// 检查用户名唯一性
		boolean flag = userService.checkName(user);
		if (!flag) {
			isValidate = "name";
			return "toAddSubAccount";
		} else if (StringUtils.isEmpty(user.getUserType())
				|| user.getUserType().length() > 2) {
			isValidate = "userType";
			return "toAddSubAccount";
		}
		String temp = user.getUserPassword(); // 密码明文
		user.setClientId(currUser.getClientId());
		user.setAddressProvince(currUser.getAddressProvince());
		user.setAddressCity(currUser.getAddressCity());
		user.setAddressStreet(currUser.getAddressStreet());
		user.setAddressDistrict(currUser.getAddressDistrict());
		user.setBindedCustomerId(currUser.getBindedCustomerId());
		if ("2".equals(user.getUserType())) {
			user.setUserCode(user.getUserName());
		}
		user.setSwitchEccount(currUser.getSwitchEccount());
		if ("2".equals(user.getUserType())) {
			user.setSite(user.getUserName());
		} else {
			user.setSite(currUser.getSite());
		}
		user.setField001(currUser.getField001());
		user.setField002(currUser.getField002());
		user.setField003(currUser.getField003());

		// 平台用户添加分仓或入驻企业
		if ("1".equals(user.getUserType())
				&& "4".equals(currUser.getUserType())
				&& StringUtils.isNotBlank(user.getChildType())) {
			user.setTaobaoEncodeKey(user.getTaobaoEncodeKey());
			user.setUserSource(currUser.getId().toString());
			user.setParentId(null);
			user.setShopName(user.getUserNameText());
			user.setUserState("TBA");
			user.setShopAccount(user.getShopAccount());
		} else {
			user.setUserState("1");
			user.setShopName(currUser.getShopName()); // 不复制
			user.setParentId(currUser.getId());
			user.setUserSource(currUser.getUserSource());
		}

		if (ids != null && !"".equals(ids)
				&& "2".equals(currUser.getUserType())) {
			User currentUser = super.readCookieUser();
			List<UserThreadContract> userContractList = new ArrayList<UserThreadContract>();
			String[] idss = ids.split(",");
			for (int s = 0; s < idss.length; s++) {
				UserThread userth = userThreadService.getUserById(Integer
						.parseInt(idss[s]));
				UserThreadContract utc = new UserThreadContract();
				if (currUser.getParentId() != null) {
					utc.setSiteId(currentUser.getParentId());
				} else {
					utc.setSiteId(currentUser.getId());
				}
				utc.setConractAreaId(userth.getId());
				utc.setAccountType(user.getUserType());
				utc.setAddUserName(addUserName);

				userContractList.add(utc);
				// userThreadContractService.insertContract(utc);
			}
			user.setUserContractList(userContractList);
		}

		isValidate = userService.addUser(user) ? "true" : "false";

		// 新增成功, 如果是卖家就更新个性化配置
		if ("true".equalsIgnoreCase(isValidate)) {
			user.setUserPassword(temp); // 把明文显示到前台
			String userType = user.getUserType();
			if ("11".equals(userType) || "12".equals(userType)
					|| "13".equals(userType) || "1".equals(userType)) {
				// 卖家要在个性化配置表里增加数据
				UserCustom userCustom = new UserCustom();
				// 平台用户分仓账号和入驻企业账号不复制父账号个性化配置的数据。
				List<UserCustom> list = new ArrayList<UserCustom>();
				if ("1".equals(user.getUserType())
						&& "4".equals(currUser.getUserType())
						&& StringUtils.isNotBlank(user.getChildType())) {
				} else {
					userCustom.setUserName(currUser.getUserName());
					list = userCustomService.searchUserCustom(userCustom); // 父账号的个性化配置数据
					for (UserCustom uc : list) {
						userCustom = uc;
						userCustom.setUserName(user.getUserName()); // 更改为当前子账号
						userCustomService.add(userCustom);

					}
				}
				if ("1".equals(userType) && "4".equals(currUser.getUserType())
						&& list.size() == 0) {
					userCustom.setUserName(user.getUserName());
					userCustomService.add(userCustom);
				}
			}
		}
		if ("4".equals(currUser.getUserType())
				&& StringUtils.isNotBlank(user.getChildType())) {
			if ("true".equalsIgnoreCase(isValidate)) {
				// 平台用户. 已有业务/分仓账号. 更新cookie为1, -1表示cookie生命为会话级
				super.writeCookie("hasFCang", "1", -1);
			}
			return "toAddPlatformSubAccount";
		}
		return "toAddSubAccount";
	}

	/**
	 * 子账号的承包用户删除处理
	 * 
	 * @return
	 */
	public void deleteCustomersList() {
		User currUser = super.readCookieUser();
		String site = currUser.getSite();
		if (currUser.getParentId() == null) {
			userThreadContractService.deleteContractByConractAreaId(null,
					conractAreaId, site);
		} else {
			userThreadContractService.deleteContractByConractAreaId(
					addUserName, conractAreaId, site);
		}

	}

	public void updateContracts() {
		User currUser = super.readCookieUser();
		UserThreadContract utc = new UserThreadContract();
		utc.setAccountType(accountType);
		utc.setAddUserName(addUserName);
		utc.setSite(currUser.getSite());
		userThreadContractService.updateContractByAddUserName(utc);
	}

	public String toEditSubAccount() {
		user = userService.getUserById(user.getId());
		List<UserThreadContract> utcList = userThreadContractService
				.getContractersByUserNameAndType(user.getUserName(), null);
		if (!"2".equals(user.getUserType())
				&& (utcList == null || (utcList != null && utcList.size() == 0))) {
			user.setCanChangeToContract("1");
		}

		return "toEditSubAccount";
	}

	public String toEditPlatformSubAccount() {
		user = userService.getUserById(user.getId());
		return "toEditPlatformSubAccount";
	}

	public String editSubAccount() throws Exception {

		if (user == null) {
			throw new RuntimeException(User.class.getName() + "对象参数信息为空!");
		}

		// User currUser = (User) session.get(Constants.SESSION_USER);
		User currUser = super.readCookieUser();
		if (currUser == null) {
			logger.info("session用户不存在. 跳转到登陆页面.");
			return "login";
		} else if (StringUtils.isEmpty(user.getUserType())
				|| user.getUserType().length() > 2) {
			isValidate = "userType";
			return "toAddSubAccount";
		}

		User dbUser = userService.getUserById(user.getId());
		dbUser.setUserNameText(user.getUserNameText());
		if (!StringUtils.isEmpty(user.getUserPassword())) {
			if (!"******".equals(user.getUserPassword())) {
				dbUser.setUserPassword(Md5Encryption.MD5Encode(user
						.getUserPassword()));
			}
		}
		dbUser.setMail(user.getMail());
		dbUser.setTelePhone(user.getTelePhone());
		dbUser.setMobilePhone(user.getMobilePhone());
		dbUser.setUserType(user.getUserType());
		/**
		 * add by yuyuezhong 2012-09-03 start
		 * 当网点创建的一个非承包区子账号（没有分配客户）进行修改账号类型为“承包区”， 需要修改子账号的site，usercode字段为子账号名称
		 * */
		if ("2".equals(user.getUserType()) && dbUser.getParentId() != null) {
			dbUser.setSite(user.getUserName());
			dbUser.setUserCode(user.getUserName());
		}

		/** add by yuyuezhong 2012-09-03 end */

		if (ids != null && !"".equals(ids)
				&& "2".equals(currUser.getUserType())) {
			User currentUser = super.readCookieUser();
			List<UserThreadContract> userContractList = new ArrayList<UserThreadContract>();
			String[] idss = ids.split(",");
			for (int s = 0; s < idss.length; s++) {
				UserThread userth = userThreadService.getUserById(Integer
						.parseInt(idss[s]));
				UserThreadContract utc = new UserThreadContract();
				if (currUser.getParentId() != null) {
					utc.setSiteId(currentUser.getParentId());
				} else {
					utc.setSiteId(currentUser.getId());
				}
				utc.setConractAreaId(userth.getId());
				utc.setAccountType(user.getUserType());
				utc.setAddUserName(addUserName);

				userContractList.add(utc);
			}
			dbUser.setUserContractList(userContractList);
		}
		isValidate = userService.edit(dbUser) ? "true" : "false";
		if ("4".equals(currUser.getUserType())
				&& StringUtils.isNotBlank(user.getChildType())) {
			return "toEditPlatformSubAccount";
		}

		return "toEditSubAccount";
	}

	/**
	 * 删除子账号
	 * 
	 * @return
	 */
	public String bindSubAccount() {
		boolean flag = userService.bindSubAccount(user, type);
		isValidate = flag ? "true" : "false";
		putMsg(JsonResponse.INFO_TYPE_SUCCESS, flag, flag ? "操作已成功！" : "操作失败",
				"");
		return "jsonRes";
	}

	/**
	 * 未绑定客户编码的卖家，根据最近的10条订单的运单号去金刚接口取网点编码 1.根据当前卖家customerId取最近10条订单
	 * 2.拿这10个运单号去金刚接口取网点编码，然后过滤出最匹配的网点信息集合 3.返回网点信息给弹出层使用。
	 */
	public String getSiteInfo() {
		User currentUser = super.readCookieUser();
		// 单个运单号获取网点
		if (StringUtils.isNotEmpty(mailNo)) {
			String mailNoArray[] = new String[1];
			mailNoArray[0] = mailNo;
			List<BranchMailNoDTO> list = questionnaireService
					.searchOrgCodeByMailNo(mailNoArray);
			if (list != null && list.size() > 0) {
				String siteCode = list.get(0).getOrgCode();

				User branch = Resource.getUserBySiteCode(siteCode);

				putMsg(JsonResponse.INFO_TYPE_SUCCESS, siteCode != null
						&& branch != null, siteCode, siteCode != null
						&& branch != null ? branch.getUserNameText()
						: "您输入的运单号没有对应网点，请检查后重新操作！");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false,
						"您输入的运单号没有对应网点，请检查后重新操作！", "");
			}
		}
		// 多运单号获取网点
		else if (StringUtils.isNotEmpty(currentUser.getTaobaoEncodeKey())) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerId", currentUser.getTaobaoEncodeKey());
			params.put("limit", 10);
			List<String> mailNos = orderService
					.queryTenOrdersByCustomerId(params);
			List<BranchMailNoDTO> list = questionnaireService
					.searchOrgCodeByMailNo(mailNos.toArray(new String[0]));
			if (list != null && list.size() > 0) {
				// 过滤掉重复的网点编码
				List<String> siteCodeList = new ArrayList<String>();
				for (BranchMailNoDTO mailDto : list) {
					if (StringUtils.isNotEmpty(mailDto.getOrgCode())) {
						if (siteCodeList.contains(mailDto.getOrgCode())) {
							continue;
						} else {
							siteCodeList.add(mailDto.getOrgCode());
						}
					}
				}

				List<DtoBranch> branchList = new ArrayList<DtoBranch>();
				User siteUser = null;
				for (String siteCode : siteCodeList) {
					siteUser = Resource.getUserBySiteCode(siteCode);
					if (siteUser != null) {
						DtoBranch dtoBranch = new DtoBranch();
						dtoBranch.setCode(siteUser.getUserName());
						dtoBranch.setMobile(siteUser.getMobilePhone());
						dtoBranch.setPhone(siteUser.getTelCode()
								+ siteUser.getTelePhone());
						dtoBranch.setStatus(siteUser.getUserState());
						dtoBranch.setText(siteUser.getUserNameText());
						branchList.add(dtoBranch);
					}
				}
				putMsg(JsonResponse.INFO_TYPE_MSG, branchList.size() > 0, "",
						"", branchList);
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false,
						"您尚无运单记录，无法自动获取用户编码，请向所属网点申请！", "");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "用户customerId为空", "");
		}
		return "jsonRes";
	}

	/**
	 * 网点--创建客户编码
	 */
	public String generateUserCode() {
		if (user == null || StringUtils.isEmpty(user.getSite())) {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "网点编码为空，请重新操作！", "");
		} else if (StringUtils.equals(user.getUserCode(), "autoGen")
				&& StringUtils.isNotEmpty(user.getUserName())) {
			UserThread ut = new UserThread();
			ut.setUserName(user.getUserName());
			ut.setSiteCode(user.getSite());
			ut = userThreadService.generateUserThread(ut);
			putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, ut.getUserCode(), ut
					.getId().toString());
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "缺少参数", "生成失败");
		}
		return "jsonRes";
	}

	/**
	 * 网点-删除客户编码
	 */
	public String delUserCode() {
		if (userThreadId != null && userThreadId > 0) {
			UserThread ut = userThreadService.getUserById(userThreadId);
			if (ut != null && StringUtils.isNotEmpty(ut.getUserCode())
					&& ut.getUserCode().toUpperCase().startsWith("YT")
					&& StringUtils.equalsIgnoreCase("TBA", ut.getUserState())) {

				boolean flag = userThreadService.delUserById(userThreadId);
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, flag, "", "");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false,
						"该客户已经被绑定或数据有误不能删除，操作失败", "");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "缺少参数，操作失败", "");
		}
		return "jsonRes";
	}

	/**
	 * 根据用户编码查询网点
	 * 
	 * @return
	 */
	public String queryBranch() {
		UserThread uh = new UserThread();
		uh.setUserCode(newUserCode);
		List<UserThread> uhList = userThreadService.searchUsersByCode(uh);
		if (uhList != null && uhList.size() > 0) {

			UserThread u = uhList.get(0);
			// List<UserThreadContract> contractList =
			// userThreadContractService.searchContractByConractAreaId(u.getId(),
			// "2");
			// if(contractList!=null && !contractList.isEmpty()){//被分配给承包区了
			// User cbq =
			// userService.getUserByUserName(contractList.get(0).getAddUserName());
			// String site = cbq.getSite();
			// DtoBranch dtoBranch = Resource.getDtoBranchByCode(site);
			// ajaxAlertText = dtoBranch == null ? "网点不存在" :
			// dtoBranch.getText();
			// }else{
			// String site = uhList.get(0).getSiteCode();
			// DtoBranch dtoBranch = Resource.getDtoBranchByCode(site);
			// ajaxAlertText = dtoBranch == null ? "网点不存在" :
			// dtoBranch.getText();
			// }
			if (u != null && StringUtils.isNotEmpty(u.getSiteCode())) {
				DtoBranch dtoBranch = Resource.getDtoBranchByCode(u
						.getSiteCode());
				ajaxAlertText = dtoBranch == null ? "网点不存在" : dtoBranch
						.getText();
			} else {
				ajaxAlertText = "用户编码不正确";
			}
		} else {
			ajaxAlertText = "用户编码不正确";
		}
		return "jsonQueryBranch";
	}

	/**
	 * 解除绑定
	 * 
	 * @return
	 */
	public String unbindUserCode() {
		// 查看此客户编码是否还有人使用，若无人使用把直客信息表中user_state改为TBA
		updateUserStateOfUserThread();
		// 原用户编码要和当前登录用户的用户编码一致
		User cu = super.readCookieUser();
		cu = userService.get(cu);
		boolean flag = userService.unbindUserCode(cu);

		// 更新session
		cu = userService.getUserById(cu.getId());
		// cu.setUserState("TBA");//设为未激活状态
		// session.put(Constants.SESSION_USER, cu);
		super.writeCookieUser(cu);

		isValidate = flag ? "true" : "false";
		putMsg(JsonResponse.INFO_TYPE_MSG, flag, "", "");
		return "jsonRes";
	}

	/**
	 * 验证网点是否弹出提示框
	 * 
	 * @return
	 */
	public String checkSitePrompt() {
		User currentUser = super.readCookieUser();
		UserCustom uc = new UserCustom();
		uc.setUserName(currentUser.getUserName());
		uc.setBindedUserName(currentUser.getUserName());
		uc.setType(UserCustom.SITE_PROMPT);
		List<UserCustom> ucs = userCustomService.searchUserCustom(uc);
		if (ucs != null && ucs.size() > 0) {
			putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "不需要弹出提示！", "");
		} else {
			Object obj = super.readCookie("sitePopup");
			if (("0").equals(obj))
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "需要弹出提示！", "");
			else
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "不需要弹出提示！", "");
		}
		super.writeCookie("sitePopup", "1");
		return "jsonRes";
	}

	/**
	 * 网点下次登录不再弹出提示写入数据
	 * 
	 * @return
	 */
	public String rememberSitePrompt() {
		User currentUser = super.readCookieUser();
		UserCustom newUC = new UserCustom();
		newUC.setUserName(currentUser.getUserName());
		newUC.setBindedUserName(currentUser.getUserName());
		newUC.setCustomerId(null);
		newUC.setType(UserCustom.SITE_PROMPT);
		newUC.setRelationalQuery("3");
		boolean resFlag = userCustomService.add(newUC);
		if (resFlag) {
			putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "下次登录不再弹出提示！", "");
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "数据插入失败！", "");
		}
		return "jsonRes";
	}

	public void addCustomersList() {
		customersList = new ArrayList<UserThread>();
		yesDeliveryList = new ArrayList<UserThread>(); // 已经分配过的客户List
		noDeliveryList = new ArrayList<UserThread>(); // 未分配的客户List
		User currentUser = super.readCookieUser();

		List<UserThreadContract> usedCustomers = new ArrayList<UserThreadContract>();
		int branchId = currentUser.getId();
		if (currentUser.getParentId() != null) {
			// 承包区子账号进来只能看到被分配的直客
			usedCustomers = userThreadContractService
					.getContractersByUserNameAndType(currentUser.getUserName(),
							"2");
			if (usedCustomers != null && usedCustomers.size() != 0) {
				for (int i = 0; i < usedCustomers.size(); i++) {
					UserThread ut = userThreadService.getUserById(usedCustomers
							.get(i).getConractAreaId());

					List<UserThreadContract> utcs = userThreadContractDao
							.searchContractByConractAreaId(usedCustomers.get(i)
									.getConractAreaId(), null);
					if (utcs != null && utcs.size() >= 2) {
						ut.setContractStatus("true");
						yesDeliveryList.add(ut);
					} else {
						noDeliveryList.add(ut);
					}

				}
			}
			StringBuffer deliveryList = assemLabel(currentUser);

			JsonBean jsonBean = new JsonBean();
			jsonBean.setDelivery(deliveryList.toString());
			String json = JsonUtil.toJson(jsonBean);
			JsonUtil.response(json);

		} else {
			// 所有客户
			customersList = userThreadService.searchAllUsersBySite(currentUser
					.getSite());

			// 该用户已经承包出去的客户
			usedCustomers = userThreadContractService.searchContractsBysiteId(
					String.valueOf(currentUser.getId()), null);
		}

		for (int i = 0; i < customersList.size(); i++) {
			boolean isDeliverty = false;
			UserThread user = customersList.get(i);
			if (user.getId().equals(currentUser.getId())) {
				// 清除自己的账户为承包区客户
				customersList.remove(i);
			}

			for (int j = 0; j < usedCustomers.size(); j++) {
				UserThreadContract utc = usedCustomers.get(j);
				if (user.getId().equals(utc.getConractAreaId())) {
					customersList.get(i).setContractStatus("true"); // 已经被分配掉的客户
					// 如果该客户已经被分配给承包区了，在不能再继续被分配给其它账号
					List<UserThreadContract> utcList2 = userThreadContractService
							.searchContractByConractAreaId(user.getId(), "2");
					if (utcList2 != null && utcList2.size() == 1) {
						customersList.get(i).setIsAgain("no");
					}

					// 模板客户的分配---已经被分配过了
					posttempUserList = posttempService
							.getPosttempUserByBranchIdAndVipId(branchId,
									user.getId());
					if (posttempUserList != null && posttempUserList.size() > 0) {
						customersList.get(i).setMailNoStatus("true");
						if (isDeliverty == false) {
							isDeliverty = true;
						}
					}

					yesDeliveryList.add(customersList.get(i));
					isDeliverty = true;
					break;
				}
			}

			// 模板客户的分配---未被分配过
			posttempUserList = posttempService
					.getPosttempUserByBranchIdAndVipId(branchId, user.getId());
			if (posttempUserList != null && posttempUserList.size() > 0) {
				customersList.get(i).setMailNoStatus("true");
				if (isDeliverty == false) {
					yesDeliveryList.add(customersList.get(i));
					isDeliverty = true;
				}
			}

			if (isDeliverty == false) {
				noDeliveryList.add(customersList.get(i));
			}

		}
		StringBuffer deliveryList = assemLabel(currentUser);

		JsonBean jsonBean = new JsonBean();
		jsonBean.setDelivery(deliveryList.toString());
		String json = JsonUtil.toJson(jsonBean);
		JsonUtil.response(json);
	}

	// 组装页面标签数据
	private StringBuffer assemLabel(User currentUser) {
		// 前台ajax拼装数据转移至后台处理
		StringBuffer deliveryList = new StringBuffer();
		deliveryList
				.append("<div id='small_tab_box'><table id='smallTab'><tbody>");
		if (noDeliveryList == null || noDeliveryList.size() == 0) {
			if (yesDeliveryList == null || yesDeliveryList.size() == 0) {
				deliveryList.append("<tr><td><b>暂无客户分配！</b></td></tr>");
			}
		}
		// 未分配客户
		int index = 0;
		if (noDeliveryList != null && noDeliveryList.size() > 0) {
			deliveryList
					.append("<tr><td colspan='2' style='height:30px;'><b>还未分配的客户有：</b></td></tr>");
		}

		for (int i = 0; i < noDeliveryList.size(); i++) {
			if (StringUtils.isNotEmpty(noDeliveryList.get(i).getUserName())) {
				if (i % 2 == 0) {
					index = i / 2;
					deliveryList.append("<tr class='trNoID" + index
							+ "' style='height:30px;'>");
				}
			}
			if (StringUtils.isNotEmpty(noDeliveryList.get(i).getUserName())) {
				deliveryList
						.append("<td><span><input name='customer' class='activityChecked' id='customer"
								+ noDeliveryList.get(i).getId()
								+ "' type='checkbox' value='"
								+ noDeliveryList.get(i).getId()
								+ "' />"
								+ "</span><span class='tabtop_span'>"
								+ noDeliveryList.get(i).getUserName()
								+ "</span></td>");
			}
			if (StringUtils.isNotEmpty(noDeliveryList.get(i).getUserName())) {
				if (((i + 1) % 2) == 0) {
					index = i / 2;
					deliveryList.append("</tr>");
				}
			}
		}
		// 已分配客户
		index = 0;
		if (yesDeliveryList != null && yesDeliveryList.size() > 0) {
			String content = "";
			if (check == "1") {
				content = "已分配的客户有：";
				deliveryList
						.append("<tr style='height:30px;'><td colspan='2' style='width:400px;'><b>"
								+ content + "</b></td></tr>");
			} else {
				content = "已分配的客户有：";
				deliveryList
						.append("<tr style='height:20px;'><td colspan='2' style='width:400px;'><b>"
								+ content + "</b></td></tr>");
				content = "<font color='#436EEE'>注：未分配给承包区的客户可继续分配（灰色为不可分配）</font>";
				deliveryList
						.append("<tr style='height:30px;'><td colspan='2' style='width:400px;'><b>"
								+ content + "</b></td></tr>");
			}

		}

		for (int i = 0; i < yesDeliveryList.size(); i++) {
			if (StringUtils.isNotEmpty(yesDeliveryList.get(i).getUserName())) {
				if (i % 2 == 0) {
					index = i / 2;
					deliveryList.append("<tr class='trYesID" + index
							+ "' style='height:30px;'>");
				}
			}
			if (StringUtils.isNotEmpty(yesDeliveryList.get(i).getUserName())) {
				if (yesDeliveryList.get(i).getContractStatus() == "true") {
					if (yesDeliveryList.get(i).getEditUserName() == editUserName) {
						deliveryList
								.append("<td><span><input name='customer' class='canChecked' checked=true id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					} else if (currentUser.getUserType() == "2"
							|| yesDeliveryList.get(i).getIsAgain() == "no") {
						deliveryList
								.append("<td><span><input name='customer' disabled=true checked=true id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					} else if (yesDeliveryList.get(i).getMailNoStatus() == "true") {
						deliveryList
								.append("<td><span><input name='customer' class='isContractBox"
										+ yesDeliveryList.get(i).getUserName()
										+ "' id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					} else {
						deliveryList
								.append("<td><span><input name='customer' val='yesDelivery"
										+ yesDeliveryList.get(i).getUserName()
										+ "' class='activityChecked' id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					}
				}

				else if (yesDeliveryList.get(i).getMailNoStatus() == "true") {
					if (check == "1") {
						deliveryList
								.append("<td><span><input name='customer' class='isContractBox"
										+ yesDeliveryList.get(i).getUserName()
										+ "' id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					} else {
						deliveryList
								.append("<td><span><input name='customer' disabled=true checked=true id='customer"
										+ yesDeliveryList.get(i).getId()
										+ "' type='checkbox' value='"
										+ yesDeliveryList.get(i).getId()
										+ "' /></span><span class='tabtop_span'>"
										+ yesDeliveryList.get(i).getUserName()
										+ "</span></td>");
					}
				} else {
					deliveryList
							.append("<td><span><input name='customer' val='yesDelivery"
									+ yesDeliveryList.get(i).getId()
									+ "' class='activityChecked' id='customer"
									+ yesDeliveryList.get(i).getId()
									+ "' type='checkbox' value='"
									+ yesDeliveryList.get(i).getId()
									+ "' /></span><span class='tabtop_span'>"
									+ yesDeliveryList.get(i).getUserName()
									+ "</span></td>");
				}
				if (StringUtils
						.isNotEmpty(yesDeliveryList.get(i).getUserName())) {
					if (((i + 1) % 2) == 0) {
						index = i / 2;
						deliveryList.append("</tr>");
					}
				}
			}
		}
		deliveryList.append("</tbody></table></div>");
		return deliveryList;
	}

	public String editCustomersList() {
		customersList = new ArrayList<UserThread>();
		yesDeliveryList = new ArrayList<UserThread>();
		noDeliveryList = new ArrayList<UserThread>();
		// 拼装的页面标签
		User currentUser = super.readCookieUser();

		// 该用户已经承包出去的客户
		List<UserThreadContract> usedCustomers = new ArrayList<UserThreadContract>();
		int branchId = currentUser.getId();
		if (currentUser.getParentId() != null) {
			// 承包区子账号进来只能看到被分配的直客
			usedCustomers = userThreadContractService
					.getContractersByUserNameAndType(currentUser.getUserName(),
							"2");
			if (usedCustomers != null && usedCustomers.size() != 0) {
				for (int i = 0; i < usedCustomers.size(); i++) {
					UserThreadContract utc = usedCustomers.get(i);
					UserThread ut = userThreadService.getUserById(utc
							.getConractAreaId());

					List<UserThreadContract> utcs = userThreadContractDao
							.searchContractByConractAreaId(
									utc.getConractAreaId(), null);
					if (utcs != null && utcs.size() >= 2) {
						ut.setContractStatus("true"); // 已经分配的客户

						// 查询承包区表里是否存在当前用户的的当前客户，如果是用来显示为勾选且活性
						List<UserThreadContract> utcList = userThreadContractService
								.searchContractByConractAreaIdAndUserName(
										utc.getConractAreaId(), editUserName);
						if (utcList != null && utcList.size() == 1) {
							ut.setEditUserName(editUserName);
						}
						yesDeliveryList.add(ut);
					} else {
						noDeliveryList.add(ut);
					}

				}
			}
			StringBuffer deliveryList = assemLabel(currentUser);

			JsonBean jsonBean = new JsonBean();
			jsonBean.setDelivery(deliveryList.toString());
			String json = JsonUtil.toJson(jsonBean);
			JsonUtil.response(json);
			return "toEditSubAccountJSON";
		} else {
			// 所有客户
			customersList = userThreadService.searchAllUsersBySite(currentUser
					.getSite());

			// 该用户已经承包出去的客户
			usedCustomers = userThreadContractService.searchContractsBysiteId(
					String.valueOf(currentUser.getId()), null);
		}

		for (int i = 0; customersList != null && i < customersList.size(); i++) {
			boolean isDeliverty = false;
			UserThread user = customersList.get(i);
			if (user.getId().equals(currentUser.getId())) {
				// 清除自己的账户为承包区客户
				customersList.remove(i);
			}

			for (int j = 0; j < usedCustomers.size(); j++) {
				UserThreadContract utc = usedCustomers.get(j);
				if (user.getId().equals(utc.getConractAreaId())) {
					customersList.get(i).setContractStatus("true"); // 已经分配的客户
					// 查询承包区表里是否存在当前用户的的当前客户，如果是用来显示为勾选且活性
					List<UserThreadContract> utcList = userThreadContractService
							.searchContractByConractAreaIdAndUserName(
									user.getId(), editUserName);
					if (utcList != null && utcList.size() == 1) {
						customersList.get(i).setEditUserName(editUserName); // 设置为该账号下的客户，状态为活性
						yesDeliveryList.add(customersList.get(i));
						isDeliverty = true;
						break;
					}
					// 如果该客户已经被分配给承包区了，在不能再继续被分配给其它账号
					List<UserThreadContract> utcList2 = userThreadContractService
							.searchContractByConractAreaId(user.getId(), "2");
					if (utcList2 != null && utcList2.size() == 1) {
						customersList.get(i).setIsAgain("no");
					}

					// 模板客户的分配---已经被分配过了
					posttempUserList = posttempService
							.getPosttempUserByBranchIdAndVipId(branchId,
									user.getId());
					if (posttempUserList != null && posttempUserList.size() > 0) {
						customersList.get(i).setMailNoStatus("true");
						if (isDeliverty == false) {
							isDeliverty = true;
						}
					}
					yesDeliveryList.add(customersList.get(i));
					isDeliverty = true;
					break;
				}
			}

			// 模板客户的分配---未被分配过
			posttempUserList = posttempService
					.getPosttempUserByBranchIdAndVipId(branchId, user.getId());
			if (posttempUserList != null && posttempUserList.size() > 0) {
				customersList.get(i).setMailNoStatus("true");
				if (isDeliverty == false) {
					yesDeliveryList.add(customersList.get(i));
					isDeliverty = true;
				}
			}

			if (isDeliverty == false) {
				noDeliveryList.add(customersList.get(i));
			}
		}
		StringBuffer deliveryList = assemLabel(currentUser);
		JsonBean jsonBean = new JsonBean();
		jsonBean.setDelivery(deliveryList.toString());
		String json = JsonUtil.toJson(jsonBean);
		JsonUtil.response(json);
		return "toEditSubAccountJSON";
	}

	/**
	 * 是否弹出层 guoliang.wang
	 * 
	 * @return
	 */
	public String printNav() {
		// =================
		showLayer = "0";
		User currentUser = super.readCookieUser();
		UserCustom userCustom = new UserCustom();
		userCustom.setType("8");
		userCustom.setUserName(currentUser.getUserName());

		// 网点或者承包区
		if (StringUtils.equals("2", currentUser.getUserType())
				|| StringUtils.equals("21", currentUser.getUserType())
				|| StringUtils.equals("22", currentUser.getUserType())
				|| StringUtils.equals("23", currentUser.getUserType())) {

			List<UserCustom> userCustomList = userCustomService
					.searchUserCustom(userCustom);
			if (CollectionUtils.isEmpty(userCustomList)) {
				showLayer = "1";
			}
		}

		// 卖家和业务账号
		if (StringUtils.equals("1", currentUser.getUserType())
				|| StringUtils.equals("11", currentUser.getUserType())
				|| StringUtils.equals("12", currentUser.getUserType())
				|| StringUtils.equals("13", currentUser.getUserType())
				|| StringUtils.equals("41", currentUser.getUserType())) {
			List<UserCustom> userCustomList = userCustomService
					.searchUserCustom(userCustom);
			if (CollectionUtils.isNotEmpty(userCustomList)) {
				showLayer = "1";
			}
		}

		return "printNav";
	}

	/**
	 * 关闭弹出层 guoliang.wang
	 * 
	 * @return
	 */
	public String printClose() {
		// =================
		User currentUser = super.readCookieUser();
		UserCustom userCustom = new UserCustom();
		userCustom.setType("8");
		userCustom.setUserName(currentUser.getUserName());
		userCustom.setCustomerId(currentUser.getClientId());
		// 网点或者承包区
		if (StringUtils.equals("2", currentUser.getUserType())
				|| StringUtils.equals("21", currentUser.getUserType())
				|| StringUtils.equals("22", currentUser.getUserType())
				|| StringUtils.equals("23", currentUser.getUserType())) {
			if (StringUtils.equals("1", currentUser.getUserState())) {
				userCustomService.add(userCustom);
			}
		}

		// 卖家和业务账号
		if (StringUtils.equals("1", currentUser.getUserType())
				|| StringUtils.equals("11", currentUser.getUserType())
				|| StringUtils.equals("12", currentUser.getUserType())
				|| StringUtils.equals("13", currentUser.getUserType())
				|| StringUtils.equals("41", currentUser.getUserType())) {
			userCustomService.remove(userCustom);
		}
		return "printClose";
	}

	/**
	 * 我要发货 点击 修改手机号码 [暂无用] 2012--8-1
	 * 
	 * @return
	 */
	public String eidtMobile() {
		User currentUser = super.readCookieUser();
		logger.info("id=" + currentUser.getId() + "=userName="
				+ currentUser.getUserName());
		User user = userService.getUserById(currentUser.getId());
		user.setMobilePhone(mobile);

		boolean bool = userService.edit(user);
		logger.info("修改手机号码成功与否：" + bool);

		return null;
	}

	public String eidtUserName() {
		UserThread ut = new UserThread();
		ut.setUserCode(userCode);

		List<UserThread> userThreadList = userThreadService
				.searchUsersByCode(ut);
		UserThread userThread = null;
		if (userThreadList.size() != 0) {
			userThread = userThreadList.get(0);
			userThread.setUserName(userName);
		}

		boolean bool = userThreadService.editUser(userThread);
		logger.info("修改客户名称成功与否：" + bool);
		return null;
	}

	// === getter && setter ===
	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public UserCustom getUserCustom() {
		return userCustom;
	}

	public void setUserCustom(UserCustom userCustom) {
		this.userCustom = userCustom;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public List<User> getAssociationAccountList() {
		return associationAccountList;
	}

	public void setAssociationAccountList(List<User> associationAccountList) {
		this.associationAccountList = associationAccountList;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<User> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<User> pagination) {
		this.pagination = pagination;
	}

	public String getAjaxAlertText() {
		return ajaxAlertText;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserBean> getUserBeanList() {
		return userBeanList;
	}

	public void setUserBeanList(List<UserBean> userBeanList) {
		this.userBeanList = userBeanList;
	}

	public int getEccount() {
		// Integer类型的eccount可能为空. 为空的时候转json会报错
		return eccount == null ? -1 : eccount;
	}

	public void setEccount(int eccount) {
		this.eccount = eccount;
	}

	public Integer getUserThreadId() {
		return userThreadId;
	}

	public void setUserThreadId(Integer userThreadId) {
		this.userThreadId = userThreadId;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public List<UserThread> getUserThreadList() {
		return userThreadList;
	}

	public String getNewUserCode() {
		return newUserCode;
	}

	public void setNewUserCode(String newUserCode) {
		this.newUserCode = newUserCode;
	}

	public String getNewAdress() {
		return newAdress;
	}

	public void setNewAdress(String newAdress) {
		this.newAdress = newAdress;
	}

	public List<User> getBindUserList() {
		return bindUserList;
	}

	public void setBindUserList(List<User> bindUserList) {
		this.bindUserList = bindUserList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getAddUserName() {
		return addUserName;
	}

	public void setAddUserName(String addUserName) {
		this.addUserName = addUserName;
	}

	public String getConractAreaId() {
		return conractAreaId;
	}

	public void setConractAreaId(String conractAreaId) {
		this.conractAreaId = conractAreaId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getShowLayer() {
		return showLayer;
	}

	public void setShowLayer(String showLayer) {
		this.showLayer = showLayer;
	}

	public List<UserThread> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<UserThread> customersList) {
		this.customersList = customersList;
	}

	public List<PosttempUser> getPosttempUserList() {
		return posttempUserList;
	}

	public void setPosttempUserList(List<PosttempUser> posttempUserList) {
		this.posttempUserList = posttempUserList;
	}

	public String getEditUserName() {
		return editUserName;
	}

	public void setEditUserName(String editUserName) {
		this.editUserName = editUserName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<UserThread> getNoDeliveryList() {
		return noDeliveryList;
	}

	public void setNoDeliveryList(List<UserThread> noDeliveryList) {
		this.noDeliveryList = noDeliveryList;
	}

	public List<UserThread> getYesDeliveryList() {
		return yesDeliveryList;
	}

	public void setYesDeliveryList(List<UserThread> yesDeliveryList) {
		this.yesDeliveryList = yesDeliveryList;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getMailNo() {
		return mailNo;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	class JsonBean {
		private String delivery;

		public String getDelivery() {
			return delivery;
		}

		public void setDelivery(String delivery) {
			this.delivery = delivery;
		}

	}
}
