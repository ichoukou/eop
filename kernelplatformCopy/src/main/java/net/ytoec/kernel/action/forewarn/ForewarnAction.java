package net.ytoec.kernel.action.forewarn;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.service.SellerInfoService;
import net.ytoec.kernel.service.ZebraForewarnService;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;

@Controller
@Scope("prototype")
public class ForewarnAction extends AbstractActionSupport {
	private String userId;
	private String userCode = null;// 商家代码
	private String selectedUserCode = "";// 商家代码
	private String userName = null;// 网点名称
	private List<UserThread> userList;// 所有商家
	private User user;// 单个商家
	private Integer alreadyFaceNum;// 已用面单数
	private Integer surplusFaceNum;// 未用面单数
	private Integer toteFaceNum;// 面单总数
	private String warning = null;// 预警信息
	private ZebraForewarn forewarn;// 预警
	private String phoneWarnMsg = null;// 是否短信预警
	private String emailWarnMsg = null;// 是否邮箱预警

	private String branckWarnValue;
	private String customerWarnValue;
	private String remarkPhone;
	private String phone;
	private String email;

	private static final long serialVersionUID = -2771198512924029961L;
	private static Logger logger = LoggerFactory.getLogger(ForewarnAction.class);
	@Inject
	private ZebraSurfacebillService zebraSurfacebillService;
	@Inject
	private ZebraForewarnService zebraForewarnService;
	@Inject
	private SellerInfoService sellerInfoService;

	// 页面加载默认显示第一条记录
	public String load() {
		request.setAttribute("searchForewarnMessage","");
		request.setAttribute("loadForewarnMessage","");
		request.setAttribute("loadErrorMessage","" );
		request.setAttribute("insertForewarnMessage","");
		request.setAttribute("updateForewarnMessage","");
		request.setAttribute("saveSuccess","");
		request.setAttribute("loadNullData","");
		User user2 = super.readCookieUser();
		// 查询当前网点下所有商家
		try{
			userList = sellerInfoService.findUserAll(user2.getUserName());
			if(userList.size()==0){
				request.setAttribute("loadNullData", "您现在还没有商家！");
			}
		}catch(Exception ec){
			logger.error("载入商家信息失败", ec);
			request.setAttribute("loadErrorMessage","载入商家信息失败,请联系管理员！" );
			return "success";
		}
		for (UserThread user : userList) {
			if (StringUtils.isNotEmpty(user.getUserCode())) {
				userCode = user.getUserCode();
				break;
			}
			break;
		}
		try{
			// 查询用户面单剩余数
			surplusFaceNum = zebraSurfacebillService.selectCountsByState(userCode,0);
			// 查询面单总数
			toteFaceNum = zebraSurfacebillService.selectAllCount(userCode);
			// 以用面单数
			alreadyFaceNum = zebraSurfacebillService.selectUsedCountsByCustomerCode(userCode);
			// 查询网点预警信息
			forewarn = zebraForewarnService.selectByCustomerCode(userCode);
		}catch(Exception ec){
			logger.error("载入预警数据失败", ec);
			request.setAttribute("loadForewarnMessage","载入预警信息失败,请联系管理员！" );
			return "success";
		}
		boolean p = false;
		boolean e = false;
		if (forewarn == null) {
			p = false;
			e = false;
		} else {
			p = forewarn.isPhoneWarn();
			e = forewarn.isEmailWarn();
		}

		if (p == true) {
			phoneWarnMsg = "checked";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}else{
			phoneWarnMsg = "";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}
		if (e == true) {
			emailWarnMsg = "checked";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}else{
			emailWarnMsg = "";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}
		// 查询是已经否预警
		if (forewarn == null) {
			warning = "无数据";
			branckWarnValue = "无数据";
			customerWarnValue = "无数据";
			remarkPhone = "无数据";
			phone = "无数据";
			email = "无数据";
		} else {
			int a = forewarn.getBranckWarnValue();
			if (surplusFaceNum<a) {
				warning = "是";
			} else {
				warning = "否";
			}
		}
		
		return "success";
	}

