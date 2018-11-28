/**
 * DeliveryInfoDTO.java
 * Created at Oct 8, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.dto;

import java.io.Serializable;
import java.util.Date;
import net.ytoec.kernel.util.DateUtil;

/**
 * <p>
 * ClassName: DeliveryInfoDTO
 * </p>
 * <p>
 * Description: 配送结果数据类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Oct 8, 2013
 * </p>
 */
public class DeliveryInfoDTO implements Serializable {

    /**
     * <p>
     * Field serialVersionUID: 序列号
     * </p>
     */
    private static final long serialVersionUID = -5880476641568791360L;

    /**
     * <p>
     * Field orderNo: 定单号
     * </p>
     */
    public String orderNo;

    /**
     * <p>
     * Field operatorType: 操作类型(1=入站，2=分配配送员，3=妥投，4=滞留，5=拒收)
     * </p>
     */
    public int operatorType;

    /**
     * <p>
     * Field orderType: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     */
    public int orderType;

    /**
     * <p>
     * Field operateTime: 操作时间
     * </p>
     */
    public String operateTime;

    /**
     * <p>
     * Field deliveryMan: 配送员
     * </p>
     */
    public String deliveryMan;

    /**
     * <p>
     * Field deliverManCode: 配送员编号
     * </p>
     */
    public String deliverManCode;

    /**
     * <p>
     * Field sendMsg: 是否发短信
     * </p>
     */
    public int sendMsg;

    /**
     * <p>
     * Field reason: 滞留或拒收原因
     * </p>
     */
    public String reason;

    /**
     * <p>
     * Field paymentType: 支付方式
     * </p>
     */
    public String paymentType;

    /**
     * <p>
     * Field comments: 具体描述
     * </p>
     */
    public String comments;

    public DeliveryInfoDTO() {
        this.sendMsg = 0;
        this.orderType=0;
        this.operatorType = 1;
        this.operateTime = DateUtil.format(new Date(), "yyyy-MM-dd");

    }

    /**
     * <p>
     * Description: 定单号
     * </p>
     * 
     * @return the orderNo
     */
    public String getOrderNo() {
        return this.orderNo;
    }

    /**
     * <p>
     * Description: 定单号
     * </p>
     * 
     * @param orderNo the orderNo to set
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * <p>
     * Description: 操作类型(1=入站，2=分配配送员，3=妥投，4=滞留，5=拒收)
     * </p>
     * 
     * @return the operatorType
     */
    public int getOperatorType() {
        return this.operatorType;
    }

    /**
     * <p>
     * Description: 操作类型(1=入站，2=分配配送员，3=妥投，4=滞留，5=拒收)
     * </p>
     * 
     * @param operatorType the operatorType to set
     */
    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
    }

    /**
     * <p>
     * Description: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     * 
     * @return the orderType
     */
    public int getOrderType() {
        return this.orderType;
    }

    /**
     * <p>
     * Description: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     * 
     * @param orderType the orderType to set
     */
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    /**
     * <p>
     * Description: 操作时间
     * </p>
     * 
     * @return the operateTime
     */
    public String getOperateTime() {
        return this.operateTime;
    }

    /**
     * <p>
     * Description: 操作时间
     * </p>
     * 
     * @param operateTime the operateTime to set
     */
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * <p>
     * Description: 配送员
     * </p>
     * 
     * @return the deliveryMan
     */
    public String getDeliveryMan() {
        return this.deliveryMan;
    }

    /**
     * <p>
     * Description: 配送员
     * </p>
     * 
     * @param deliveryMan the deliveryMan to set
     */
    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    /**
     * <p>
     * Description: 配送员编号
     * </p>
     * 
     * @return the deliverManCode
     */
    public String getDeliverManCode() {
        return this.deliverManCode;
    }

    /**
     * <p>
     * Description: 配送员编号
     * </p>
     * 
     * @param deliverManCode the deliverManCode to set
     */
    public void setDeliverManCode(String deliverManCode) {
        this.deliverManCode = deliverManCode;
    }

    /**
     * <p>
     * Description: 是否发短信
     * </p>
     * 
     * @return the sendMsg
     */
    public int getSendMsg() {
        return this.sendMsg;
    }

    /**
     * <p>
     * Description: 是否发短信
     * </p>
     * 
     * @param sendMsg the sendMsg to set
     */
    public void setSendMsg(int sendMsg) {
        this.sendMsg = sendMsg;
    }

    /**
     * <p>
     * Description: 滞留或拒收原因
     * </p>
     * 
     * @return the reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * <p>
     * Description: 滞留或拒收原因
     * </p>
     * 
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * <p>
     * Description: 支付方式
     * </p>
     * 
     * @return the paymentType
     */
    public String getPaymentType() {
        return this.paymentType;
    }

    /**
     * <p>
     * Description: 支付方式
     * </p>
     * 
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * <p>
     * Description: 具体描述
     * </p>
     * 
     * @return the comments
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * <p>
     * Description: 具体描述
     * </p>
     * 
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

}
