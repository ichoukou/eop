package net.ytoec.kernel.action.user;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.forewarn.ForewarnAction;
import net.ytoec.kernel.common.FailinfoMD5Util;
import net.ytoec.kernel.common.JsonUtil;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SellerInfoService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 商家信息管理
 * 
 * @author John
 * 
 */
@Controller
@Scope("prototype")
public class SellerInfoAction extends AbstractActionSupport {
	
	private static final long serialVersionUID = 1L;
	private String sellerUserCode = null;// 商家编码
	private String userName = null;// 商家名称
	private String parternCode = null;// 商家密钥
	// private String userName;// 运维登陆初始设置网店或商家
	// private String userPassword;// 运维登陆初始设置网店或商家密码
	private Logger logger = LoggerFactory.getLogger(ForewarnAction.class);
	private String searchMEassag = null;
	private String sellerUserCodeMsg = null;
	@Inject
	private SellerInfoService sellerInfoService;

	@Inject
	private MailService<Mail> mailService;
//	@Inject
//	private NewMailService NewMailService;

	@Inject
	private UserService<User> userService;

//	@Inject
//	private SimpleMailSender service;

	private String zebraPartern = null;// 密钥输出
	private User user = null;// 用户信息输出
	private String showUserNmae = null;// 账号输出
	private String showPwd = null;// 明文密码
	private String userNull;

	
    
	/**
	 * 根据商家编码和名称模糊查询
	 * 
	 * @return
	 */
	public String search() {
		request.setAttribute("seraUserOrParternError", "");
		try {
			if (StringUtils.isEmpty(sellerUserCode)
					&& StringUtils.isEmpty(userName)) {
				return "doMerchantMain";
			}

			if (sellerUserCode == "" && userName == "") {
				searchMEassag = "商家编码和名称不能为空";
				return "error";
			} else if (StringUtils.isEmpty(sellerUserCode)
					&& StringUtils.isNotEmpty(userName)) {
				// 根据商家名称查询密钥
				zebraPartern = sellerInfoService
						.searchParternBysellerUserName(userName);
				user = sellerInfoService.searchUserByUserName(userName);

				if (user == null) {
					user = new User();
					userNull = "";
					request.removeAttribute("userNull");
					request.setAttribute("userNull", userNull);
				}
				String userCode = null;
				userCode = user.getUserCode();
				return "success";
			} else if (StringUtils.isNotEmpty(sellerUserCode)
					&& StringUtils.isEmpty(userName)) {

				// 根据商家编码查用户
				List<User> list = new ArrayList<User>();
				list = sellerInfoService.searchUserByUserCode(sellerUserCode);
				if (list.size() == 0) {
					user = new User();
				} else {
					user = list.get(0);
				}

				userName = sellerInfoService
						.findUserNameByUserCode(sellerUserCode);
				if (userName == null) {
					user = new User();
					userNull = "";
					request.removeAttribute("userNull");
					request.setAttribute("userNull", userNull);
					// 根据商家编码查询查密钥
					// zebraPartern = sellerInfoService
					// .searchParternBysellerUserCode(sellerUserCode);
				} else {
					// 根据商家编码查询查密钥
					zebraPartern = sellerInfoService
							.searchParternBysellerUserCode(sellerUserCode);
				}
				return "success";

			} else if (StringUtils.isNotEmpty(sellerUserCode)
					&& StringUtils.isNotEmpty(userName)) {

				// 根据商家编码查用户
				List<User> list = new ArrayList<User>();
				list = sellerInfoService.searchUserByUserCode(sellerUserCode);
				if (list.size() == 0) {
					user = new User();
				} else {
					user = list.get(0);
				}
				userName = sellerInfoService
						.findUserNameByUserCode(sellerUserCode);
				String userCode = sellerInfoService
						.findUserCodeByUserCode(sellerUserCode);
				if (userCode == null) {
					user = new User();
					userNull = "";
					request.removeAttribute("userNull");
					request.setAttribute("userNull", userNull);
				} else {
					// 根据商家编码查询查密钥
					zebraPartern = sellerInfoService
							.searchParternBysellerUserCode(sellerUserCode);
				}
			}
		} catch (Exception ex) {
			logger.error("SerachPartarnError--->" + ex);
			request.setAttribute("seraUserOrParternError", "查询用户或密钥出现异常，请稍后重试！");
			user = new User();
			return "success";
		}
		return "success";
	}

