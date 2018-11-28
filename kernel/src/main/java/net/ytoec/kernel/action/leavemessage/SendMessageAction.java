package net.ytoec.kernel.action.leavemessage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoReceiver;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 发送留言
 * @author Wangyong
 * @2011-7-28
 * net.ytoec.kernel.action.leavemessage
 */
@Controller
@Scope("prototype")
public class SendMessageAction extends AbstractActionSupport {

	private static Logger logger=Logger.getLogger(SendMessageAction.class);
	
	@Inject
	private MessageService<Message> messageService;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	
	//消息主题
	private String messageTheme;
	//消息内容
	private String messageContent;
	//网点id串，以逗号","相隔
	private String receiveIdString;
	//卖家用户编码串，以逗号","相隔
	private String receiveUserCodeString;
	//卖家用户名称，以逗号","相隔
	private String receiveUserNameString;
	//消息id
	private Integer messageId;
	//消息回复内容
	private String replyContent;
	//接收人信息
	private List<DtoReceiver> receiverUser;
	//发送消息接收人查询条件
	private String receiverCondition;
	
	private String siteName = "";
	
	/**
	 * 跳转到发送消息页面
	 * @return
	 */
	public String openUI(){
		User curUser = super.readCookieUser();
		String site = curUser.getSite();
		if (StringUtils.isNotEmpty(site)) {
			siteName = Resource.getDtoBranchByCode(site).getText();
		}
		return "openUI";
	}
	
	public String openUIIndex(){
		User curUser = super.readCookieUser();
		return "openUIIndex";
	}
	
	/**
	 * 跳转到发送建议页面
	 * @return
	 */
	public String openAdviseUI(){
		User curUser = super.readCookieUser();
		return "openAdviseUI";
	}
	