	public String search() {
		request.setAttribute("searchForewarnMessage","");
		request.setAttribute("loadForewarnMessage","");
		request.setAttribute("loadErrorMessage","" );
		request.setAttribute("insertForewarnMessage","");
		request.setAttribute("updateForewarnMessage","");
		request.setAttribute("saveSuccess","");
		request.setAttribute("loadNullData", "");
		selectedUserCode = userCode;
		User user2 = super.readCookieUser();
		try{
			// 查询当前网点下所有商家
			
			userList = sellerInfoService.findUserAll(user2.getUserName());
			// 查询用户面单剩余数
			surplusFaceNum = zebraSurfacebillService.selectCountsByState(userCode,0);
			// 查询面单总数
			toteFaceNum = zebraSurfacebillService.selectAllCount(userCode);
			// 以用面单数
			alreadyFaceNum = zebraSurfacebillService.selectUsedCountsByCustomerCode(userCode);
			// 查询网点预警信息
			forewarn = zebraForewarnService.selectByCustomerCode(userCode);
		}catch(Exception ec){
			logger.error("查询商家预警信息失败", ec);
			request.setAttribute("searchForewarnMessage","加载预警信息失败,请联系管理员！");
			return "success";
		}
		boolean p = false;
		boolean e = false;
		if (forewarn == null) {
			p = false;
			e = false;
		} else {
			p = forewarn.isPhoneWarn();
			
			e = forewarn.isEmailWarn();
			
		}

		if (p == true) {
			phoneWarnMsg = "checked";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}else{
			phoneWarnMsg = "";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}
		if (e == true) {
			emailWarnMsg = "checked";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}else{
			emailWarnMsg = "";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}
		// 查询是已经否预警
		if (forewarn == null) {
			warning = "无数据";
			branckWarnValue = "无数据";
			customerWarnValue = "无数据";
			remarkPhone = "无数据";
			phone = "无数据";
			email = "无数据";
		} else {
			int a = forewarn.getBranckWarnValue();
			if (surplusFaceNum<a) {
				warning = "是";
			} else {
				warning = "否";
			}
		}
		return "success";

	}

