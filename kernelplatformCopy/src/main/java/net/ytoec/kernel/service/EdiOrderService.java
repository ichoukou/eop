/**
 * EdiOrderService.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.SendTaskToTB;

/**
 * <p>
 * ClassName: EdiOrderService
 * </p>
 * <p>
 * Description: 凡客对接业务接口
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
public interface EdiOrderService {

    /**
     * <p>
     * Description: 获取订单
     * </p>
     * 
     * @param lcid 对接编号
     * @return 订单数据JSON格式字符串
     */
    public String getOrders(String lcid);

    /**
     * <p>
     * Description: 获取订单状态更新结果
     * </p>
     * 
     * @param lcid 对接编号
     * @param orderNos 订单编号集合
     * @return 是否更新
     */
    public boolean successOrders(String lcid, List<String> orderNos);

    /**
     * <p>
     * Description: 提交配送结果
     * </p>
     * 
     * @param lcid 对接编号
     * @param deliverInfo 配送结果json字符串+RSA数字签名
     * @return 提交结果（null 值提交成功,否则提交失败）
     */
    public String commitDeliverInfo(String lcid, String deliverInfo);

    /**
     * <p>
     * Description: 新增一条发送凡客的任务
     * </p>
     * 
     * @param sendTask 新任务
     * @return boolean
     */
    public boolean saveSendTaskToVancl(SendTaskToTB sendTask);

    /**
     * <p>
     * Description: 发送至凡客EDI成功将taskToTB删除，同时写入TASK_LOG表中
     * </p>
     * 
     * @param sendTask 当前操作任务
     * @return boolean
     */
    public boolean finishedSendTaskToVancl(SendTaskToTB sendTask);

    /**
     * <p>
     * Description: 更新taskToTB表，最后一 次发送时间，通常是执行任务失败时需要更新操作
     * </p>
     * 
     * @param sendTask 当前操作任务
     * @return boolean
     */
    public boolean updateSendTaskLastSendTime(SendTaskToTB sendTask);

    /**
     * <p>
     * Description: 批量添加发送至凡客任务记录
     * </p>
     * 
     * @param sendTasks 任务集合
     * @return boolean
     */
    public boolean batchAddSendTaskToVancl(List<SendTaskToTB> sendTasks);

    /**
     * <p>
     * Description: 发送任务
     * </p>
     * 
     * @param sendTask 当前任务
     * @return boolean
     */
    public boolean sendTaskToVancl(SendTaskToTB sendTask);

    /**
     * <p>
     * Description: 按照时间倒序，获取SendTask
     * </p>
     * 
     * @return
     */
    public List<SendTaskToTB> getSendTask();
    
    /**
     * <p>Description: 发送任务</p>
     * @return boolean
     */
    public boolean send();
}