	// 生成密钥
	public void createKeyt() throws ParseException {
		try {
			if (StringUtils.isEmpty(sellerUserCode)) {
				sellerUserCodeMsg = "商家代码不可为空，请输入商家代码！";
				JsonBean jsonBean = new JsonBean();
				jsonBean.setSellerUserCodeMsg(sellerUserCodeMsg);
				String json = JsonUtil.toJson(jsonBean);
				JsonUtil.response(json);
				return;
			}
			userName = sellerInfoService.findUserNameByUserCode(sellerUserCode);
			// 查询是否存在客户编码
			String userCode1 = sellerInfoService
					.findUserCodeByUserCode(sellerUserCode);
			if (userCode1 == null) {
				JsonBean jsonBean = new JsonBean();
				jsonBean.setUserNull("");
				String json = JsonUtil.toJson(jsonBean);
				JsonUtil.response(json);
			} else {
				char[] ss = new char[8];
				int i = 0;
				while (i < ss.length) {
					int f = (int) (Math.random() * 3);
					if (f == 0)
						ss[i] = (char) ('A' + Math.random() * 26);
					else if (f == 1)
						ss[i] = (char) ('a' + Math.random() * 26);
					else
						ss[i] = (char) ('0' + Math.random() * 10);
					i++;
				}
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 设置日期格式
				Date d = df.parse(df.format(new Date()));
				Channel z = new Channel();
				z.setUpdateTime(d);
				/**
				 * 将生成的密钥放入渠道表（易通与仓配通交互的密钥）
				 */
				String s = new String(ss);
				z.setUserCode(sellerUserCode);
				z.setKey(sellerUserCode);
				z.setClientId(sellerUserCode);
				z.setIp(sellerUserCode);
				z.setParternId(s);
				// 查询是否存在记录
				zebraPartern = sellerInfoService
						.searchParternBysellerUserCode(sellerUserCode);

				if (zebraPartern == null) {
					// 插入
					sellerInfoService.inserteByPrimaryKeySelective(z);

				} else {
					// 更新
					sellerInfoService.updateByPrimaryKeySelective(z);
				}
				/**
				 * 将生成的密钥放入密钥表（易通与金刚交互的密钥）
				 */
				ZebraPartern zebraParternJg = new ZebraPartern();
				zebraParternJg.setCustomerCode(sellerUserCode);
				zebraParternJg.setParternCode(s);
				zebraParternJg.setUpdateTime(d);
				// 查询密钥表是否为空
				String parternJg = sellerInfoService
						.findParternByUserCode(sellerUserCode);
				if (parternJg == null) {
					// 插入
					sellerInfoService
							.insertByCustomerCodeFromPartern(zebraParternJg);
				} else {
					// 更新
					sellerInfoService
							.updateByCustomerCodeFromPartern(zebraParternJg);
				}

				zebraPartern = sellerInfoService
						.searchParternBysellerUserCode(sellerUserCode);

				JsonBean jsonBean = new JsonBean();
				jsonBean.setZebraPartern(zebraPartern);
				String json = JsonUtil.toJson(jsonBean);
				JsonUtil.response(json);
			}

		} catch (Exception ex) {
			logger.error("CreateParternError--->" + ex);
			JsonBean jsonBean = new JsonBean();
			jsonBean.setCreateKeytError("生成密钥出现异常，请联系管理员！");
			String json = JsonUtil.toJson(jsonBean);
			JsonUtil.response(json);
		}
	}

