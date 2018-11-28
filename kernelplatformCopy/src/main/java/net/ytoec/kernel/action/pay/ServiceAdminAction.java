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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.StringUtil;

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class ServiceAdminAction extends AbstractActionSupport {

	/**
	 * 管理员，对用户（卖家、平台用户、业务帐号、承包去区、网点）的开通进行管理
	 * 
	 * @Author haolong
	 * @2012-7-4 操作做数据表：ec_core_dredgeService
	 * */

	@Inject
	private DredgeServiceService dredgeServiceService;
	@Inject
	private UserService userService;
	@Inject
	private PayServiceService payServiceService;
	// 开通服务表 dto
	private DredgeService dredgeService;
	// 服务表dto
	private PayService payService;
	// 开通的服务
	private Integer[] serviceIds;
	// 开通的时间区间
	private String startTime;
	private String endTime;
	// 用户的类型
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
	// 用户名,电话,店铺名字
	private String formName;

	private List<DredgeService> dredgeServiceList;

	private List<PayService> payServiceList;

	private Integer currentPage = 1;
	private Integer pageNum = 20;
	// 分页对象
	private Pagination pagination;

	// 备注
	private String remark;
	// 更新用户的备注，需要传过来用户的开通服务id
	private Integer dredgeServiceId;
	// 用户表 id
	private Integer userId;
	
	private Integer def;//首页登录默认复选框全选

	private String response;
	private String  menuFlag;
	
	/**
	 * 点击左侧菜单栏，金额管理，进入金额管理首页
	 * 
	 * */
	public String index() {

		pagination = new Pagination(currentPage, pageNum);

		pagination.setTotalRecords(0);
		//首页登录默认复选框全选		
		def = 1;
		/**
		 * 默认读取三个月的数据
		 * */
		startTime = dateArithmetic(new Date(), 3);
		endTime = dateArithmetic(new Date(), 0);

		// 页面上读取服务表中的所有的已经开通的服务(免费服务除外)
		payServiceList = payServiceService.getAllOpenServiceList();

		return "index";
	}

	/**
	 * 开通服务查询
	 * */
	public String list() {
		
		def = 99;

		// 页面上读取服务表中的所有的已经开通的服务(免费服务除外)
		payServiceList = payServiceService.getAllOpenServiceList();
		// 用户id 列表
		List<Integer> userIdList = new ArrayList<Integer>();
		// 服务id 列表
		List<Integer> serviceIdlist = new ArrayList<Integer>();
		
		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			params.put("startTime", DateUtil.valueof(this.startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			params.put("endTime", DateUtil.valueof(this.endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}
		
		// 服务id 列表
		if (ArrayUtils.isNotEmpty(serviceIds)) 
		{
			for (Integer serviceId : serviceIds) 
				serviceIdlist.add(serviceId);
			
			params.put("serviceIdArray", serviceIdlist);
		}
		
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
					userIdList.add(user.getId());									
				}
			}
		}
		
		// 共多少条记录，分页使用
		Integer total = 0;
		if (pagination == null) 
			pagination = new Pagination(currentPage, pageNum);
		
		if(userIdList.size()<=0 || serviceIds==null )
		{
			pagination.setTotalRecords(0);
			dredgeServiceList = new ArrayList();			
			return "list";
		}
		else
		{
			params.put("userIdArray", userIdList);
			dredgeServiceList = dredgeServiceService.getDredgeserviceList(params,pagination, true);

			if (!CollectionUtils.isEmpty(dredgeServiceList)) {
				
				Integer serviceId = -1;
				Integer userid = -1;
				for (DredgeService dredgeService : dredgeServiceList) {
					// 得到用户ID
					userid = dredgeService.getUserId();
					serviceId = dredgeService.getServiceId();
					// 根据serviceId查询出服务名称
					payService = new PayService();
					payService.setId(serviceId);
					payService = (PayService) payServiceService.get(payService);
					User user = userService.getUserById(userid);
					if (user != null) {
						//dredgeService.setUserType(user.getUserType());
						String userType= user.getUserType();
						if(userType.equals("2")){
							if(user.getParentId()!=null&&user.getParentId().intValue()>0)
								dredgeService.setUserType("2;承包区");//承包区
							else
								dredgeService.setUserType("2;网点");//网点
						}
						
						if(userType.equals("1")) {
							if(user.getChildType()!=null&&user.getChildType().length()>0)
								dredgeService.setUserType("1;业务账号");//业务账号
							else
								dredgeService.setUserType("1;卖家");//卖家
						}
						
						if(!userType.equals("1")&& !userType.equals("2")){
							dredgeService.setUserType(user.getUserType());
						}
						
						dredgeService.setUserName(user.getUserName());
						dredgeService.setServiceName(payService.getName());
					}
				}
			}
			total = dredgeServiceService.getDredgeserviceListCount(params);
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
		
		if (StringUtils.isNotEmpty(remark) && StringUtils.isNotBlank(dredgeServiceId.toString())) 
		{
			// 根据页面传过来的dredgeServiceId更新，更新用户备注
			DredgeService dredgeService = new DredgeService();
			dredgeService.setId(dredgeServiceId);
			dredgeService = (DredgeService) dredgeServiceService.get(dredgeService);
			dredgeService.setRemark(remark);
			if(dredgeServiceService.edit(dredgeService))
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
	
	public DredgeService getDredgeService() {
		return dredgeService;
	}

	public void setDredgeService(DredgeService dredgeService) {
		this.dredgeService = dredgeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Integer[] getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(Integer[] serviceIds) {
		this.serviceIds = serviceIds;
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
	
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public List<DredgeService> getDredgeServiceList() {
		return dredgeServiceList;
	}

	public void setDredgeServiceList(List<DredgeService> dredgeServiceList) {
		this.dredgeServiceList = dredgeServiceList;
	}

	public DredgeServiceService getDredgeServiceService() {
		return dredgeServiceService;
	}

	public void setDredgeServiceService(
			DredgeServiceService dredgeServiceService) {
		this.dredgeServiceService = dredgeServiceService;
	}

	public PayServiceService getPayServiceService() {
		return payServiceService;
	}

	public void setPayServiceService(PayServiceService payServiceService) {
		this.payServiceService = payServiceService;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDredgeServiceId() {
		return dredgeServiceId;
	}

	public void setDredgeServiceId(Integer dredgeServiceId) {
		this.dredgeServiceId = dredgeServiceId;
	}

	public PayService getPayService() {
		return payService;
	}

	public void setPayService(PayService payService) {
		this.payService = payService;
	}

	public List<PayService> getPayServiceList() {
		return payServiceList;
	}

	public void setPayServiceList(List<PayService> payServiceList) {
		this.payServiceList = payServiceList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public Integer getDef() {
		return def;
	}

	public void setDef(Integer def) {
		this.def = def;
	}
	
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
}
