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

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.opensymphony.xwork2.util.ArrayUtils;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.UserService;

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PaymentAdminAction extends AbstractActionSupport {

	/**
	 * 管理员，对用户（卖家、平台用户、业务帐号、承包去区、网点）的收支记录进行管理
	 * 
	 * @Author haolong
	 * @2012-7-4 操作做
	 * 数据表：ec_core_payment
	 * */
	// 交易时间区间
	private String startTime;
	private String endTime;
	// 交易额度区间
	private String startMoney;
	private String endMoney;
	// 交易类型
	private String dealType;
	// 交易状态
	private String dealStatus;
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
	
	private List<Payment> paymentList;
	@Inject
	private PaymentService paymentService;
	@Inject
	private UserService userService;

	// 分页对象
	private Pagination pagination;
	private Integer currentPage = 1;
	private Integer pageNum = 20;
	private Integer flag;
	private String formName;
	
	private String userId;
	
	private String  menuFlag;
	

	/**
	 * 点击左侧菜单栏，收支明细，进入收支明细首页
	 * 
	 * */
	public String index() {

		pagination = new Pagination(currentPage, pageNum);

		pagination.setTotalRecords(0);
		flag = 1;

		/**
		 * 默认读取三个月的数据
		 * */
		startTime = dateArithmetic(new Date(), 3);
		endTime = dateArithmetic(new Date(), 0);

		return "index";
	}

	/**
	 * 查询用户收支记录
	 * 
	 * @return
	 * */
	public String list() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			params.put("startTime",DateUtil.valueof(this.startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
			params.put("endTime",DateUtil.valueof(this.endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
		}
		else
		{
			startTime = dateArithmetic(new Date(), 3);
			endTime = dateArithmetic(new Date(), 0);
			params.put("startTime",DateUtil.valueof(this.startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
			params.put("endTime",DateUtil.valueof(this.endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
		}
		
		if (StringUtils.isNotBlank(startMoney) && Double.parseDouble(startMoney)>= 0) 
			 params.put("startMoney", startMoney);
		if (StringUtils.isNotBlank(endMoney) && Double.parseDouble(endMoney)>= 0) 	
			 params.put("endMoney", endMoney);
		
		//交易类型
		if (StringUtils.isNotBlank(dealType)) {			
			if(StringUtils.indexOf(dealType, ",") > 0){
				List<String> typeList = new ArrayList<String>();
				String[] str = StringUtils.split(dealType, ",");
				for(String sType:str){
					typeList.add(sType);
				}
				params.put("dealTypeArray", typeList);
			}
			else
			{
				params.put("dealType", dealType);
			}
		}
		//交易状态      
		if (StringUtils.isNotBlank(dealStatus)) {
			if(StringUtils.indexOf(dealStatus, ",") >0){
				List<String> statusList = new ArrayList<String>();
				String[] str = StringUtils.split(dealStatus, ",");
				for(String dStatus:str){
					statusList.add(dStatus);					
				}
				params.put("dealStatusArray", statusList);
			}
			else
			{
				params.put("dealStatus", dealStatus);
			}
		}
		
		//用户类型查询
		List<Integer> userIdList=new ArrayList<Integer>();
		
		List<String> userTypes=new ArrayList<String>();
		if(StringUtils.isNotBlank(checkbox1))
			userTypes.add(checkbox1);
		if(StringUtils.isNotBlank(checkbox2))
			userTypes.add(checkbox2);
		if(StringUtils.isNotBlank(checkbox3))
			userTypes.add(checkbox3);		
		if(StringUtils.isNotBlank(checkbox4))//网点
			userTypes.add(checkbox4);		
		if(StringUtils.isNotBlank(checkbox5))//承包区
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
		
		//有账号管理页面，点击交易明细，查看交易明细时用到------start
		if(userId!=null&&userId.trim().length()>0)
		{
			userIdList=new ArrayList<Integer>();
			userIdList.add(Integer.parseInt(userId));
		}
		//有账号管理页面，点击交易明细，查看交易明细时用到------end
		
		// 总共有多条记录，分页使用
		Integer total = 0;
		
		if (pagination == null) 
			pagination = new Pagination(currentPage, pageNum);
		
		if(userIdList.size()<=0)
		{
			pagination.setTotalRecords(0);
			paymentList=new ArrayList();			
			return "list";
		}
		else
		{
			params.put("userIdArray", userIdList);			
			paymentList = paymentService.getPaymentList(params, pagination, true);
			
			if (!CollectionUtils.isEmpty(paymentList)) 
			{
				// 得到用户的类型，保存list，页面显示使用
				for (Payment payment : paymentList) {
					Integer userId = payment.getUserId();
					User user = userService.getUserById(userId);
					if(user != null){
						payment.setUserName(user.getUserName());
						
						String userType= user.getUserType();
						if(userType.equals("2")){
							if(user.getParentId()!=null&&user.getParentId().intValue()>0)
								payment.setUserType("2;承包区");//承包区
							else
								payment.setUserType("2;网点");//网点
						}
						
						if(userType.equals("1")) {
							if(user.getChildType()!=null&&user.getChildType().length()>0)
								payment.setUserType("1;业务账号");//业务账号
							else
								payment.setUserType("1;卖家");//卖家
						}
						
						if(!userType.equals("1")&& !userType.equals("2")){
							payment.setUserType(user.getUserType());
						}
					}
				}
			}
			
			total = paymentService.getPaymentListCount(params);
			pagination.setTotalRecords(total);
			return "list";
		}
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

	public String getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(String startMoney) {
		this.startMoney = startMoney;
	}

	public String getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(String endMoney) {
		this.endMoney = endMoney;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	public List<Payment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<Payment> paymentList) {
		this.paymentList = paymentList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
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
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

}
