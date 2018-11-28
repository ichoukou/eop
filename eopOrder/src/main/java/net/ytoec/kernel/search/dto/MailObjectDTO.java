package net.ytoec.kernel.search.dto;

import java.util.Date;

/**
 * build数据到搜索引擎的dto
 * 
 * @author der
 */
public class MailObjectDTO {

    private int     id;
    private int     numProv;
    // private int numCity;
    // private int numDistrict;
    private String  city;
    private String  district;

    private String  mailNo;
    private String  status;
    private String  name;
    private String  phone;
    private String  mobile;
    private String  customerId;
    private String  address;

    private float   weight      = 0.0f;

    private Date    partitiondate;
    private Date    createTime;
    private Date    updateTime;
    private Date    acceptTime;
    private Integer orderType   = 0;

    // FIXME ....
    private int     createTimeInt;

    // 0：线上下单 1：线下下单
    private Integer lineType;

    /** 寄件地/发件地/卖家 市名 */
    private String  cityF;             // 电子对账. 当省份为广东的时候会用到这个值来判断珠三角地区

    /** 特殊账单标识。0：特殊账单 */
    private Integer freightType;       // = 1;

    /** 特殊账单修改值 */
    private Float   trimFreight = 0.0f;

    /** 物流号 */
    private String  txLogisticId;

    /**
     * 卖家省名
     */
    private Integer numProvF;

    private String  prov;
    private String  provF;

    private String  buildTask;

    private String  oldMailNo;
    
    /**
     * 揽收时间  by wusha
     */
    private String holdTime;

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumProv() {
        return numProv;
    }

    public void setNumProv(int numProv) {
        this.numProv = numProv;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getPartitiondate() {
        return partitiondate;
    }

    public void setPartitiondate(Date partitiondate) {
        this.partitiondate = partitiondate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public int getCreateTimeInt() {
        return createTimeInt;
    }

    public void setCreateTimeInt(int createTimeInt) {
        this.createTimeInt = createTimeInt;
    }

    public Integer getLineType() {
        return lineType;
    }

    public void setLineType(Integer lineType) {
        this.lineType = lineType;
    }

    public String getCityF() {
        return cityF;
    }

    public void setCityF(String cityF) {
        this.cityF = cityF;
    }

    public Integer getFreightType() {
        return freightType;
    }

    public void setFreightType(Integer freightType) {
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

    public Integer getNumProvF() {
        return numProvF;
    }

    public void setNumProvF(Integer numProvF) {
        this.numProvF = numProvF;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getProvF() {
        return provF;
    }

    public void setProvF(String provF) {
        this.provF = provF;
    }

    public String getBuildTask() {
        return buildTask;
    }

    public void setBuildTask(String buildTask) {
        this.buildTask = buildTask;
    }

    public String getOldMailNo() {
        return oldMailNo;
    }

    public void setOldMailNo(String oldMailNo) {
        this.oldMailNo = oldMailNo;
    }

}
