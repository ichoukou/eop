/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.User;

/**
 * 问题件信息反馈service接口
 * @author wangyong
 * @date 2012-1-30
 */
public interface QuestionaireExchangeService<T> {
	
	/**
     * 根据问题件ids查询交互记录
     * 
     * @param map
     * @return
     */
    public List<T> getListByQuestionaireIds(List<Integer> questionnaireIds);

    /**
     * 添加交互信息
     * @param entry
     * @return
     */
    public boolean add(T entry);
    
    /**
     * 发送反馈信息，向反馈信息表中增加一条记录，并设置该条记录对方用户的问题件为未读状态，己方读取状态为已读。
     * 如果问题件网点通知状态为未通知（1）或者其他（3），则需该更改为已通知（2）；同时网点发送反馈后卖家处理状态为未处理
     * 如果问题件卖家问题件状态为未处理（1）或者其他（3）则改为已处理（2）;
     * @param curUser 
     * @param entity 
     * @return
     */
    public boolean sendExchange(User curUser, T entity);

}
