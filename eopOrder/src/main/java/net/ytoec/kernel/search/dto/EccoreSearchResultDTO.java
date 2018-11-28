package net.ytoec.kernel.search.dto;

import java.util.Date;

/**
 * solr 返回的结果DTO
 * 
 * @author der
 */
public class EccoreSearchResultDTO {

    /**
     * order id
     */
    private Integer id;
    /**
     * 省名
     */
    private String  prov;
    /** 收件地/目的地/买家 省份编码 */
    private Integer numProv;
    /**
     * 市名
     */
    private String  city;
    /**
     * 地区名
     */
    private String  district;
    /**
     * 详细地址
     */
    private String  address;
    /**
     * 运单号
     */
    private String  mailNo;
    /**
     * 订单状态
     */
    private String  status;
    /**
     * 买家姓名
     */
    private String  name;
    /**
     * 卖家手机或电话
     */
    private String  phone;

    /**
     * 重量
     */
    private Float   weight;

    /**
     * 订单创建时间
     */
    private Date    createTime;
    /**
     * 状态变更时间，order表的acceptTime，build数据时候如果为空，则会塞入 update_time
     */
    private Date    acceptTime;

    /**
     * 订单类型
     */
    private Short   orderType;

    // 0：线上下单 1：线下下单
    private Short   lineType;

    /** 寄件地/发件地/卖家 省份编码 */
    private Integer numProvF;

    /** 寄件地/发件地/卖家 市名 */
    private String  cityF;       // 电子对账. 当省份为广东的时候会用到这个值来判断珠三角地区

    /** 订单表中的customerId */
    private String  customerId;

    /** 特殊账单标识。0：特殊账单 */
    private Short   freightType;

    /** 特殊账单修改值 */
    private Float   trimFreight;

    /** 物流号 */
    private String  txLogisticId;
    
    /** 对应客户编码 */
    private String userCode;
    
    /**
     * 揽件时间
     */
    private Date holdTime;

    public Date getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Short getOrderType() {
        return orderType;
    }


    public Short getLineType() {
        return lineType;
    }

    public void setLineType(Short lineType) {
        this.lineType = lineType;
    }

    public Integer getNumProv() {
        return numProv;
    }

    public void setNumProv(Integer numProv) {
        this.numProv = numProv;
    }

    public Integer getNumProvF() {
        return numProvF;
    }

    public void setNumProvF(Integer numProvF) {
        this.numProvF = numProvF;
    }

    public String getCityF() {
        return cityF;
    }

    public void setCityF(String cityF) {
        this.cityF = cityF;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Short getFreightType() {
        return freightType;
    }

    public void setFreightType(Short freightType) {
        this.freightType = freightType;
    }

    public Float getTrimFreight() {
        return trimFreight;
    }

    public void setTrimFreight(Float trimFreight) {
        this.trimFreight = trimFreight;
    }

    public String getTxLogisticId() {
        return txLogisticId;
    }

    public void setTxLogisticId(String txLogisticId) {
        this.txLogisticId = txLogisticId;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