	// 更新预警信息
	public String addForewarn() throws ParseException {
		selectedUserCode = userCode;
		request.setAttribute("searchForewarnMessage","");
		request.setAttribute("loadForewarnMessage","");
		request.setAttribute("loadErrorMessage","" );
		request.setAttribute("insertForewarnMessage","");
		request.setAttribute("updateForewarnMessage","");
		request.setAttribute("saveSuccess","");
		request.setAttribute("loadNullData", "");
		if (StringUtils.isEmpty(branckWarnValue)
				&& StringUtils.isEmpty(customerWarnValue)
				&& StringUtils.isEmpty(phone) 
				&& StringUtils.isEmpty(email)) {
			User user2 = super.readCookieUser();
			// 查询当前网点下所有商家
			try{
				userList = sellerInfoService.findUserAll(user2.getUserName());
				// 查询用户未用面单 
				surplusFaceNum = zebraSurfacebillService.selectCountsByState(userCode,0);
				System.out.print(surplusFaceNum);
				// 查询当前用户的ID
				// 查询面单总数
				toteFaceNum = zebraSurfacebillService.selectAllCount(userCode);
				// 以用面单数
				alreadyFaceNum = zebraSurfacebillService.selectUsedCountsByCustomerCode(userCode);
				// 查询网点预警信息
				forewarn = zebraForewarnService.selectByCustomerCode(userCode);
				request.setAttribute("saveSuccess","您未做任何更改！");
			}catch(Exception ec){
				logger.error("查询预警信息失败", ec);
				request.setAttribute("searchForewarnMessage","加载预警信息失败,请联系管理员！");
				return "success";
			}
			boolean p = false;
			boolean e = false;
			if (forewarn == null) {
				p = false;
				e = false;
			} else {
				p = forewarn.isPhoneWarn();
				
				e = forewarn.isEmailWarn();
				
			}

			if (p == true) {
				phoneWarnMsg = "checked";
				request.removeAttribute("phoneWarnMsg");
				request.setAttribute("phoneWarnMsg", phoneWarnMsg);
			}else{
				phoneWarnMsg = "";
				request.removeAttribute("phoneWarnMsg");
				request.setAttribute("phoneWarnMsg", phoneWarnMsg);
			}
			if (e == true) {
				emailWarnMsg = "checked";
				request.removeAttribute("emailWarnMsg");
				request.setAttribute("emailWarnMsg", emailWarnMsg);
			}else{
				emailWarnMsg = "";
				request.removeAttribute("emailWarnMsg");
				request.setAttribute("emailWarnMsg", emailWarnMsg);
			}
			// 查询是已经否预警
			if (forewarn == null) {
				warning = "无数据";
				branckWarnValue = "无数据";
				customerWarnValue = "无数据";
				remarkPhone = "无数据";
				phone = "无数据";
				email = "无数据";
			} else {
				int a = forewarn.getBranckWarnValue();
				if (surplusFaceNum<a) {
					warning = "是";
				} else {
					warning = "否";
				}
			}
			return "success";
		}
		String p = phoneWarnMsg;
		String e = emailWarnMsg;
		boolean p1;
		boolean e1;
		if (p == null) {
			p1 = false;
		} else {
			p1 = true;
		}

		if (e == null) {
			e1 = false;
		} else {
			e1 = true;
		}

		ZebraForewarn forewarn1 = zebraForewarnService
				.selectByCustomerCode(userCode);
		
		ZebraForewarn z = new ZebraForewarn();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date d = df.parse(df.format(new Date()));
		if (forewarn1 == null) {
			// 插入
			try{
				z.setBranckId(Integer.parseInt(userId));
				z.setCustomerCode(userCode);
				z.setBranckWarnValue(Integer.parseInt(branckWarnValue));
				z.setCustomerWarnValue(Integer.parseInt(customerWarnValue));
				z.setCustomerWarnHistoryValue(0);
				z.setPhone(phone);
				z.setRemarkPhone(remarkPhone);
				z.setEmail(email);
				z.setPhoneWarn(p1);
				z.setEmailWarn(e1);
				z.setUpdateTime(d);
				zebraForewarnService.insertForwarnByCustomerCode(z);
				request.setAttribute("saveSuccess","预警信息已成功保存！");
			}catch(Exception ec){
				logger.error("插入预警信息失败",ec);
				request.setAttribute("insertForewarnMessage","保存预警信息失败,请联系管理员！");
				return "success";
			}
		} else {
			// 更新
			try{	
					z.setBranckId(Integer.parseInt(userId));
					z.setCustomerCode(userCode);
//					z.setBranckWarnValue(forewarn1.getBranckWarnValue());//临时更改
//					z.setCustomerWarnValue(forewarn1.getCustomerWarnValue());//临时更改
					z.setBranckWarnValue(Integer.parseInt(branckWarnValue));
					z.setCustomerWarnValue(Integer.parseInt(customerWarnValue));
					z.setPhone(phone);
					z.setRemarkPhone(remarkPhone);
					z.setEmail(email);
					z.setPhoneWarn(p1);
					z.setEmailWarn(e1);
					z.setUpdateTime(d);
					zebraForewarnService.updateForwarnByCustomerCode(z);
					request.setAttribute("saveSuccess","预警信息已成功保存！");
			}catch(Exception ec){
				logger.error("更新预警信息失败",ec);
				request.setAttribute("updateForewarnMessage","保存预警信息失败,请联系管理员！");
				return "success";
			}

		}
		User user2 = super.readCookieUser();
		// 查询当前网点下所有商家
		try{
			request.setAttribute("searchForewarnMessage","");
			userList = sellerInfoService.findUserAll(user2.getUserName());
			// 查询用户面单剩余数
			surplusFaceNum = zebraSurfacebillService.selectCountsByState(userCode,0);
			// 查询当前用户的ID
			// 查询面单总数
			toteFaceNum = zebraSurfacebillService.selectAllCount(userCode);
			// 以用面单数
			alreadyFaceNum = zebraSurfacebillService.selectUsedCountsByCustomerCode(userCode);
			// 查询网点预警信息
			forewarn = zebraForewarnService.selectByCustomerCode(userCode);
		}catch(Exception ec){
			logger.error("查询预警信息失败", ec);
			request.setAttribute("searchForewarnMessage","加载预警信息失败,请联系管理员！");
			return "success";
		}
		boolean p2 = false;
		boolean e2 = false;
		if (forewarn == null) {
			p2 = false;
			e2 = false;
		} else {
			p2 = forewarn.isPhoneWarn();
			e2 = forewarn.isEmailWarn();
		}

		if (p2 == true) {
			phoneWarnMsg = "checked";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}else{
			phoneWarnMsg = "";
			request.removeAttribute("phoneWarnMsg");
			request.setAttribute("phoneWarnMsg", phoneWarnMsg);
		}
		if (e2 == true) {
			emailWarnMsg = "checked";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}else{
			emailWarnMsg = "";
			request.removeAttribute("emailWarnMsg");
			request.setAttribute("emailWarnMsg", emailWarnMsg);
		}
		
		branckWarnValue = Integer.toString(forewarn.getBranckWarnValue());
		customerWarnValue = Integer.toString(forewarn.getCustomerWarnValue());
		remarkPhone =forewarn.getRemarkPhone();
		phone = forewarn.getPhone();
		email = forewarn.getEmail();

		if (forewarn == null) {
			p1 = false;
			e1 = false;
		} else {
			p1 = forewarn.isPhoneWarn();
			e1 = forewarn.isEmailWarn();
		}

		if (p1 == true) {
			phoneWarnMsg = "checked";
		}
		if (e1 == true) {
			emailWarnMsg = "checked";
		}
		// 查询是已经否预警
		if (forewarn == null) {
			warning = "无数据";
		} else {
			int a = forewarn.getBranckWarnValue();
			if (surplusFaceNum<a) {
				warning = "是";
			} else {
				warning = "否";
			}
		}
		
		return "success";
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

	public Integer getAlreadyFaceNum() {
		return alreadyFaceNum;
	}

	public void setAlreadyFaceNum(Integer alreadyFaceNum) {
		this.alreadyFaceNum = alreadyFaceNum;
	}

	public Integer getSurplusFaceNum() {
		return surplusFaceNum;
	}

	public void setSurplusFaceNum(Integer surplusFaceNum) {
		this.surplusFaceNum = surplusFaceNum;
	}

	public Integer getToteFaceNum() {
		return toteFaceNum;
	}

	public void setToteFaceNum(Integer toteFaceNum) {
		this.toteFaceNum = toteFaceNum;
	}



	public List<UserThread> getUserList() {
		return userList;
	}

	public void setUserList(List<UserThread> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public ZebraForewarn getForewarn() {
		return forewarn;
	}

	public void setForewarn(ZebraForewarn forewarn) {
		this.forewarn = forewarn;
	}

	public ZebraSurfacebillService getZebraSurfacebillService() {
		return zebraSurfacebillService;
	}

	public void setZebraSurfacebillService(
			ZebraSurfacebillService zebraSurfacebillService) {
		this.zebraSurfacebillService = zebraSurfacebillService;
	}

	public ZebraForewarnService getZebraForewarnService() {
		return zebraForewarnService;
	}

	public void setZebraForewarnService(
			ZebraForewarnService zebraForewarnService) {
		this.zebraForewarnService = zebraForewarnService;
	}

	public SellerInfoService getSellerInfoService() {
		return sellerInfoService;
	}

	public void setSellerInfoService(SellerInfoService sellerInfoService) {
		this.sellerInfoService = sellerInfoService;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPhoneWarnMsg() {
		return phoneWarnMsg;
	}

	public void setPhoneWarnMsg(String phoneWarnMsg) {
		this.phoneWarnMsg = phoneWarnMsg;
	}

	public String getEmailWarnMsg() {
		return emailWarnMsg;
	}

	public void setEmailWarnMsg(String emailWarnMsg) {
		this.emailWarnMsg = emailWarnMsg;
	}

	public String getBranckWarnValue() {
		return branckWarnValue;
	}

	public void setBranckWarnValue(String branckWarnValue) {
		this.branckWarnValue = branckWarnValue;
	}

	public String getCustomerWarnValue() {
		return customerWarnValue;
	}

	public void setCustomerWarnValue(String customerWarnValue) {
		this.customerWarnValue = customerWarnValue;
	}

	public String getRemarkPhone() {
		return remarkPhone;
	}

	public void setRemarkPhone(String remarkPhone) {
		this.remarkPhone = remarkPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSelectedUserCode() {
		return selectedUserCode;
	}

	public void setSelectedUserCode(String selectedUserCode) {
		this.selectedUserCode = selectedUserCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
