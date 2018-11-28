/**
 * ErrorConstants.java
 * Created at Oct 8, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.client.util;

import java.io.Serializable;

/**
 * <p>
 * ClassName: ErrorConstants
 * </p>
 * <p>
 * Description: 异常消息类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Oct 8, 2013
 * </p>
 */
public class ErrorConstants implements Serializable {

    /**
     * <p>
     * Field serialVersionUID: 序列号
     * </p>
     */
    private static final long serialVersionUID = -1912144538710611946L;

    /**
     * <p>
     * Field A1: 操作人不能为空
     * </p>
     */
    public static final String A1 = "操作人不能为空";

    /**
     * <p>
     * Field A2: 操作站点不能为空
     * </p>
     */
    public static final String A2 = "操作站点不能为空";

    /**
     * <p>
     * Field A3: 操作配送商不能为空
     * </p>
     */
    public static final String A3 = "操作配送商不能为空";
    /**
     * <p>
     * Field A4: 运单号不正确
     * </p>
     */
    public static final String A4 = "运单号不正确";
    /**
     * <p>
     * Field A5: 配送商业务配置未定义
     * </p>
     */
    public static final String A5 = "配送商业务配置未定义";
    /**
     * <p>
     * Field A6: 订单状态不正确
     * </p>
     */
    public static final String A6 = "订单状态不正确";
    /**
     * <p>
     * Field A7: 此运单不属于操作人所在配送商
     * </p>
     */
    public static final String A7 = "此运单不属于操作人所在配送商";
    /**
     * <p>
     * Field A8: 此运单不属于此站点
     * </p>
     */
    public static final String A8 = "此运单不属于此站点";
    /**
     * <p>
     * Field A9: 上门退货单不能在此操作入站
     * </p>
     */
    public static final String A9 = "上门退货单不能在此操作入站";
    /**
     * <p>
     * Field A10: 转站接收接口异常
     * </p>
     */
    public static final String A10 = "转站接收接口异常";
    /**
     * <p>
     * Field A11: 转站订单必须先打印再入站
     * </p>
     */
    public static final String A11 = "转站订单必须先打印再入站";
    /**
     * <p>
     * Field A12: 入站失败
     * </p>
     */
    public static final String A13 = "入站失败";
    /**
     * <p>
     * Field A13: 操作单号不能为空
     * </p>
     */
    public static final String A14 = "操作单号不能为空";
    /**
     * <p>
     * Field A14: 订单号不正确
     * </p>
     */
    public static final String A15 = "订单号不正确";
    /**
     * <p>
     * Field A15: 配送单号不正确
     * </p>
     */
    public static final String A16 = "配送单号不正确";
    /**
     * <p>
     * Field A16: 此运单状态不能分配配送员
     * </p>
     */
    public static final String A17 = "此运单状态不能分配配送员";
    /**
     * <p>
     * Field A17: 配送员不能为空
     * </p>
     */
    public static final String A18 = "配送员不能为空";
    /**
     * <p>
     * Field A18: 该单已经分配到该配送员
     * </p>
     */
    public static final String A19 = "该单已经分配到该配送员";
    /**
     * <p>
     * Field A19: 订单号对应多个运单
     * </p>
     */
    public static final String A20 = "订单号对应多个运单";
    /**
     * <p>
     * Field A20: 配送单号对应多个运单
     * </p>
     */
    public static final String A21 = "配送单号对应多个运单";
    /**
     * <p>
     * Field A21: 退货单未打印
     * </p>
     */
    public static final String A22 = "退货单未打印";
    /**
     * <p>
     * Field A22: 配送员信息不存在
     * </p>
     */
    public static final String A23 = "配送员信息不存在";
    /**
     * <p>
     * Field A23: 非EDI获取订单不能通过EDI进行提交
     * </p>
     */
    public static final String A24 = "非EDI获取订单不能通过EDI进行提交";
    /**
     * <p>
     * Field A24: 根据订单没有取到站点信息
     * </p>
     */
    public static final String A25 = "根据订单没有取到站点信息";
    /**
     * <p>
     * Field A25: 所选人员与单据分配人员不符
     * </p>
     */
    public static final String A26 = "所选人员与单据分配人员不符";
    /**
     * <p>
     * Field A26: 上门退已提交拒收后不可更改
     * </p>
     */
    public static final String A27 = "上门退已提交拒收后不可更改";
    /**
     * <p>
     * Field A27: 上门退货单不允许选择POS机
     * </p>
     */
    public static final String A28 = "上门退货单不允许选择POS机";
    /**
     * <p>
     * Field A28: 该单受理单已存在，不能提交拒收
     * </p>
     */
    public static final String A29 = "该单受理单已存在，不能提交拒收";
    /**
     * <p>
     * Field A30: 该单已拒收入库不允许提交
     * </p>
     */
    public static final String A30 = "该单已拒收入库不允许提交";
    /**
     * <p>
     * Field A31: 该单已退换货入库不允许提交
     * </p>
     */
    public static final String A31 = "该单已退换货入库不允许提交";
    /**
     * <p>
     * Field A32: 不允许隔天提交归班
     * </p>
     */
    public static final String A32 = "不允许隔天提交归班";
    /**
     * <p>
     * Field S1: 数据库操作异常
     * </p>
     */
    public static final String S1 = "数据库操作异常";
    /**
     * <p>
     * Field S2: 系统程序异常
     * </p>
     */
    public static final String S2 = "系统程序异常";

}