	/**
	 * 发送消息
	 * @return
	 */
	public String send(){
		User curUser = super.readCookieUser();
		if(!StringUtils.isEmpty(messageTheme) && !StringUtils.isEmpty(messageContent)){
			String messageType = "";
			boolean result = false;
			if(curUser.getUserType().equals("1") || curUser.getUserType().equals("11") || curUser.getUserType().equals("12") || curUser.getUserType().equals("13")){
				messageType = "1";
				//卖家给自己所属网点发消息
				result = messageService.sendMessage(curUser, messageTheme, messageContent, null, messageType);
			}else if(curUser.getUserType().equals("2") || curUser.getUserType().equals("21") || curUser.getUserType().equals("22") || curUser.getUserType().equals("23")){
				messageType = "2";
				//网点给直客发消息
				if(!StringUtils.isEmpty(receiveUserCodeString)){
					List<String> receiveUser = new ArrayList<String>();
					String[] receiveArr = receiveUserCodeString.split(",");
					for(int i=0; i<receiveArr.length; i++)
						receiveUser.add(receiveArr[i]);
					result = messageService.sendMessage(curUser, messageTheme, messageContent, receiveUser, messageType);
				}
			}else if(curUser.getUserType().equals("3")){//管理员发消息
				boolean resultSite=true,resultUser=true;
				if(!StringUtils.isEmpty(receiveIdString)){
					List<String> receiveUserId = new ArrayList<String>();//网点idList
					String[] receiveIdStringArr = receiveIdString.split(",");
					for(int i=0; i<receiveIdStringArr.length; i++){
						receiveUserId.add(receiveIdStringArr[i].trim());
					}
					resultSite = messageService.sendMessage(curUser, messageTheme, messageContent, receiveUserId, 0);
				}
				if(!StringUtils.isEmpty(receiveUserCodeString)){
					List<String> receiveUserCode = new ArrayList<String>();//卖家codeList
					String[] receiveCodeStringArr = receiveUserCodeString.split(",");
					for(int j=0; j<receiveCodeStringArr.length; j++){
						receiveUserCode.add(receiveCodeStringArr[j].trim());
					}
					resultUser = messageService.sendMessage(curUser, messageTheme, messageContent, receiveUserCode, 1);
				}
				result = resultUser&&resultSite;
			}
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "发送成功", "");
			}else{
				putMsg(JsonResponse.INFO_TYPE_ERROR, true, "发送失败", "");
			}
		}
		return "jsonResponse";
	}
	
	/**
	 * 发送建议：发送建议给管理员
	 * @return
	 */
	public String suggest(){
		User curUser = super.readCookieUser();
		if(!StringUtils.isEmpty(messageTheme) && !StringUtils.isEmpty(messageContent)){
			boolean result = messageService.sendAdvise(curUser, messageTheme, messageContent);
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "发送成功", "");
			}else
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "发送失败", "");
		}
		return "jsonResponse";
	}
	
	/**
	 * 回复消息
	 * @return
	 */
	public String reply(){
		User curUser = super.readCookieUser();
		if(StringUtils.isNotEmpty(replyContent)){
			boolean result = messageService.replyMessage(curUser, messageId, replyContent);
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "发送成功", "");
			}else
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "发送失败", "");
		}
		return "jsonResponse";
		
	}
	
	/**
	 * 获取接收人信息
	 * @return
	 */
	public String receiver(){
		User curUser = super.readCookieUser();
		receiverUser = new ArrayList<DtoReceiver>();
		if(curUser.getUserType().equals("2") || curUser.getUserType().equals("21") || curUser.getUserType().equals("22") || curUser.getUserType().equals("23")){
			List<UserThread> userThreadList = super.getZhiKeUser(curUser);
			if(userThreadList!=null){
				for(UserThread ut : userThreadList){
					DtoReceiver receiver = new DtoReceiver();
					receiver.setUserCode(ut.getUserCode());
					receiver.setUserName(ut.getUserName());
					receiver.setUserType(1);
					if(StringUtils.isNotEmpty(receiverCondition)){
						if(ut.getUserName().contains(receiverCondition.trim())){
							receiverUser.add(receiver);
						}
					}else{
						receiverUser.add(receiver);
					}
				}
			}
		}else if(curUser.getUserType().equals("3")){//管理员需获取网点和直客两种用户列表
			//获取网点用户
			List<User> userList = userService.getUserListByUserType("2");
			if(userList!=null){
				for(User u : userList){
					DtoReceiver receiver = new DtoReceiver();
					receiver.setUserId(u.getId());
					receiver.setUserName(u.getUserName());
					receiver.setUserType(2);
					if(StringUtils.isNotEmpty(receiverCondition)){
						if(u.getUserName().contains(receiverCondition.trim())){
							receiverUser.add(receiver);
						}
					}else{
						receiverUser.add(receiver);
					}
				}
			}
			//获取激活的直客列表
			List<UserThread> userThreadList = userThreadService.getByState("1");
			if(userThreadList!=null){
				for(UserThread ut : userThreadList){
					DtoReceiver receiver = new DtoReceiver();
					receiver.setUserCode(ut.getUserCode());
					receiver.setUserName(ut.getUserName());
					receiver.setUserType(1);
					if(StringUtils.isNotEmpty(receiverCondition)){
						if(ut.getUserName().contains(receiverCondition.trim())){
							receiverUser.add(receiver);
						}
					}else{
						receiverUser.add(receiver);
					}
				}
			}
		}
		return "receiver";
	}
	
	public String getMessageTheme() {
		return messageTheme;
	}

	public void setMessageTheme(String messageTheme) {
		this.messageTheme = messageTheme;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getReceiveIdString() {
		return receiveIdString;
	}

	public void setReceiveIdString(String receiveIdString) {
		this.receiveIdString = receiveIdString;
	}

	public String getReceiveUserCodeString() {
		return receiveUserCodeString;
	}

	public void setReceiveUserCodeString(String receiveUserCodeString) {
		this.receiveUserCodeString = receiveUserCodeString;
	}

	public String getReceiveUserNameString() {
		return receiveUserNameString;
	}

	public void setReceiveUserNameString(String receiveUserNameString) {
		this.receiveUserNameString = receiveUserNameString;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public List<DtoReceiver> getReceiverUser() {
		return receiverUser;
	}

	public void setReceiverUser(List<DtoReceiver> receiverUser) {
		this.receiverUser = receiverUser;
	}

	public String getReceiverCondition() {
		return receiverCondition;
	}

	public void setReceiverCondition(String receiverCondition) {
		this.receiverCondition = receiverCondition;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}
