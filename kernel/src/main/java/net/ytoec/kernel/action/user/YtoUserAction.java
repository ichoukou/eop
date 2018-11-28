package net.ytoec.kernel.action.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.AppProviderService;
import net.ytoec.kernel.service.YtoUserService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author
 * @date 2012-5-3
 */
@Controller
@Scope("prototype")
public class YtoUserAction extends AbstractActionSupport {

	private Logger logger = Logger.getLogger(YtoUserAction.class);

	// === fields ===
	private static final long serialVersionUID = 1754742686308746474L;

	@Inject
	private YtoUserService<User> userService;
	@Inject
	private AppProviderService<AppProvider> providerService;
	private String userIdStr;
	private User user;
	private List<User> userList;
	private List<UserThread> userThreadList;

	private String ajaxAlertText;

	/**
	 * 验证是否通过. 通过json格式返回到前台<br>
	 * 默认为false；
	 */
	private String isValidate;
	private String newPwd;
	private String oldPwd;
	private String userName;

	private Integer currentPage = 1;
	private Pagination<User> pagination;
	private String startTime;
	private String endTime;

	public String deleteUser = "";// 删除指定用户
	public String initBranch = "";// 初始化网点

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public String getInitBranch() {
		return initBranch;
	}

	public void setInitBranch(String initBranch) {
		this.initBranch = initBranch;
	}

	/**
	 * 检查用户名的唯一性
	 * 
	 */
	public String checkName() throws Exception {

		// 验证通过true，否则false
		isValidate = userService.checkName(user) ? "true" : "false";
		return "jsonNameCheck";
	}

