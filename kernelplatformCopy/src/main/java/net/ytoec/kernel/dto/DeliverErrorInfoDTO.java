/**
 * DeliverErrorInfoDTO.java
 * Created at Oct 8, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.dto;

import java.io.Serializable;

/**
 * <p>
 * ClassName: DeliverErrorInfoDTO
 * </p>
 * <p>
 * Description: 提交配送结果信息错误消息类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Oct 8, 2013
 * </p>
 */
public class DeliverErrorInfoDTO implements Serializable {
    /**
     * <p>
     * Field serialVersionUID: 序列号
     * </p>
     */
    private static final long serialVersionUID = -4063686572106994206L;
    /**
     * <p>
     * Field OrderNO: 订单号
     * </p>
     */
    public String OrderNO;

    /**
     * <p>
     * Field ErrorNO: 错误编号
     * </p>
     */
    public String ErrorNO;

    /**
     * <p>
     * Description: 订单号
     * </p>
     * 
     * @return the orderNO
     */
    public String getOrderNO() {
        return this.OrderNO;
    }

    /**
     * <p>
     * Description: 订单号
     * </p>
     * 
     * @param orderNO the orderNO to set
     */
    public void setOrderNO(String orderNO) {
        this.OrderNO = orderNO;
    }

    /**
     * <p>
     * Description: 错误编号
     * </p>
     * 
     * @return the errorNO
     */
    public String getErrorNO() {
        return this.ErrorNO;
    }

    /**
     * <p>
     * Description: 错误编号
     * </p>
     * 
     * @param errorNO the errorNO to set
     */
    public void setErrorNO(String errorNO) {
        this.ErrorNO = errorNO;
    }

}
