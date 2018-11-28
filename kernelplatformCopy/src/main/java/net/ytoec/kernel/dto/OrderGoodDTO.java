/**
 * OrderGoodDTO.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.dto;

import java.io.Serializable;

/**
 * <p>
 * ClassName: OrderGoodDTO
 * </p>
 * <p>
 * Description: 商品明细数据类 (EDI系统WebService Json
 * 字符串key采用首字符大写，此类作为JSON格式交互类，所有字段均大写开头匹配JSON数据KEY值，所有字段访问修饰为Public)
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
public class OrderGoodDTO implements Serializable {

    /**
     * <p>
     * Field serialVersionUID: 序列号
     * </p>
     */
    private static final long serialVersionUID = 6006996589488887387L;

    /**
     * <p>
     * Field ProductName: 商品名称
     * </p>
     */
    public String ProductName;

    /**
     * <p>
     * Field ProductCode: 商品编号
     * </p>
     */
    public String ProductCode;

    /**
     * <p>
     * Field OrderAmount: 商品数量
     * </p>
     */
    public String OrderAmount;

    /**
     * <p>
     * Field SellPrice: 商品单价
     * </p>
     */
    public String SellPrice;

    /**
     * <p>
     * Field Size: 商品尺寸
     * </p>
     */
    public String Size;

    /**
     * <p>
     * Field Remark: 备注
     * </p>
     */
    public String Remark;

    /**
     * <p>
     * Description: 备注
     * </p>
     * 
     * @return the productName
     */
    public String getProductName() {
        return this.ProductName;
    }

    /**
     * <p>
     * Description: 商品名称
     * </p>
     * 
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    /**
     * <p>
     * Description: 商品编号
     * </p>
     * 
     * @return the productCode
     */
    public String getProductCode() {
        return this.ProductCode;
    }

    /**
     * <p>
     * Description: 商品编号
     * </p>
     * 
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    /**
     * <p>
     * Description: 商品数量
     * </p>
     * 
     * @return the orderAmount
     */
    public String getOrderAmount() {
        return this.OrderAmount;
    }

    /**
     * <p>
     * Description: 商品数量
     * </p>
     * 
     * @param orderAmount the orderAmount to set
     */
    public void setOrderAmount(String orderAmount) {
        this.OrderAmount = orderAmount;
    }

    /**
     * <p>
     * Description: 商品单价
     * </p>
     * 
     * @return the sellPrice
     */
    public String getSellPrice() {
        return this.SellPrice;
    }

    /**
     * <p>
     * Description: 商品单价
     * </p>
     * 
     * @param sellPrice the sellPrice to set
     */
    public void setSellPrice(String sellPrice) {
        this.SellPrice = sellPrice;
    }

    /**
     * <p>
     * Description: 商品尺寸
     * </p>
     * 
     * @return the size
     */
    public String getSize() {
        return this.Size;
    }

    /**
     * <p>
     * Description: 商品尺寸
     * </p>
     * 
     * @param size the size to set
     */
    public void setSize(String size) {
        this.Size = size;
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

}