	/**
	 * 更改用户状态
	 * 
	 * @return
	 */
	public String updateUserState() {
		if (user != null && StringUtils.isNotBlank(userIdStr)
				&& StringUtils.isNotBlank(user.getUserState())) {
			String state = user.getUserState();
			boolean result = false;

			user = userService.getUserById(Integer.parseInt(userIdStr));
			user.setUserState(state);
			result = userService.edit(user);

			if (result) {
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true,
						"用户" + ("0".equals(state) ? "禁用" : "激活") + "操作已成功！",
						"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false,
						"用户" + ("0".equals(state) ? "禁用" : "激活") + "操作失败！",
						"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			}
			// user = null;
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "数据丢失，请重新操作！",
					"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
		}
		return "jsonRes";
	}

	/**
	 * 初始化密码为123456
	 * 
	 * @return
	 */
	public String updatePasswordByUserId() {
		if (StringUtils.isNotBlank(userIdStr)) {
			boolean result = false;

			User currUser = userService
					.getUserById(Integer.parseInt(userIdStr));
			try {
				currUser.setUserPassword(Md5Encryption.MD5Encode("123456"));
			} catch (Exception e) {
				logger.debug("密码加密时出错。");
				e.printStackTrace();
			}
			result = userService.edit(currUser);

			if (result) {
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true,
						"重置密码操作已成功，密码为123456！",
						"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "重置密码操作失败，请重新操作！",
						"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			}
			// user = null;
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR, false, "数据丢失，请重新操作！",
					"ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1");
		}
		return "jsonRes";
	}

	/**
	 * 根据卖家帐号删除卖家信息
	 * 
	 * @param userName
	 * @return
	 */
	public String deleteUser() { // String userName
		String userName = request.getParameter("userName");// "xindan123";

		// 查询配置文件，检查目标帐号是否为可删除帐号
		String userNames = ConfigUtilSingle.getInstance().getSELLER_USERNAME();
		if (StringUtils.isEmpty(userName)) {
			deleteUser = "用户名不能为空";
			return "init";
		}
		if (StringUtils.isEmpty(userNames)) {
			deleteUser = "请在配置文件中配置信息";
			return "init";
		}
		String[] userList = userNames.split(";");
		boolean flag = false;
		for (int i = 0; i < userList.length; i++) {
			flag = StringUtils.equals(userName, userList[i]);
			if (flag) {
				break;
			}
		}
		if (flag) {
			userService.delUserByUserName(userName);
			deleteUser = "成功";
		} else {
			deleteUser = "不能删除该用户";
		}

		return "init";
	}

	/**
	 * 初始化激活网点
	 * 
	 * @return
	 */
	public String initBranch() {

		String branch = request.getParameter("branchName");// "755068";
		// 查询配置文件，检查目标帐号是否为可删除帐号
		String branchs = ConfigUtilSingle.getInstance().getBRANCH_USERNAME();
		if (StringUtils.isEmpty(branch)) {
			initBranch = "网点名不能为空";
			return "init";
		}
		if (StringUtils.isEmpty(branchs)) {
			initBranch = "请在配置文件中配置信息";
			return "init";
		}
		String[] branchList = branchs.split(";");
		boolean flag = false;
		for (int i = 0; i < branchList.length; i++) {
			flag = StringUtils.equals(branch, branchList[i]);
			if (flag) {
				break;
			}
		}
		if (flag) {
			userService.initBranch(branch);
			initBranch = "成功";
		} else {
			initBranch = "不能初始化该网点";
		}

		return "init";
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
	}

	/**
	 * 跳转到帮助的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toHelp() throws Exception {
		return "toHelp";
	}

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

		if (!userService.addUser(user)) {
			this.addActionMessage("添加数据失败!");
			return ERROR;
		}
		/*
		 * 新增信息的时候user对象不为空. 然后调用list方法会根据user对象里的属性进行条件查询，所以把user置空。
		 */
		user = null;
		return list();
	}

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public String list() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			params.put("startTime", startTime);
			params.put("endTime", endTime);
		}
		if (StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			params.put("startTime", startTime);
			params.put("endTime", endTime);
		}
		if (StringUtils.isNotBlank(user.getUserName())) {
			params.put("userName", user.getUserName());
		}
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		Integer total = userService.getAllUserMap(params, pagination, false)
				.size();
		userList = userService.getAllUserMap(params, pagination, true);
		pagination.setTotalRecords(total);

		return "toList";
	}

	/**
	 * 修改密码<br>
	 * 
	 * @return
	 */
	public String edit_pwd() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");

		user = new User();
		user.setUserPassword(oldPwd);
		user.setUserName(userName);

		boolean isSuccess = false;
		try {
			isSuccess = userService.edit_pwd(user, newPwd);
			isValidate = isSuccess ? "true" : "false";
		} catch (Exception e) {
			isValidate = e.getMessage();
		}
		return "jsonEditPwd";
	}

	/**
	 * 验证密码
	 * 
	 * @return
	 */
	public String checkPassword() {
		user = new User();
		user.setUserName(userName);
		user.setUserPassword(oldPwd);
		boolean isSuccess = false;
		isSuccess = userService.checkPassword(user);
		isValidate = isSuccess ? "true" : "false";
		return "checkPassword";
	}

	/**
	 * 跳转到用户编辑的页面
	 * 
	 * @return
	 */
	public String toEdit() throws Exception {
		user = userService.getUserById(Integer.parseInt(userIdStr));
		return "view";
	}

	/**
	 * 保存编辑用户信息
	 * 
	 * @return
	 */
	public String edit() throws Exception {
		if (userService.editUser(user)) {
			userIdStr = user.getId() + "";
			ajaxAlertText = "修改成功!";
			return list();
		} else {
			return ERROR;
		}
	}

	/**
	 * 转到添加用户账号(卖家，网点，平台)
	 * 
	 * @return
	 */
	public String toAddYtoUserAccount() {
		user = null;
		return "toAddYtoUserAccount";
	}

	/**
	 * 添加用户账号(卖家，网点，平台)
	 * 
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	public String addYtoUserAccount() throws IOException{
		user.setUserState("0");//未激活
		user.setCreateTime(new Date());
		userService.addUser(user);
		
		//user.setUserName("");
		
 		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);  
 		response.sendRedirect("ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_ytoUser_list");
 		
		//return list();
		return null;
	}

	/**
	 * 转到更新用户账号(卖家，网点，平台)
	 * 
	 * @return
	 */
	public String toEditYtoUserAccount() {
		return "toUpdateEopUserAccount";
	}

	/**
	 * 更新用户账号(卖家，网点，平台)
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String editYtoUserAccount() throws Exception {
		
//		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);  
//		response.sendRedirect("ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_ytoUser_list");
//		
		return list();
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<UserThread> getUserThreadList() {
		return userThreadList;
	}

	public void setUserThreadList(List<UserThread> userThreadList) {
		this.userThreadList = userThreadList;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
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

	public YtoUserService<User> getUserService() {
		return userService;
	}

	public void setUserService(YtoUserService<User> userService) {
		this.userService = userService;
	}

	public String getUserIdStr() {
		return userIdStr;
	}

	public void setUserIdStr(String userIdStr) {
		this.userIdStr = userIdStr;
	}

	public String getAjaxAlertText() {
		return ajaxAlertText;
	}

	public void setAjaxAlertText(String ajaxAlertText) {
		this.ajaxAlertText = ajaxAlertText;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public AppProviderService<AppProvider> getProviderService() {
		return providerService;
	}

	public void setProviderService(
			AppProviderService<AppProvider> providerService) {
		this.providerService = providerService;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
