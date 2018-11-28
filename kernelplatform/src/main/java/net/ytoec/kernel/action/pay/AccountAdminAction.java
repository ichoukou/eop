package net.ytoec.kernel.action.pay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 * 管理员管理,用户（卖家、平台用户、业务帐号、承包去区、网点的账户 操作数据库表：ec_core_accountUser
 * 
 * @author haolong
 * @2012-7-3
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class AccountAdminAction extends AbstractActionSupport {

	@Inject
	private AccountUserService<AccountUser> accountUserService;
	@Inject
	private UserService<User> userService;

	private AccountUser accountUser;
	private User user;
	private Integer userId;

	private String startTime;
	private String endTime;

	// 用户类型
	//卖家   value=1
	private String checkbox1;
	//平台用户  value=4
	private String checkbox2;
	//业务帐号   value=5
	private String checkbox3;
	//网点   value=2
	private String checkbox4;
	//承包区  value=2
	private String checkbox5;
	private String formName;
	// 余额段
	private String startBalance;
	private String endBalance;
	// 消费额段
	private String startConsume;
	private String endConsume;

	// 备注
	private String remark;
	// 账户表ID
	private Integer accountUserId;
	private Integer flag;
	
	private String response;
	
	public Double getSysUserBalance() {
		return sysUserBalance;
	}

	public void setSysUserBalance(Double sysUserBalance) {
		this.sysUserBalance = sysUserBalance;
	}

	public Double getSysAllConsume() {
		return sysAllConsume;
	}

	public void setSysAllConsume(Double sysAllConsume) {
		this.sysAllConsume = sysAllConsume;
	}

	// 账户表
	private List<AccountUser> accountUserList;
	// 金额管理显示列表

	private Pagination<AccountUser> pagination;
	private Integer currentPage = 1;
	private Integer pageNum = 20;

	private Double sysUserBalance;
	private Double sysAllConsume;
	
	private String  menuFlag;
	

	/**
	 * 点击左侧菜单栏，金额管理，进入金额管理首页
	 * 
	 * */
	public String index() {

		pagination = new Pagination(currentPage, pageNum);
		pagination.setTotalRecords(0);
		//首页登录默认复选框全选
		flag = 1;
		return "index";
	}

	/*
	 * 金额管理，查询账户列表
	 * 
	 * @return
	 */
	public String list() {

		List<User> userList = null;
		// 通过接口查询总余额
		sysUserBalance = 0.0;
		// 通过接口查询总消费
		sysAllConsume = 0.0;

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> list = new ArrayList<Integer>();
		
		if (StringUtils.isNotBlank(startBalance) && Double.parseDouble(startBalance)>= 0) 
			   params.put("startBalance", startBalance);
		if (StringUtils.isNotBlank(endBalance)&& Double.parseDouble(endBalance)>= 0) 
			   params.put("endBalance", endBalance);

		if (StringUtils.isNotBlank(startConsume) && Double.parseDouble(startConsume)>= 0) 
			   params.put("startConsume", startConsume);			
		if (StringUtils.isNotBlank(endConsume)&&Double.parseDouble(endConsume)>= 0) 
			   params.put("endConsume", endConsume);
		
		
		//用户类型查询
		List<String> userTypes=new ArrayList<String>();
		if(StringUtils.isNotBlank(checkbox1))
			userTypes.add(checkbox1);
		if(StringUtils.isNotBlank(checkbox2))
			userTypes.add(checkbox2);
		if(StringUtils.isNotBlank(checkbox3))
			userTypes.add(checkbox3);
		if(StringUtils.isNotBlank(checkbox4))
			userTypes.add(checkbox4);
		if(StringUtils.isNotBlank(checkbox5))
			userTypes.add(checkbox5);
		//用户名/电话/店铺名字/网点名称
        if (StringUtils.isNotBlank(formName)){			
        	userTypes.add("formName:"+formName);
		}
		
		if(userTypes.size()>0)
		{
			List<User> users = userService.getUserListByUserTypes(userTypes);
			if(users!=null && users.size()>0)
			{
				for (User user : users) 
				{				
				    list.add(user.getId());									
				}
			}
		}
		
		//共多少条记录数
		Integer total = 0;
		
		if (pagination == null) 
			pagination = new Pagination(currentPage, pageNum);
		
		if(list.size()<=0)
		{			
			pagination.setTotalRecords(0);
			accountUserList=new ArrayList();			
			return "list";
		}
		else
		{
			params.put("userIdArray", list);
			
			// 通过接口查询总消费，总余额
			sysUserBalance = accountUserService.getSumUseBalance(params);
			// 通过接口查询总消费，总消费
			sysAllConsume = accountUserService.getSumAllConsume(params);
			
			if(sysUserBalance==null)
				sysUserBalance=0.0;
			
			if(sysAllConsume==null)
				sysAllConsume=0.0;
			
			accountUserList = accountUserService.getAccountUserList(params, pagination, true);
			if (!CollectionUtils.isEmpty(accountUserList)) 
			{				
				for (AccountUser accountUser : accountUserList) 
				{
					// 得到用户id
					int userId = accountUser.getUserId();
					// 通过id得到用户的信息主帐号
					User user = userService.getUserById(userId);
					if(user!=null)
					{
						//accountUser.setUserType(user.getUserType());
						String userType= user.getUserType();
						if(userType.equals("2")){
							if(user.getParentId()!=null&&user.getParentId().intValue()>0)
								accountUser.setUserType("2;承包区");//承包区
							else
								accountUser.setUserType("2;网点");//网点
						}
						
						if(userType.equals("1")) {
							if(user.getChildType()!=null&&user.getChildType().length()>0)
								accountUser.setUserType("1;业务账号");//业务账号
							else
								accountUser.setUserType("1;卖家");//卖家
						}
						
						if(!userType.equals("1")&& !userType.equals("2")){
							accountUser.setUserType(user.getUserType());
						}
						accountUser.setUserName(user.getUserName());
					}
				}
			}
			total = accountUserService.getAccountUserListCount(params);
			pagination.setTotalRecords(total);
			return "list";
		}
	}

	/**
	 * 更新备注
	 * 
	 * */
	public String editRemark() {

		response = "修改失败";		
		if (StringUtils.isNotEmpty(remark) && StringUtils.isNotBlank(accountUserId.toString())) 
		{
			// 根据页面传过来的accountUserId更新，更新用户
			AccountUser accountUser = new AccountUser();
			accountUser.setId(accountUserId);		
			accountUser = accountUserService.get(accountUser);
			accountUser.setRemark(remark);
			if(accountUserService.edit(accountUser))
				response = "修改成功";
		}
		return "remark";
	}
	

	/**
	 * 日期处理的方法
	 * */
	private static String dateArithmetic(Date originalDate, int countMonth) {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加-countDay天数
		cal.add(Calendar.MONTH, -countMonth);
		return sdm.format(cal.getTime());
	}

	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public Pagination<AccountUser> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<AccountUser> pagination) {
		this.pagination = pagination;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public AccountUserService<AccountUser> getAccountUserService() {
		return accountUserService;
	}

	public void setAccountUserService(
			AccountUserService<AccountUser> accountUserService) {
		this.accountUserService = accountUserService;
	}

	public AccountUser getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(AccountUser accountUser) {
		this.accountUser = accountUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(String startBalance) {
		this.startBalance = startBalance;
	}

	public String getEndBalance() {
		return endBalance;
	}

	public void setEndBalance(String endBalance) {
		this.endBalance = endBalance;
	}

	public String getStartConsume() {
		return startConsume;
	}

	public void setStartConsume(String startConsume) {
		this.startConsume = startConsume;
	}

	public String getEndConsume() {
		return endConsume;
	}

	public void setEndConsume(String endConsume) {
		this.endConsume = endConsume;
	}

	public List<AccountUser> getAccountUserList() {
		return accountUserList;
	}

	public void setAccountUserList(List<AccountUser> accountUserList) {
		this.accountUserList = accountUserList;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getAccountUserId() {
		return accountUserId;
	}

	public void setAccountUserId(Integer accountUserId) {
		this.accountUserId = accountUserId;
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

	public String getCheckbox1() {
		return checkbox1;
	}

	public void setCheckbox1(String checkbox1) {
		this.checkbox1 = checkbox1;
	}

	public String getCheckbox2() {
		return checkbox2;
	}

	public void setCheckbox2(String checkbox2) {
		this.checkbox2 = checkbox2;
	}

	public String getCheckbox3() {
		return checkbox3;
	}

	public void setCheckbox3(String checkbox3) {
		this.checkbox3 = checkbox3;
	}

	public String getCheckbox4() {
		return checkbox4;
	}

	public void setCheckbox4(String checkbox4) {
		this.checkbox4 = checkbox4;
	}

	public String getCheckbox5() {
		return checkbox5;
	}

	public void setCheckbox5(String checkbox5) {
		this.checkbox5 = checkbox5;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
}
