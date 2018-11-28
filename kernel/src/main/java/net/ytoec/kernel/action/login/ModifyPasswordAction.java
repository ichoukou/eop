package net.ytoec.kernel.action.login;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionSupport;
/**
 * description  用于管理员重置密码的类
 * created  by  hufei@2013-04-26
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class ModifyPasswordAction extends ActionSupport{
	private String ajaxAlertText;
	private String loginName;
	private boolean status;
	private final String randomPass="123456";
	
	@Inject
	private UserService<User> userService;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getAjaxAlertText() {
		return ajaxAlertText;
	}
	public void setAjaxAlertText(String ajaxAlertText) {
		this.ajaxAlertText = ajaxAlertText;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	/**
	 *  description  重置密码页面
	 *  created  by  hufei@2013-04-26
	 */
	public String passwdPage(){
		return SUCCESS;
	}
	
	/**
	 * description 验证用户是否存在
	 * created by  hufei@2013-04-26
	 */
	public String checkFindPass() {
		User user = userService.searchUsersByLoginName(loginName);
		if(user==null){
			ajaxAlertText="登录账号不存在，请重新输入";
			status=false;
		}else{
			status=true;
		}
		return "input";
	}
	/**
	 * description 重置密码
	 * @return
	 */
	public String modifyPassword(){
		User user = userService.searchUsersByLoginName(loginName);
		user.setUserPassword(Md5Encryption.MD5Encode(randomPass));
		boolean flag=userService.updateUser(user);
		 if(flag){
			 ajaxAlertText="重置成功";
			 status=true;
		 }else{
			 ajaxAlertText="稍后再试";
			 status=false;
		 }
		return "modify";
	}
}