	// 生成账户
	public void createUserInfo() throws ParseException,
			NoSuchAlgorithmException {
		try {
			userName = sellerInfoService.findUserNameByUserCode(sellerUserCode);
			// 查询是否存在客户编码
			String userCode2 = sellerInfoService
					.findUserCodeByUserCode(sellerUserCode);
			if (userCode2 == null) {
				JsonBean jsonBean = new JsonBean();
				jsonBean.setUserNull("");
				String json = JsonUtil.toJson(jsonBean);
				JsonUtil.response(json);
			} else {

				User u = new User();
				// String userName = "K";
				// String password = "";
				Random random = new Random();
				// for (int i = 0; i < 7; i++) {
				// userName += r.nextInt(10);
				//
				// password += r.nextInt(10);
				// }
				int x = random.nextInt(89999999);
				int x1 = x + 100000;
				int pwd1 = x1 - 123;
				String newUserName = "K" + Integer.toString(x1);
				String password = Integer.toString(pwd1);

				if (StringUtils.isEmpty(sellerUserCode)) {
					sellerUserCodeMsg = "商家代码不可为空，请输入商家代码！";
					JsonBean jsonBean = new JsonBean();
					jsonBean.setSellerUserCodeMsg(sellerUserCodeMsg);
					String json = JsonUtil.toJson(jsonBean);
					JsonUtil.response(json);
					return;
				}

				String pwd = FailinfoMD5Util.encrypt(password);
				showUserNmae = newUserName;
				showPwd = password;// 明文密码
				// 根据商家编码查询网点userName
				// String
				// siteCode=sellerInfoService.findSiteCodeByUserCode(sellerUserCode);

				// zebraPartern = sellerInfoService
				// .searchParternBysellerUserName(sellerUserCode);
				// System.out.print(zebraPartern);
				List<User> list = new ArrayList<User>();
				User use = new User();
				list = sellerInfoService.searchUserByUserCode(sellerUserCode);
				// 查询商家的网点编码
				String siteCode = null;
				try {
					siteCode = sellerInfoService
							.findSiteCodeByUserCodeFromUT(sellerUserCode);
				} catch (Exception ex) {
					logger.equals(ex);
				}
				if (list.size() == 0) {
					use = new User();
				} else {
					use = list.get(0);
				}

				// 初始化商家用户信息
				u.setUserName(newUserName);
				u.setUserPassword(pwd);
				u.setUserType("1");
				u.setUserCode(sellerUserCode);
				u.setUserState("TBA");
				u.setUserNameText(userName);
//
//				if (StringUtils.isNotEmpty(use.getUserName())
//						&& StringUtils.isNotEmpty(use.getUserPassword())) {
					// System.out.println("-----更新----");
					// // 更新
					// u.setTaobaoEncodeKey(use.getId()+sellerUserCode);
					// sellerInfoService.updateOrInsertUser(u);

//				} else {
					System.out.println("-----插入----");
					// 插入
					sellerInfoService.insertUser(u);
					// 插入后更新taobaoEncodeKey
					String updaeUserId = sellerInfoService.findIdByUserName(u
							.getUserName());
					u.setTaobaoEncodeKey(updaeUserId + sellerUserCode);
					sellerInfoService.updateTaobaoEncodeKey(u);

//				}

				List<User> lis = new ArrayList<User>();
				lis = sellerInfoService.searchUserByUserCode(sellerUserCode);
				User use1 = new User();
				if (lis.size() == 0) {
					use1 = new User();
				} else {
					use1 = lis.get(0);
				}

				// 查询网点邮箱
				String userMail = sellerInfoService
						.findEmailByUserName(siteCode);
				 String mailContent = "<b>尊敬的易通用户：</b><br/><b>您好!</b><br/>"+
				 "<b>您的商家" +
				 "(" + sellerUserCode + ")"+
				 "新帐户已创建成功！</b><br/><b>新的用户名为：</b>" + newUserName +
				 "<br/>"+"<b>密码为：</b>"+"<b>"+ showPwd+"</b>"+
				 "<br/><br/><br/><br/><br/><br/>"+"<b>上海圆通新龙电子商务有限公司</b><br/>"+
				 "<b><a href='http://www.ytoxl.com'>www.ytoxl.com</a></b><br/>"+
				 "<b>Room106, Building 1, No.396 Guilin Rd Xuhui District Shanghai</b><br/>"+
				 "<b>上海市徐汇区桂林路 396 号 1 号楼106</b>";

//				String mailContent = "尊敬的易通用户：您好！您的商家" + "(" + sellerUserCode
//						+ ")" + "新帐户已创建成功！用户名为：" + newUserName + "密码为："
//						+ showPwd;

//				if (StringUtils.isEmpty(use.getUserName())) {
					Mail mail = new Mail();
					mail.setFromMail("yto.eccore@gmail.com");
					mail.setFromMailText("");
					mail.setSendToMail(userMail);
					mail.setSubject("用户信息修改提示");
					mail.setContent(mailContent);
					boolean flag = mailService.sendMail(mail);
					if (!flag) {
						logger.error("发送邮件失败");
					}
					JsonBean jsonBean = new JsonBean();
					jsonBean.setUserName(showUserNmae);
					jsonBean.setShowPwd(showPwd);
					jsonBean.setFlag(flag);
					String json = JsonUtil.toJson(jsonBean);
					JsonUtil.response(json);
//				} 
//				else {
//					JsonBean jsonBean = new JsonBean();
//					jsonBean.setAleradyUser("该用户已存在，无需创建！");
//					String json = JsonUtil.toJson(jsonBean);
//					JsonUtil.response(json);
//				}

			}

		} catch (Exception ex) {
			logger.error("CreateUserInfoError---->" + ex);
			JsonBean jsonBean = new JsonBean();
			jsonBean.setCreateUserError("创建用户出现异常，请与管理员联系！");
			String json = JsonUtil.toJson(jsonBean);
			JsonUtil.response(json);
		}
	}

