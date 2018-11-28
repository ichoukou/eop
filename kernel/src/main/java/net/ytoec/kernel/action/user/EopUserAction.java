package net.ytoec.kernel.action.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ytoec.uninet.util.HessianUtil;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.AppProviderService;
import net.ytoec.kernel.service.UserService;

/**
 * 
 * @author ChenRen
 * @date 2011-7-26
 */
@Controller
@Scope("prototype")
public class EopUserAction extends AbstractActionSupport {

	private Logger logger=Logger.getLogger(UserAction.class);
	// === fields ===
	private static final long serialVersionUID = 1754742686308746474L;
	// private static Integer pageNum = 10; // 每页显示的记录数 // 2011-09-06/ChenRen
	// 使用父对象的

	@Inject
	private UserService<User> userService;
	@Inject
	private AppProviderService<AppProvider> providerService;
	@Inject
    private PosttempUserDao<PosttempUser> puDao;
	private User user;
	private List<User> userList;
	private List<User> subAccountList;

	/**
	 * 验证是否通过. 通过json格式返回到前台<br>
	 * 默认为false；
	 */
	private String isValidate;
	private String newPwd;
	private String oldPwd;
	private String startTime;
	private String endTime;

	private Integer currentPage = 1;
	private Pagination<User> pagination;
	private String mediaPath;

	/**
	 * ajax验证的错误提示信息<br>
	 * 如果验证通过可以不写值
	 */
	private String ajaxAlertText;

	/** 0表示要初始化user；我的VIP 的连接中的参数. 见left.jsp */
	private String type;
	/** 当前方法的请求地址（包括参数） */
	private String url;
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
	@Deprecated
	public String checkName() throws Exception {
		// 验证通过true，否则false
		isValidate = userService.checkName(user) ? "true" : "false";
		return "jsonNameCheck";
	} // checkName

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
	 * 查询用户列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public String list() throws Exception {
		
		if(pagination == null){
			pagination = new Pagination(currentPage,pageNum);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("userSource","1");
		if(user != null){
			if(StringUtils.isNotBlank(user.getUserState())){
				params.put("userState", user.getUserState());
			}
			if(StringUtils.isNotBlank(user.getUserType())){//过滤服务商类型
				params.put("servicesType", user.getUserType());
			}
			if(StringUtils.isNotBlank(user.getUserNameText())){
				params.put("userNameText", user.getUserNameText());
			}
		}
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			params.put("startTime", startTime);
			params.put("endTime", endTime);
		}
		

		userList = userService.searchUsers(params, pagination, true);
		pagination.setTotalRecords(userList.size());
		
		List<User> tempUserList = new ArrayList<User>();
		for(User u : userList){
			AppProvider provider = providerService.getByUserId(u.getId());
			u.setAppProvider(provider);
			tempUserList.add(u);
		}
		userList.clear();
		userList = tempUserList;

		return "toList";
	} // list
	
	/**
	 * 查看会员详细信息
	 * @return
	 */
	public String view(){
		if(user != null && user.getId() != null){
			user = userService.getUserById(user.getId());
			AppProvider provider = providerService.getByUserId(user.getId());
			user.setAppProvider(provider);
			
			mediaPath = HessianUtil.getMediaPath();
		}
		return "view";
	}
	
	public String updateState() throws Exception{
		if(user != null && StringUtils.isNotBlank(user.getRemark()) && StringUtils.isNotBlank(user.getUserState())){
			String state = user.getUserState();
			boolean result = false;
			if(!user.getRemark().contains(",")){
				
				user = userService.getUserById(Integer.parseInt(user.getRemark()));
				user.setUserState(state);
				result = userService.edit(user);
			}
			else{
				for(String idStr : user.getRemark().split(",")){
					user = userService.getUserById(Integer.parseInt(idStr));
					user.setUserState(state);
					result = userService.edit(user);
					if(!result){
						break;
					}
				}
			}
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"用户"+("0".equals(state) ? "禁用" : "激活")+"操作已成功！", "user!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			}
			else{
				putMsg(JsonResponse.INFO_TYPE_ERROR,false,"用户"+("0".equals(state) ? "禁用" : "激活")+"操作失败！", "user!list.action?currentPage=1&user.userType=-1&user.userState=-1");
			}
			user = null;
		}
		else{
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"数据丢失，请重新操作！", "user!list.action?currentPage=1&user.userType=-1&user.userState=-1");
		}
		return "jsonRes";
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
	
