/**
 * 
 */
package net.ytoec.kernel.action.leavemessage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.MessageClassifyEnum;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.MessageReply;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MessageReplyService;
import net.ytoec.kernel.service.MessageService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 留言管理：网点或vip查看留言、发送留言。
 * @author Wangyong
 * @2011-7-27
 * net.ytoec.kernel.action.leavemessage
 */
@Controller
@Scope("prototype")
public class LeaveMessageAction extends AbstractActionSupport {

	private static Logger logger = LoggerFactory.getLogger(LeaveMessageAction.class);
	@Inject
	private MessageService<Message> messageService;
	@Inject
	private MessageReplyService<MessageReply> messageReplyService;
	
	private List<MessageClassifyEnum> typeList;
	
	//消息类型（默认为0）
	private Integer classify=0;
	
	private Integer currentPage=1;
	
	private Pagination pagination;
	
	private List<Message> messageList;
	
	private List<MessageReply> replyList;
	//消息Id 
	private Integer messageId;
	//标记状态1已读/0未读
	private Integer messageStatus;
	//消息id串：以","相隔
	private String messageIds;
	
	@SuppressWarnings("rawtypes")
    public String nonIndex(){
	    pagination = new Pagination(currentPage, pageNum);
	    pagination.setTotalRecords(0);
	    return "list";
    }
	
	public String index(){
		return list();
	}
	
	/**
	 * 我的消息
	 * @return
	 */
	public String list(){
		User curUser = super.readCookieUser();
		this.initMessageClassify(curUser);
		pagination = new Pagination(currentPage, pageNum);
		int count = 0;
		if(classify!=null){
			messageList = messageService.getMessageList(curUser, classify.toString(), pagination);
			count = messageService.countMessageList(curUser, classify.toString());
		}
		pagination.setTotalRecords(count);
		return "list";
	}
	
	/**
	 * 获取消息的回复
	 * @return
	 */
	public String replyList(){
		if(messageId != null){
			replyList = messageReplyService.getReplyListByMap(messageId, null);
		}
		return "replyList";
	}
	
	public String mark(){
		User curUser = super.readCookieUser();
		if(messageId!=null && messageStatus!=null){
			boolean result = messageService.markMessage(messageId, curUser, messageStatus);
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "标记成功", "");
			}else{
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "标记失败", "");
			}
		}
		return "jsonRes";
	}
	
	public String marks(){
		User curUser = super.readCookieUser();
		if(StringUtils.isNotEmpty(messageIds) && messageStatus!=null){
			List<Integer> msgIds = new ArrayList<Integer>();
			String[] msgArr = messageIds.split(",");
			for(int i=0; i<msgArr.length; i++){
				if(msgArr[i]!=null)
					msgIds.add(Integer.parseInt(msgArr[i]));
			}
			boolean result = messageService.markMessages(msgIds, curUser, messageStatus);
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "标记成功", "");
			}else
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "标记失败", "");
		}
		return "jsonRes";
	}
	
	public String delete(){
		User curUser = super.readCookieUser();
		if(StringUtils.isNotEmpty(messageIds)){
			List<Integer> msgIds = new ArrayList<Integer>();
			String[] msgArr = messageIds.split(",");
			for(int i=0; i<msgArr.length; i++){
				if(msgArr[i]!=null)
					msgIds.add(Integer.parseInt(msgArr[i]));
			}
			boolean result = messageService.deleteMessage(msgIds, curUser);
			if(result){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "删除成功", "");
			}else
				putMsg(JsonResponse.INFO_TYPE_ERROR, false, "删除失败", "");
		}
		return "jsonRes";
	}
	
	/**
	 * 给不同用户设置不同的消息分类列表:
	 * @param curUser
	 * @return
	 */
	private void initMessageClassify(User curUser){
		String userType = curUser.getUserType();
		typeList = new ArrayList<MessageClassifyEnum>();
		if(userType!=null && !"".equals(userType)){
			if(userType.equals("1") || userType.equals("11") || userType.equals("12") || userType.equals("13")){
				typeList.add(MessageClassifyEnum.ALL);
				typeList.add(MessageClassifyEnum.ADMIN);
				typeList.add(MessageClassifyEnum.SYSTEM);
				typeList.add(MessageClassifyEnum.ELSE);
			}else if(userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23")){
				try{
				typeList.add(MessageClassifyEnum.ALL);
				typeList.add(MessageClassifyEnum.ADMIN);
				typeList.add(MessageClassifyEnum.SYSTEM);
				typeList.add(MessageClassifyEnum.ELSE);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(userType.equals("4") || userType.equals("41")){
				typeList.add(MessageClassifyEnum.PLANT_SEND);
				typeList.add(MessageClassifyEnum.PLANT_FC);
			}else if(userType.equals("3")){
				typeList.add(MessageClassifyEnum.ALL);
				typeList.add(MessageClassifyEnum.ADMIN);
				typeList.add(MessageClassifyEnum.SITE);
				typeList.add(MessageClassifyEnum.VIP);
			}
		}
	}

	public List<MessageClassifyEnum> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<MessageClassifyEnum> typeList) {
		this.typeList = typeList;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public List<MessageReply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<MessageReply> replyList) {
		this.replyList = replyList;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(String messageIds) {
		this.messageIds = messageIds;
	}

}
