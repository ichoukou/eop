/**
 * OrderJsonDTO.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * ClassName: OrderJsonDTO
 * </p>
 * <p>
 * Description: 订单JSON格式数据类 (EDI系统WebService Json 字符串key采用首字符大写，此类作为JSON格式交互类，
 * 所有字段均大写开头匹配JSON数据KEY值,所有字段访问修饰为Public)
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
public class OrderJsonDTO implements Serializable {

    /**
     * <p>
     * Field serialVersionUID: 序列编号
     * </p>
     */
    private static final long serialVersionUID = 7953477207783412823L;

    /**
     * <p>
     * Field orderid: 订单ID
     * </p>
     */
    private String orderid;

    /**
     * <p>
     * Field OrderNO: 订单号
     * </p>
     */
    public String OrderNO;

    /**
     * <p>
     * Field CustomerOrder: 商家单号
     * </p>
     */
    public String CustomerOrder;

    /**
     * <p>
     * Field DeliverCode: 发货单号
     * </p>
     */
    public String DeliverCode;

    /**
     * <p>
     * Field OrderType: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     */
    public String OrderType;

    /**
     * <p>
     * Field WaybillSourse: 订单来源（VANCL=0、VJIA=1、如风达=2）
     * </p>
     */
    public String WaybillSourse;

    /**
     * <p>
     * Field MerchantName: 商家名称
     * </p>
     */
    public String MerchantName;

    /**
     * <p>
     * Field Weight: 订单重量
     * </p>
     */
    public String Weight;

    /**
     * <p>
     * Field UserName: 收件人姓名
     * </p>
     */
    public String UserName;

    /**
     * <p>
     * Field PostalCode: 收件人邮编
     * </p>
     */
    public String PostalCode;

    /**
     * <p>
     * Field Province: 收件人所在省
     * </p>
     */
    public String Province;

    /**
     * <p>
     * Field City: 收件市
     * </p>
     */
    public String City;

    /**
     * <p>
     * Field Area: 收件人所在区
     * </p>
     */
    public String Area;

    /**
     * <p>
     * Field Address: 收件人详细地址
     * </p>
     */
    public String Address;

    /**
     * <p>
     * Field CallBefore: 是否送前提示
     * </p>
     */
    public String CallBefore;

    /**
     * <p>
     * Field OutTime: 出库时间
     * </p>
     */
    public String OutTime;

    /**
     * <p>
     * Field NeedFund: 应收金额
     * </p>
     */
    public String NeedFund;

    /**
     * <p>
     * Field BackFund: 应退金额
     * </p>
     */
    public String BackFund;

    /**
     * <p>
     * Field WareHouseName: 仓库名称
     * </p>
     */
    public String WareHouseName;

    /**
     * <p>
     * Field PaymentType: 付款方式（现金1,POS机2）
     * </p>
     */
    public String PaymentType;

    /**
     * <p>
     * Field MobilePhone: 联系电话
     * </p>
     */
    public String MobilePhone;

    /**
     * <p>
     * Field ReceiveTel: 收件人座机
     * </p>
     */
    public String ReceiveTel;

    /**
     * <p>
     * Field Remark: 备注
     * </p>
     */
    public String Remark;

    /**
     * <p>
     * Field Goods: 商品明细
     * </p>
     */
    public List<OrderGoodDTO> Goods;

    /**
     * <p>
     * Description: 收件人座机
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
     * Description: 商家单号
     * </p>
     * 
     * @return the customerOrder
     */
    public String getCustomerOrder() {
        return this.CustomerOrder;
    }

    /**
     * <p>
     * Description: 商家单号
     * </p>
     * 
     * @param customerOrder the customerOrder to set
     */
    public void setCustomerOrder(String customerOrder) {
        this.CustomerOrder = customerOrder;
    }

    /**
     * <p>
     * Description: 发货单号
     * </p>
     * 
     * @return the deliverCode
     */
    public String getDeliverCode() {
        return this.DeliverCode;
    }

    /**
     * <p>
     * Description: 发货单号
     * </p>
     * 
     * @param deliverCode the deliverCode to set
     */
    public void setDeliverCode(String deliverCode) {
        this.DeliverCode = deliverCode;
    }

    /**
     * <p>
     * Description: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     * 
     * @return the orderType
     */
    public String getOrderType() {
        return this.OrderType;
    }

    /**
     * <p>
     * Description: 订单类型（普通=0、换货=1、退货=2）
     * </p>
     * 
     * @param orderType the orderType to set
     */
    public void setOrderType(String orderType) {
        this.OrderType = orderType;
    }

    /**
     * <p>
     * Description: 订单来源（VANCL=0、VJIA=1、如风达=2）
     * </p>
     * 
     * @return the waybillSourse
     */
    public String getWaybillSourse() {
        return this.WaybillSourse;
    }

    /**
     * <p>
     * Description: 订单来源（VANCL=0、VJIA=1、如风达=2）
     * </p>
     * 
     * @param waybillSourse the waybillSourse to set
     */
    public void setWaybillSourse(String waybillSourse) {
        this.WaybillSourse = waybillSourse;
    }

    /**
     * <p>
     * Description: 商家名称
     * </p>
     * 
     * @return the merchantName
     */
    public String getMerchantName() {
        return this.MerchantName;
    }

    /**
     * <p>
     * Description: 商家名称
     * </p>
     * 
     * @param merchantName the merchantName to set
     */
    public void setMerchantName(String merchantName) {
        this.MerchantName = merchantName;
    }

    /**
     * <p>
     * Description: 订单重量
     * </p>
     * 
     * @return the weight
     */
    public String getWeight() {
        return this.Weight;
    }

    /**
     * <p>
     * Description: 订单重量
     * </p>
     * 
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.Weight = weight;
    }

    /**
     * <p>
     * Description: 收件人姓名
     * </p>
     * 
     * @return the userName
     */
    public String getUserName() {
        return this.UserName;
    }

    /**
     * <p>
     * Description: 收件人姓名
     * </p>
     * 
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.UserName = userName;
    }

    /**
     * <p>
     * Description: 收件人邮编
     * </p>
     * 
     * @return the postalCode
     */
    public String getPostalCode() {
        return this.PostalCode;
    }

    /**
     * <p>
     * Description: 收件人邮编
     * </p>
     * 
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.PostalCode = postalCode;
    }

    /**
     * <p>
     * Description: 收件人所在省
     * </p>
     * 
     * @return the province
     */
    public String getProvince() {
        return this.Province;
    }

    /**
     * <p>
     * Description: 收件人所在省
     * </p>
     * 
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.Province = province;
    }

    /**
     * <p>
     * Description: 收件市
     * </p>
     * 
     * @return the city
     */
    public String getCity() {
        return this.City;
    }

    /**
     * <p>
     * Description: 收件市
     * </p>
     * 
     * @param city the city to set
     */
    public void setCity(String city) {
        this.City = city;
    }

    /**
     * <p>
     * Description: 收件人所在区
     * </p>
     * 
     * @return the area
     */
    public String getArea() {
        return this.Area;
    }

    /**
     * <p>
     * Description: 收件人所在区
     * </p>
     * 
     * @param area the area to set
     */
    public void setArea(String area) {
        this.Area = area;
    }

    /**
     * <p>
     * Description: 收件人详细地址
     * </p>
     * 
     * @return the address
     */
    public String getAddress() {
        return this.Address;
    }

    /**
     * <p>
     * Description: 收件人详细地址
     * </p>
     * 
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.Address = address;
    }

    /**
     * <p>
     * Description: 是否送前提示
     * </p>
     * 
     * @return the callBefore
     */
    public String getCallBefore() {
        return this.CallBefore;
    }

    /**
     * <p>
     * Description: 是否送前提示
     * </p>
     * 
     * @param callBefore the callBefore to set
     */
    public void setCallBefore(String callBefore) {
        this.CallBefore = callBefore;
    }

    /**
     * <p>
     * Description: 出库时间
     * </p>
     * 
     * @return the outTime
     */
    public String getOutTime() {
        return this.OutTime;
    }

    /**
     * <p>
     * Description: 出库时间
     * </p>
     * 
     * @param outTime the outTime to set
     */
    public void setOutTime(String outTime) {
        this.OutTime = outTime;
    }

    /**
     * <p>
     * Description: 应收金额
     * </p>
     * 
     * @return the needFund
     */
    public String getNeedFund() {
        return this.NeedFund;
    }

    /**
     * <p>
     * Description: 应收金额
     * </p>
     * 
     * @param needFund the needFund to set
     */
    public void setNeedFund(String needFund) {
        this.NeedFund = needFund;
    }

    /**
     * <p>
     * Description: 应退金额
     * </p>
     * 
     * @return the backFund
     */
    public String getBackFund() {
        return this.BackFund;
    }

    /**
     * <p>
     * Description: 应退金额
     * </p>
     * 
     * @param backFund the backFund to set
     */
    public void setBackFund(String backFund) {
        this.BackFund = backFund;
    }

    /**
     * <p>
     * Description: 仓库名称
     * </p>
     * 
     * @return the wareHouseName
     */
    public String getWareHouseName() {
        return this.WareHouseName;
    }

    /**
     * <p>
     * Description: 仓库名称
     * </p>
     * 
     * @param wareHouseName the wareHouseName to set
     */
    public void setWareHouseName(String wareHouseName) {
        this.WareHouseName = wareHouseName;
    }

    /**
     * <p>
     * Description: 付款方式（现金1,POS机2）
     * </p>
     * 
     * @return the paymentType
     */
    public String getPaymentType() {
        return this.PaymentType;
    }

    /**
     * <p>
     * Description: 付款方式（现金1,POS机2）
     * </p>
     * 
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.PaymentType = paymentType;
    }

    /**
     * <p>
     * Description: 联系电话
     * </p>
     * 
     * @return the mobilePhone
     */
    public String getMobilePhone() {
        return this.MobilePhone;
    }

    /**
     * <p>
     * Description: 联系电话
     * </p>
     * 
     * @param mobilePhone the mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone) {
        this.MobilePhone = mobilePhone;
    }

    /**
     * <p>
     * Description: 收件人座机
     * </p>
     * 
     * @return the receiveTel
     */
    public String getReceiveTel() {
        return this.ReceiveTel;
    }

    /**
     * <p>
     * Description: 收件人座机
     * </p>
     * 
     * @param receiveTel the receiveTel to set
     */
    public void setReceiveTel(String receiveTel) {
        this.ReceiveTel = receiveTel;
    }

    /**
     * <p>
     * Description: 商品明细
     * </p>
     * 
     * @return the goods
     */
    public List<OrderGoodDTO> getGoods() {
        return this.Goods;
    }

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param goods the goods to set
     */
    public void setGoods(List<OrderGoodDTO> goods) {
        this.Goods = goods;
    }

    /**
     * <p>
     * Description: 备注
     * </p>
     * 
     * @return the remark
     */
    public String getRemark() {
        return this.Remark;
    }

    /**
     * <p>
     * Description: 备注
     * </p>
     * 
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.Remark = remark;
    }

    /**
     * <p>
     * Description: 订单ID
     * </p>
     * 
     * @return the orderid
     */
    public String getOrderid() {
        return this.orderid;
    }

    /**
     * <p>
     * Description: 订单ID
     * </p>
     * 
     * @param orderid the orderid to set
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