	/**
	 * 查询网点的用户，以分配模板<br>
	 * 如果该用户分配过模板，用户将不能被选择，标识属性field003的值为false<br>
	 * 
	 * @return	user_list_modelWindow.jsp
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String listPosttempUser() throws Exception {
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
		
		User currUser = super.readCookieUser();
		// 网点用户
		if ("2".equals(currUser.getUserType())) {
			// 设置VIP用户的所属网点
			user.setSite(currUser.getSite());
			user.setField002(currUser.getId().toString());	// 设置用户的网点Id
		}
		
		pagination = new Pagination(currentPage, 20);
		
		userList = listPosttempUser(user, pagination, true);
		pagination.setTotalRecords(listPosttempUser(user, pagination, false).size());
		
		return "toListModelWindow";
	} // list

	/**
	 * 跳转到用户编辑的页面
	 * 
	 * @return
	 */
	public String toEdit() throws Exception {
		Integer id = user.getId();
		user = userService.getUserById(id);
		/**
		 * 如果是网点用户就判断该用户是否分配过模板
		 */
		User currUser = super.readCookieUser();
		if(("2").equals(currUser.getUserType())){
		    if(puDao.getPosttempUserByBranchId(id)!=null && puDao.getPosttempUserByBranchId(id).size()>0){
		        user.setField003("true");//已创建模板，不可修改发货地址
		    }
		}
		if(("1").equals(currUser.getUserType())){
			// 如果当前用户是vip用户，取当前用户的子账号
			subAccountList = userService.getUserByParentId(currUser.getId());
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
			return toEdit();
		} else {
			return ERROR;
		}
	} // edit

	/**
	 * 修改密码<br>
	 * 
	 * @return
	 */
	public String edit_pwd() throws Exception {
		// TODO 报错还是乱码
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");

		User currUser = super.readCookieUser();
//		user.setSite(currUser.getSite());
		user = new User();
		user.setUserName(currUser.getUserName());
		user.setUserPassword(oldPwd);
		
		/*
		 * @2011-09-23/ChenRen
		 * @description 
		 * 		进行了统一的异常处理后，修改密码在service里跑出的异常不能正确显示在客户端；
		 * 	这里重新处理提示形式。以保证提示信息可以正确显示。
		boolean isSuccess = userService.edit_pwd(user, newPwd);
		isValidate = isSuccess ? "true" : "false";
		 */
		boolean isSuccess = false;
		try {
			isSuccess = userService.edit_pwd(user, newPwd);
			isValidate = isSuccess ? "true" : "false";
		} catch (Exception e) {
			isValidate = e.getMessage();
		}
		
		return "jsonEditPwd";
	} // edit_pwd
	
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
	 * 用户编码验证<br>
	 * <br>
	 * 
	 * 变更历史：<br>
	 * &nbsp; 2011-08-22/ChenRen<br>
	 * &nbsp; 描述：将该方法设置成不推荐使用<br>
	 * &nbsp; 系统会对{@link UserAction}进行登录验证拦截. <br>
	 * &nbsp; 如果需要登录验证通过后才能进行"用户编码验证", 则使用该方法;<br>
	 * &nbsp; 否则参照：
	 * {@link net.ytoec.kernel.action.login.NoInterceptAction#checkUserCode}
	 * 
	 * @return
	 */
	@Deprecated
	public String checkUserCode() throws Exception {
		// 用户编码为空
		if (StringUtils.isEmpty(user.getUserCode())) {
			isValidate = "false";
			ajaxAlertText = "参数错误：用户编码为空!";
		} else {

			String msg = userService.checkUserCode(user);
			// 验证通过
			if (msg.toUpperCase().indexOf("TBA") > -1) {
				isValidate = "true";
				// 验证通过不会显示服务器信息, 所以不要定义ajaxAlertText值
			}
			// 验证失败. 返回服务器提示信息
			else {
				isValidate = "false";
				ajaxAlertText = msg;
			}
		}
		return "jsonChechUserCode";
	} // checkUserCode

	@SuppressWarnings("unchecked")
	public List<User> listPosttempUser(User entriy, Pagination<User> pagination, boolean flag){
		if(StringUtils.isEmpty(entriy.getField002() ) ) {
			// 设置网点Id是为了根据网点Id去查询已经分配过的模板的用户
			logger.error("参数异常! 没有指定用户的网点Id!");
			return Collections.EMPTY_LIST;
		}
		
		List<PosttempUser> puList = puDao.getPosttempUserByBranchId(Integer.parseInt(entriy.getField002() ) );
		List<User> uList = userService.searchUsers(entriy, pagination, flag);
		for (PosttempUser posttempUser : puList) {
			for (User u : uList) {
				if(u.getId().equals(posttempUser.getVipId() ) ) {
					// 设置用户的备用字段field003为false，标识当前用户已经分配过模板
					// 页面显示用户的时候判断该值，如果为false，不让网点选择该用户
					u.setField003("false");	
				}
			}
		} // for posttempUser
		
		return uList;
	}
	
	public String bindSubAccount(){
		boolean flag = userService.bindSubAccount(user, type);
		isValidate = flag ? "true" : "false";
		return "jsonEditPwd";
	}
	
	// === getter && setter ===
	public void setType(String type) {
		this.type = type;
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

	public List<User> getSubAccountList() {
		return subAccountList;
	}

	public void setSubAccountList(List<User> subAccountList) {
		this.subAccountList = subAccountList;
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

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

}
