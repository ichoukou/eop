/**
 * 2012-4-17下午01:25:37
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.User;

import org.springframework.dao.DataAccessException;

/**消息模块业务逻辑处理
 * @author wangyong
 * 2012-4-17
 */
public interface MessageService<T> {

	/**
	 * 增加消息并返回消息的主键id
	 * @param entity
	 * @return
	 */
	public boolean addMessage(T entity);
	
	/**
	 * 消息有回复后需要更新时间
	 * @param entity
	 * @throws DataAccessException
	 */
	public boolean updateMessage(T entity) throws DataAccessException;
	
	/**
	 * 根据id获取消息对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getById(Integer id);
	
	/**
	 * 根据消息分类和当前用户获取消息列表
	 * @param curUser
	 * @param classify 消息select分类值
	 * @param pagination 分页对象
	 * @return
	 */
	public List<T> getMessageList(User curUser, String classify, Pagination pagination);
	
	/**
	 * 根据消息分类和当前用户统计消息列表
	 * @param curUser
	 * @param classify 消息select分类值
	 * @return
	 */
	public int countMessageList(User curUser, String classify);
	
	/**
	 * 发送消息公用方法(卖家和网点发消息)
	 * @param user 当前用户对象
	 * @param messageTheme 消息主题
	 * @param messageContent 消息内容
	 * @param receiveUser 接收人
	 * @param messageTyp 消息类型：1、卖家所发；2、网点所发
	 * @return
	 */
	public boolean sendMessage(User user, String messageTheme, String messageContent, List<String> receiveUser, String messageTyp);
	
	/**
	 * 管理员给网点或者卖家发消息
	 * @param user
	 * @param messageTheme
	 * @param messageContent
	 * @param receiveUser 给网点发时存放网点id，给卖家发时存放卖家userCode
	 * @param flag 0代表给网点发；1代表给卖家发
	 * @return
	 */
	public boolean sendMessage(User user, String messageTheme, String messageContent, List<String> receiveUser, int flag);
	
	/**
	 * 发送系统消息:注意给网点或者卖家发系统消息时发给主账号用户id
	 * @param receiveUser 接收人
	 * @param messageTheme
	 * @param messageContent
	 * @return
	 */
	public boolean sendSystemMessage(User receiveUser, String messageTheme, String messageContent);
	
	/**
	 * 发送建议
	 * @param sendUser
	 * @param messageTheme
	 * @param messageContent
	 * @return
	 */
	public boolean sendAdvise(User sendUser, String messageTheme, String messageContent);
	
	/**
	 * 回复消息:回复消息后同时要更新消息时间，设置消息未读
	 * @param curUser 回复人
	 * @param messageId 消息id
	 * @param replyContent 回复内容
	 * @return
	 */
	public boolean replyMessage(User curUser, Integer messageId, String replyContent);
	
	/**
	 * 删除消息
	 * @param messageIds
	 * @param user
	 * @return
	 */
	public boolean deleteMessage(List<Integer> messageIds, User user);
	
	/**
	 * 标记消息
	 * @param messageId
	 * @param curUser
	 * @param messageStatus 0：未读   1：已读
	 * @return
	 */
	public boolean markMessage(Integer messageId, User curUser, Integer messageStatus);
	
	/**
	 * 批量标记
	 * @param messageIds
	 * @param curUser
	 * @param messageStatus 0：未读   1：已读
	 * @return
	 */
	public boolean markMessages(List<Integer> messageIds, User curUser, Integer messageStatus);
	
}