	class JsonBean {
		private String showPwd;
		private String userName;
		private String passWord;
		private String showPassWord;
		private String zebraPartern;
		private String sellerUserCodeMsg;
		private Boolean flag;
		private String createKeytError;
		private String userNull;
		private String createUserError;
		private String aleradyUser;
		private Boolean info;

		public Boolean getInfo() {
			return info;
		}

		public void setInfo(Boolean info) {
			this.info = info;
		}

		public String getAleradyUser() {
			return aleradyUser;
		}

		public void setAleradyUser(String aleradyUser) {
			this.aleradyUser = aleradyUser;
		}

		public String getCreateKeytError() {
			return createKeytError;
		}

		public void setCreateKeytError(String createKeytError) {
			this.createKeytError = createKeytError;
		}

		public String getCreateUserError() {
			return createUserError;
		}

		public void setCreateUserError(String createUserError) {
			this.createUserError = createUserError;
		}

		public String getUserNull() {
			return userNull;
		}

		public void setUserNull(String userNull) {
			this.userNull = userNull;
		}

		public String getZebraPartern() {
			return zebraPartern;
		}

		public void setZebraPartern(String zebraPartern) {
			this.zebraPartern = zebraPartern;
		}

		public void setSellerUserCodeMsg(String sellerUserCodeMsg) {
			this.sellerUserCodeMsg = sellerUserCodeMsg;
		}

		public String getSellerUserCodeMsg() {
			return sellerUserCodeMsg;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassWord() {
			return passWord;
		}

		public void setPassWord(String passWord) {
			this.passWord = passWord;
		}

		public String getShowPassWord() {
			return showPassWord;
		}

		public void setShowPassWord(String showPassWord) {
			this.showPassWord = showPassWord;
		}

		public void setShowPwd(String showPwd) {
			this.showPwd = showPwd;
		}

		public String getShowPwd() {
			return showPwd;
		}

		public Boolean getFlag() {
			return flag;
		}

		public void setFlag(Boolean flag) {
			this.flag = flag;
		}

	}

	public String getSellerUserCode() {
		return sellerUserCode;
	}

	public void setSellerUserCode(String sellerUserCode) {
		this.sellerUserCode = sellerUserCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParternCode() {
		return parternCode;
	}

	public void setParternCode(String parternCode) {
		this.parternCode = parternCode;
	}

	public String getSearchMEassag() {
		return searchMEassag;
	}

	public void setSearchMEassag(String searchMEassag) {
		this.searchMEassag = searchMEassag;
	}

	public SellerInfoService getSellerInfoService() {
		return sellerInfoService;
	}

	public void setSellerInfoService(SellerInfoService sellerInfoService) {
		this.sellerInfoService = sellerInfoService;
	}

	public String getZebraPartern() {
		return zebraPartern;
	}

	public void setZebraPartern(String zebraPartern) {
		this.zebraPartern = zebraPartern;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}


	public String getSellerUserCodeMsg() {
		return sellerUserCodeMsg;
	}

	public void setSellerUserCodeMsg(String sellerUserCodeMsg) {
		this.sellerUserCodeMsg = sellerUserCodeMsg;
	}

	public String getShowPwd() {
		return showPwd;
	}

	public void setShowPwd(String showPwd) {
		this.showPwd = showPwd;
	}

	

	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public String getShowUserNmae() {
		return showUserNmae;
	}

	public void setShowUserNmae(String showUserNmae) {
		this.showUserNmae = showUserNmae;
	}

	public String getUserNull() {
		return userNull;
	}

	public void setUserNull(String userNull) {
		this.userNull = userNull;
	}

}
