/**
 * 2012-5-7上午10:19:46
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.MessageReplyDao;
import net.ytoec.kernel.dataobject.MessageReply;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MessageReplyService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 消息业务逻辑处理实现
 * @author wangyong
 * 2012-5-7
 */
@Service
@Transactional
public class MessageReplyServiceImpl<T extends MessageReply> implements MessageReplyService<T>{
	
	private static Logger logger = LoggerFactory.getLogger(MessageReplyServiceImpl.class);
	
	@Inject
	private MessageReplyDao<T> messageReplyDao;

	@Inject
	private UserService<User> userService;
	
	@Override
	public boolean addMessageReply(T entity) {
		boolean flag = false;
		if(entity.getMessageId()==null){
			logger.error("新增回复消息时消息id为空");
			return flag;
		}
		flag = messageReplyDao.addMessageReply(entity);
		return flag;
	}

	@Override
	public List<T> getReplyListByMap(Integer messageId, Pagination pagination) {
		Map map = new HashMap();
		if(messageId!=null){
			map.put("messageId", messageId);
		}else
			return null;
		if(pagination!=null){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		List<MessageReply> replyList = (List<MessageReply>)messageReplyDao.getReplyListByMap(map);
		if(replyList!=null){
			List<MessageReply> resultList = new ArrayList<MessageReply>();
			for(MessageReply rm : replyList){
				/**
				 * 设置回复人名称、联系方式
				 */
				if(rm.getReplyUser()!=null){
					User user = userService.getUserById(rm.getReplyUser());
					if(StringUtils.isNotEmpty(user.getUserNameText()))	rm.setReplyUserName(user.getUserNameText());
					if(StringUtils.isNotEmpty(user.getMobilePhone()))	rm.setReplyUserMobile(user.getMobilePhone());
					if(StringUtils.isNotEmpty(user.getTelePhone()))	rm.setReplyUserPhone(user.getTelePhone());
					
					//这里设置回复消息所属类型：表示当期回复属于那种用户回复的（1:网点；2其他），用于页面上颜色区分
					if(user.getUserType().equals("2") || user.getUserType().equals("21") || user.getUserType().equals("22") || user.getUserType().equals("23")){
						rm.setReplyType("1");
					}else
						rm.setReplyType("2");
				}
				resultList.add(rm);
			}
			return (List<T>)resultList;
		}
		return null;
	}

	@Override
	public int countReplyListByMap(Integer messageId) {
		Map map = new HashMap();
		if(messageId!=null){
			map.put("messageId", messageId);
		}else
			return 0;
		return messageReplyDao.countReplyListByMap(map);
	}

}
