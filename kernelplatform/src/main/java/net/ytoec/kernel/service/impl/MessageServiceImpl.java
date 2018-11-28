/**
 * 2012-4-17下午02:34:38
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.MessageDao;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.MessageReply;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.MessageReplyService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.MessageUserService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息管理业务逻辑实现
 * @author wangyong
 * 2012-4-17
 */
@Service
@Transactional
public class MessageServiceImpl<T extends Message> implements MessageService<T> {
	
	private static Logger logger=LoggerFactory.getLogger(MessageServiceImpl.class);
	
	@Inject
	private MessageDao<Message> messageDao;
	
	@Inject
	private UserService<User> userService;
	
	@Inject
	private UserThreadService<UserThread> userThreadService;
	
	@Inject
	private MessageUserService<MessageUser> messageUserService;
	
	@Inject
	private MessageReplyService<MessageReply> messageReplyService;

	@Override
	public boolean addMessage(T entity) {
		return messageDao.addMessage(entity);
	}
	
	@Override
	public boolean updateMessage(T entity) throws DataAccessException{
		return messageDao.updateMessage(entity);
	}

	@Override
	public T getById(Integer id) {
		return (T)messageDao.getById(id);
	}

	@Override
	public List<T> getMessageList(User curUser, String classify, Pagination pagination) {
		List<Message> resultList = new ArrayList<Message>();
		Map map = new HashMap();
		map.put("startIndex", pagination.getStartIndex());
		map.put("pageNum", pagination.getPageNum());
		recombinateCondition(map, curUser, classify);
		String userType = curUser.getUserType();
		if(map.containsKey("errorString"))
			return null;
		else{
			List<Message> messageList = new ArrayList<Message>();
			if(userType.equals("1") || userType.equals("11") || userType.equals("12") || userType.equals("13"))
				messageList = messageDao.getSellerMessageListByMap(map);
			else if(userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23"))
				messageList = messageDao.getSiteMessageListByMap(map);
			else if(userType.equals("3"))
				messageList = messageDao.getAdminMessageListByMap(map);
			else if(userType.equals("4") || userType.equals("41"))
				messageList = messageDao.getPlatMessageListByMap(map);
			/**
			 * 统计每条消息对应的回复记录数并设置消息发起人名称、电话和手机
			 */
			if(messageList!=null && messageList.size()>0){
				for(Message msg : messageList){
					int sum = messageReplyService.countReplyListByMap(msg.getId());
					msg.setReplyNum(sum);
					//根据id获取用户
					if(msg.getSendUserId()!=null){
						User user = userService.getUserById(Integer.parseInt(msg.getSendUserId()));
						if (user != null) {
							msg.setSendUserName(user.getUserNameText());
						} else {
							msg.setSendUserName("该用户已经被删除！");
						}
					}
					resultList.add(msg);
				}
			}
		}
		return (List<T>)resultList;
	}
	
	@Override
	public int countMessageList(User curUser, String classify) {
		int result = 0;
		String userType = curUser.getUserType();
		Map map = new HashMap();
		recombinateCondition(map, curUser, classify);
		if(map.containsKey("errorString"))
			return result;
		else{
			if(userType.equals("1") || userType.equals("11") || userType.equals("12") || userType.equals("13"))
				result = messageDao.countSellerMessageListByMap(map);
			else if(userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23"))
				result = messageDao.countSiteMessageListByMap(map);
			else if(userType.equals("3"))
				result = messageDao.countAdminMessageListByMap(map);
			else if(userType.equals("4") || userType.equals("41"))
				result = messageDao.countPlatMessageListByMap(map);
		}
		return result;
	}

	/**
	 * 组装查询条件map
	 * @param map
	 * @param user
	 * @param classify
	 * @return
	 */
	@SuppressWarnings("all")
	private void recombinateCondition(Map map, User user, String classify){
		String userType = user.getUserType();
		if(userType!=null && !"".equals(userType)){
			map.put("classify", classify);
			if(userType.equals("1") || userType.equals("11") || userType.equals("12") || userType.equals("13"))
				recombaniteSellerCondition(map, user, classify);
			else if(userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23"))
				recombinateSiteCondition(map, user, classify);
			else if(userType.equals("3"))
				recombinateAdminCondition(map, user, classify);
			else if(userType.equals("4") || userType.equals("41"))
				recombinatePlatCondition(map, user, classify);
		}
	}
	
	/**
	 * 卖家查看消息封装条件
	 * @param map
	 * @param user
	 * @param classify
	 * @return
	 */
	@SuppressWarnings("all")
	private void recombaniteSellerCondition(Map map, User user, String classify){
		Integer userId = user.getId();
		//子账号获取主账号的id
		if(!user.getUserType().equals("1")){
			userId = user.getParentId();
		}
		map.put("userId", userId);
		if(classify.equals("0")){//卖家所有消息
			if(user.getUserCode()!=null && !("").equals(user.getUserCode())){
				map.put("receiveUser", user.getUserCode());
				map.put("sendUser", user.getUserCode());
			}
			map.put("sendUserId", user.getId());
		}else if(classify.equals("1")){//管理员
			if(user.getUserCode()!=null && !("").equals(user.getUserCode())){
				map.put("receiveUser", user.getUserCode());
			}else{
				logger.error("组装查询条件map中查询管理员出错，卖家用户编码为空!");
				map.put("errorString", "卖家用户编码为空");
			}
		}else if(classify.equals("2")){//系统消息
			if(userId!=null){
				map.put("receiveUser", userId);
			}else{
				logger.error("组装查询条件map中查询管理员出错，卖家用户id为空!");
				map.put("errorString", "卖家用户id为空");
			}
		}else if(classify.equals("3")){//其他消息
			map.put("sendUserId", user.getId());
			if(user.getUserCode()!=null && !("").equals(user.getUserCode())){
				map.put("receiveUser", user.getUserCode());
			}
		}
	}
	
	/**
	 * 网点查看消息封装条件
	 * @param map
	 * @param user
	 * @param classity
	 */
	@SuppressWarnings("all")
	private void recombinateSiteCondition(Map map, User user, String classify){
		Integer userId = user.getId();
		//子账号获取主账号的id
		if(!user.getUserType().equals("2")){
			userId = user.getParentId();
		}
		map.put("userId", userId);
		if(classify.equals("0")){//所有消息
			map.put("receiveUser", userId);
			map.put("sendUser", userId);
		}else if(classify.equals("1")){//管理员
			map.put("receiveUser", userId);
		}else if(classify.equals("2")){//系统消息
			map.put("receiveUser", userId);
		}else if(classify.equals("3")){//其他消息
			map.put("receiveUser", userId);
			map.put("sendUser", userId);
		}
	}
	
	/**
	 * 管理员查看消息封装条件
	 * @param map
	 * @param user
	 * @param classify
	 */
	@SuppressWarnings("all")
	private void recombinateAdminCondition(Map map, User user, String classify){
		Integer userId = user.getId();
		//子账号获取主账号的id
		if(!user.getUserType().equals("3")){
			userId = user.getParentId();
		}
		map.put("userId", userId);
		if(classify.equals("0")){//所有消息
			map.put("receiveUser", userId);
			map.put("sendUser", userId);
		}else if(classify.equals("1")){//管理员
			map.put("sendUser", userId);
		}else if(classify.equals("2")){//网点
			map.put("receiveUser", userId);
		}else if(classify.equals("3")){//卖家
			map.put("receiveUser", userId);
		}
	}
	
	/**
	 * 平台用户查看消息封装条件
	 * @param map
	 * @param user
	 * @param classify
	 */
	@SuppressWarnings("all")
	private void recombinatePlatCondition(Map map, User user, String classify){
		Integer id = user.getId();
		/**
		 * 首先获取平台用户及其分仓的用户id
		 */
		List<Integer> userId = new ArrayList<Integer>();
		if(!user.getUserType().equals("4")){
			id = user.getParentId();
		}
		userId.add(id);
		List<User> cfUserList = userService.pingTaiSelect(user, 1, "1");
		for(User u : cfUserList){
			if(u.getUserType().equals("1"))
				userId.add(u.getId());
		}
		/**
		 * 平台用户的分仓用户的用户编码
		 */
		List<String> userCode = new ArrayList<String>();
		for(User u : cfUserList){
			if(u.getUserType().equals("1"))
				if(!StringUtils.isEmpty(u.getUserCode()))
					userCode.add(u.getUserCode());
		}
		map.put("userId", userId);
		if(classify.equals("0")){//平台用户自己发的消息
			map.put("sendUser", id);
		}else if(classify.equals("1")){//分仓所发信息
			map.put("sendUser", userCode);
		}
	}

	@Override
	public boolean sendMessage(User curUser, String messageTheme, String messageContent, List<String> receiveUser, String messageType) {
		//卖家发消息：卖家向网点发送消息：接收人取该卖家的网点的主账号id     
		if(messageType.equals("1")){
			/**
			 * 获取卖家的网点主账号:承包区也作为一个独立的网点出现
			 */
			List<User> siteUser = userService.searchUsersBySiteAndUserType(curUser.getSite(), "2");
			if(siteUser!=null && !siteUser.isEmpty()){
				Message message = new Message();
				message.setReceiveUser(siteUser.get(0).getId().toString());
				message.setMessageTheme(messageTheme);
				message.setMessageContent(messageContent);
				message.setSendUserId(curUser.getId().toString());
				message.setSendUser(curUser.getUserCode());
				message.setSendWay("1");
				message.setMessageType(messageType);
				boolean b = this.addMessage((T)message);
				//添加消息后，还需要向消息用户表中插入记录，同时将该条消息作为第一条回复数据插入到回复表
				if(b){
					MessageReply messageReply = new MessageReply();
					messageReply.setMessageId(message.getId());
					messageReply.setReplyContent(messageContent);
					messageReply.setReplyUser(curUser.getId());
					messageReplyService.addMessageReply(messageReply);
					
					MessageUser mu = new MessageUser();
					mu.setMessageId(message.getId());
					mu.setMessageStatus(0);//设置未读
					mu.setUserId(siteUser.get(0).getId());
					if(messageUserService.add(mu)){
						/**
						 * 最后必须加入一条当前自己的记录
						 */
						MessageUser muSelf = new MessageUser();
						muSelf.setMessageId(message.getId());
						muSelf.setMessageStatus(1);//设置已读
						if(!curUser.getUserType().equals("1"))
							muSelf.setUserId(curUser.getParentId());
						else
							muSelf.setUserId(curUser.getId());
						return messageUserService.add(muSelf);
					}
					else
						logger.error("当前用户 "+curUser.getUserName()+" 发送消息失败");
				}else{
					logger.error("当前用户 "+curUser.getUserName()+" 发送消息失败");
				}
			}
		}else if(messageType.equals("2")){//网点发消息
			/**
			 * 网点或者承包区向自己的直客发送消息
			 */
			if(receiveUser!=null && !receiveUser.isEmpty()){
				for(String userCode : receiveUser){
					Message message = new Message();
					message.setMessageContent(messageContent);
					message.setMessageTheme(messageTheme);
					message.setMessageType(messageType);
					message.setSendUserId(curUser.getId().toString());
					if(!curUser.getUserType().equals("2")){//子账号取主账号的id
						message.setSendUser(curUser.getParentId().toString());
					}else
						message.setSendUser(curUser.getId().toString());
					message.setReceiveUser(userCode);
					message.setSendWay("1");
					boolean b = this.addMessage((T)message);
					/*
					 * 添加消息后，还需要向消息用户表中插入记录，同时将该条消息作为第一条回复数据插入到回复表
					 * 这里根据UserCode取用户，依每个用户id创建一个MessageUser
					 */
					if(b){
						MessageReply messageReply = new MessageReply();
						messageReply.setMessageId(message.getId());
						messageReply.setReplyContent(messageContent);
						messageReply.setReplyUser(curUser.getId());
						messageReplyService.addMessageReply(messageReply);
						
						List<User> userList = userService.searchUsersByCodeTypeState(userCode, "1", "1");
						if(userList!=null && !userList.isEmpty()){
							for(User u : userList){
								MessageUser mu = new MessageUser();
								mu.setMessageId(message.getId());
								mu.setMessageStatus(0);//设置未读
								mu.setUserId(u.getId());
								if(messageUserService.add(mu)){
								}
								else{
									logger.error("网点"+curUser.getUserName()+"给用户编码为"+userCode+"发送消息时插入messageUser信息时出错");
								}
							}
							/**
							 * 最后必须加入一条当前自己的记录
							 */
							MessageUser muSelf = new MessageUser();
							muSelf.setMessageId(message.getId());
							muSelf.setMessageStatus(1);//设置已读
							if(!curUser.getUserType().equals("2"))
								muSelf.setUserId(curUser.getParentId());
							else
								muSelf.setUserId(curUser.getId());
							messageUserService.add(muSelf);
						}
					}else{
						logger.error("网点"+curUser.getUserName()+"给用户编码为"+userCode+"发送消息失败");
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean sendMessage(User user, String messageTheme, String messageContent, List<String> receiveUser, int flag) {
		if(flag==0){//管理员给网点发消息
			if(receiveUser!=null){
				for(String receive : receiveUser){
					Message message = new Message();
					message.setMessageContent(messageContent);
					message.setMessageTheme(messageTheme);
					message.setMessageType("3");
					message.setSendWay("1");
					message.setSendUser(user.getId().toString());
					message.setSendUserId(user.getId().toString());
					message.setReceiveUser(receive);
					boolean b = this.addMessage((T)message);
					if(b){
						MessageReply messageReply = new MessageReply();
						messageReply.setMessageId(message.getId());
						messageReply.setReplyContent(messageContent);
						messageReply.setReplyUser(user.getId());
						messageReplyService.addMessageReply(messageReply);
						
						MessageUser mu = new MessageUser();
						mu.setMessageId(message.getId());
						mu.setMessageStatus(0);//设置未读
						mu.setUserId(Integer.parseInt(receive));
						if(messageUserService.add(mu)){
							/**
							 * 最后必须加入一条当前自己的记录
							 */
							MessageUser muSelf = new MessageUser();
							muSelf.setMessageId(message.getId());
							muSelf.setMessageStatus(1);//设置已读
							muSelf.setUserId(user.getId());
							messageUserService.add(muSelf);
						}
						else
							logger.error("当前用户 "+user.getUserName()+" 给网点id"+receive+"发送消息失败");
					}
				}
				return true;
			}
		}else if(flag==1){//管理员给卖家发消息
			if(receiveUser!=null){
				for(String userCode : receiveUser){
					Message message = new Message();
					message.setMessageContent(messageContent);
					message.setMessageTheme(messageTheme);
					message.setMessageType("3");
					message.setSendWay("1");
					message.setSendUser(user.getId().toString());
					message.setSendUserId(user.getId().toString());
					message.setReceiveUser(userCode);
					boolean b = this.addMessage((T)message);
					/*
					 * 添加消息后，还需要向消息用户表中插入记录
					 * 这里根据UserCode取用户，依每个用户id创建一个MessageUser
					 */
					if(b){
						MessageReply messageReply = new MessageReply();
						messageReply.setMessageId(message.getId());
						messageReply.setReplyContent(messageContent);
						messageReply.setReplyUser(user.getId());
						messageReplyService.addMessageReply(messageReply);
						
						List<User> userList = userService.searchUsersByCodeTypeState(userCode, "1", "1");
						if(userList!=null && !userList.isEmpty()){
							for(User u : userList){
								MessageUser mu = new MessageUser();
								mu.setMessageId(message.getId());
								mu.setMessageStatus(0);//设置未读
								mu.setUserId(u.getId());
								if(messageUserService.add(mu)){}
								else{
									logger.error("管理员"+user.getUserName()+"给用户编码为"+userCode+"发送消息时插入messageUser信息时出错");
								}
							}
							/**
							 * 最后必须加入一条当前自己的记录
							 */
							MessageUser muSelf = new MessageUser();
							muSelf.setMessageId(message.getId());
							muSelf.setMessageStatus(1);//设置已读
							muSelf.setUserId(user.getId());
							messageUserService.add(muSelf);
						}
					}else{
						logger.error("管理员"+user.getUserName()+"给用户编码为"+userCode+"发送消息失败");
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean sendSystemMessage(User receiveUser, String messageTheme, String messageContent){
		if(receiveUser!=null){
			Integer receiveUserId = receiveUser.getId();
			if(receiveUser.getUserType().equals("11") || receiveUser.getUserType().equals("12") || receiveUser.getUserType().equals("13") || receiveUser.getUserType().equals("21") || receiveUser.getUserType().equals("22") || receiveUser.getUserType().equals("23"))
				receiveUserId = receiveUser.getParentId();
			Message message = new Message();
			message.setMessageContent(messageContent);
			message.setMessageTheme(messageTheme);
			message.setMessageType("4");
			message.setReceiveUser(receiveUserId.toString());
			message.setSendWay("1");
			/**
			 * 系统消息默认是管理员所发
			 */
			List<User> adminList = userService.getUserListByUserType("3");
			message.setSendUserId(adminList.get(0).getId().toString());
			boolean b = this.addMessage((T)message);
			if(b){
				MessageReply messageReply = new MessageReply();
				messageReply.setMessageId(message.getId());
				messageReply.setReplyContent(messageContent);
				messageReply.setReplyUser(adminList.get(0).getId());
				messageReplyService.addMessageReply(messageReply);
				
				MessageUser mu = new MessageUser();
				mu.setMessageId(message.getId());
				mu.setMessageStatus(0);//设置未读
				mu.setUserId(receiveUserId);
				if(messageUserService.add(mu)){
					return true;
				}else
					logger.error("发送系统消息失败,接收人id:"+receiveUserId);
			}
		}
		return false;
	}
	
	@Override
	public boolean sendAdvise(User sendUser, String messageTheme, String messageContent){
		Message message = new Message();
		Integer userId = sendUser.getId();
		if(sendUser.getUserType().equals("1") || sendUser.getUserType().equals("11") || sendUser.getUserType().equals("12") || sendUser.getUserType().equals("13")){
			message.setMessageType("1");
			message.setSendUser(sendUser.getUserCode());
			if(!sendUser.getUserType().equals("1"))
				userId = sendUser.getParentId();
		}else if(sendUser.getUserType().equals("2") || sendUser.getUserType().equals("21") || sendUser.getUserType().equals("22") || sendUser.getUserType().equals("23")){
			message.setMessageType("2");
			if(sendUser.getUserType().equals("2")){
				message.setSendUser(sendUser.getId().toString());
			}else{
				userId = sendUser.getParentId();
				message.setSendUser(sendUser.getParentId().toString());
			}
		}else if(sendUser.getUserType().equals("4") || sendUser.getUserType().equals("41")){
			message.setMessageType("5");
			if(sendUser.getUserType().equals("4")){
				message.setSendUser(sendUser.getId().toString());
			}
			else{
				message.setSendUser(sendUser.getParentId().toString());
				userId = sendUser.getParentId();
			}
		}else
			return false;
		message.setMessageContent(messageContent);
		message.setMessageTheme(messageTheme);
		message.setSendUserId(sendUser.getId().toString());
		/** 获取管理员用户ID */
		List<User> receiveUser = userService.getUserListByUserType("3");
		if(receiveUser!=null && !receiveUser.isEmpty()){
			message.setReceiveUser(receiveUser.get(0).getId().toString());
			boolean b = this.addMessage((T)message);
			if(b){
				MessageReply messageReply = new MessageReply();
				messageReply.setMessageId(message.getId());
				messageReply.setReplyContent(messageContent);
				messageReply.setReplyUser(sendUser.getId());
				messageReplyService.addMessageReply(messageReply);
				
				MessageUser mu = new MessageUser();
				mu.setMessageId(message.getId());
				mu.setMessageStatus(0);
				mu.setUserId(receiveUser.get(0).getId());
				if(messageUserService.add(mu)){
					/**
					 * 最后必须加入一条当前自己的记录
					 */
					MessageUser muSelf = new MessageUser();
					muSelf.setMessageId(message.getId());
					muSelf.setMessageStatus(1);//设置已读
					muSelf.setUserId(userId);
					messageUserService.add(muSelf);
					return true;
				}
				else
					logger.error("发送建议出错");
			}
		}else{
			logger.error("发送建议时没有找到管理员用户");
		}
		return false;
	}

	@Override
	public boolean replyMessage(User curUser, Integer messageId, String replyContent){
		MessageReply messageReply = new MessageReply();
		messageReply.setMessageId(messageId);
		messageReply.setReplyContent(replyContent);
		messageReply.setReplyUser(curUser.getId());
		boolean flag = messageReplyService.addMessageReply(messageReply);
		if(flag){
			Message message = this.getById(messageId);
			try{
				this.updateMessage((T)message);
				/**
				 * 同时要更新messageUser中消息为未读
				 */
				List<MessageUser> messageUserList = messageUserService.getByMessageId(messageId);
				if(messageUserList!=null && !messageUserList.isEmpty()){
					for(MessageUser messageUser : messageUserList){
						if(!curUser.getId().equals(messageUser.getUserId())){
							messageUserService.markMessage(messageId, messageUser.getUserId(),0);
						}
					}
				}
			}catch(Exception e){
				flag = false;
			}
		}
		return flag;
	}
	
	@Override
	public boolean deleteMessage(List<Integer> messageIds, User user){
		if(messageIds!=null){
			Integer userId = null;
			if(user.getUserType().equals("1") || user.getUserType().equals("2") || user.getUserType().equals("3") || user.getUserType().equals("4"))
				userId = user.getId();
			else
				userId = user.getParentId();
			for(Integer messageId : messageIds){
				/**
				 * 删除问题件只需要删除问题件用户关联表中的记录
				 */
				try{
					boolean f = messageUserService.deleteByUserAndMsgId(messageId, userId);
				}catch(Exception e){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean markMessage(Integer messageId, User curUser, Integer messageStatus) {
		try{
			Integer userId = curUser.getId();
			if(curUser.getUserType().equals("11") || curUser.getUserType().equals("12") || curUser.getUserType().equals("13")){
				userId = curUser.getParentId();
			}else if(curUser.getUserType().equals("21") || curUser.getUserType().equals("22") || curUser.getUserType().equals("23")){
				userId = curUser.getParentId();
			}else if(curUser.getUserType().equals("41"))
				userId = curUser.getParentId();
			return messageUserService.markMessage(messageId, userId, messageStatus);
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean markMessages(List<Integer> messageIds, User curUser, Integer messageStatus){
		try{
			Integer userId = curUser.getId();
			if(curUser.getUserType().equals("11") || curUser.getUserType().equals("12") || curUser.getUserType().equals("13")){
				userId = curUser.getParentId();
			}else if(curUser.getUserType().equals("21") || curUser.getUserType().equals("22") || curUser.getUserType().equals("23")){
				userId = curUser.getParentId();
			}else if(curUser.getUserType().equals("41"))
				userId = curUser.getParentId();
			for(Integer messageId : messageIds){
				messageUserService.markMessage(messageId, userId, messageStatus);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
