package net.ytoec.kernel.search.dataobject;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class EccoreItem {

    /**
     * order表id
     */
    @Field("id")
    private Integer  id;
    /**
     * order表customerID
     */
    @Field("customerId")
    private String   customerId;
    /**
     * order表mailNo
     */
    @Field("mailNo")
    private String   mailNo;
    /**
     * order表orderType
     */
    @Field("orderType")
    private Short    orderType;
    /**
     * order表订单状态
     */
    @Field("numStatus")
    private Short    numStatus;
    /**
     * 联系人表name
     */
    @Field("name")
    private String   name;
    /**
     * 联系人表phone和mobile,用于索引
     */
    @Field("phone")
    private String[] phone;
    /**
     * 搜索结果显示的电话
     */
    @Field("displayPhone")
    private String   displayPhone;
    /**
     * 联系人表省的id
     */
    @Field("numProv")
    private Integer  numProv;
    /**
     * 联系人表市的id
     */
    @Field("numCity")
    private Integer  numCity;
    /**
     * 联系人表区的id
     */
    @Field("numDistrict")
    private Integer  numDistrict;
    /**
     * 联系人表地址
     */
    @Field("address")
    private String   address;
    /**
     * order表重量
     */
    @Field("weight")
    private Float    weight;

    /**
     * order表创建时间
     */
    @Field("createTime")
    private Date     createTime;
    /**
     * order表acceptTime，如果为空，则填充update_time
     */
    @Field("acceptTime")
    private Date     acceptTime;
    /**
     * partitiondate 减去 2011-11-19 得到天数 ， 例如：2011-11-20 则为 1
     */
    @Field("numCreateTime")
    private Integer  numCreateTime;

    /**
     * 卖家省份Code
     */
    @Field("numProvF")
    private Integer  numProvF;

    /**
     * 卖家城市名称
     */
    @Field("cityF")
    private String   cityF;

    /** 特殊账单标识。0：特殊账单 ,默认为1，即不是特殊账单*/
    @Field("freightType")
    private Short    freightType=1;

    /** 特殊账单修改值 */
    @Field("trimFreight")
    private Float    trimFreight;

    /** 物流号 */
    @Field("txLogisticId")
    private String   txLogisticId;

    /**
     * 下单渠道
     */
    @Field("lineType")
    private Short    lineType;
    /**
     * 是否允许solr搜索此数据，默认值为0，运单号为空则为1
     */
    @Field("isDispaly")
    private Short    isDispaly=0;
    
    /**
     * 揽收时间
     * @return
     */
    @Field("holdTime")
    private Integer holdTime;

    /**
     * 将mailNo 和 customerId 做为主键
     */
    @Field("mailNoAndCustomerId")
    
    private String mailNoAndCustomerId;
    
    public String getMailNoAndCustomerId() {
		return mailNoAndCustomerId;
	}

	public void setMailNoAndCustomerId(String mailNoAndCustomerId) {
		this.mailNoAndCustomerId = mailNoAndCustomerId;
	}

	public Integer getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Integer holdTime) {
		this.holdTime = holdTime;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumProv() {
        return numProv;
    }

    public void setNumProv(Integer numProv) {
        this.numProv = numProv;
    }

    public Integer getNumCity() {
        return numCity;
    }

    public void setNumCity(Integer numCity) {
        this.numCity = numCity;
    }

    public Integer getNumDistrict() {
        return numDistrict;
    }

    public void setNumDistrict(Integer numDistrict) {
        this.numDistrict = numDistrict;
    }

    public Short getNumStatus() {
        return numStatus;
    }

    public void setNumStatus(Short numStatus) {
        this.numStatus = numStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPhone() {
        return phone;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
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

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Short getOrderType() {
        return orderType;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

    public Integer getNumCreateTime() {
        return numCreateTime;
    }

    public void setNumCreateTime(Integer numCreateTime) {
        this.numCreateTime = numCreateTime;
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

    public Short getLineType() {
        return lineType;
    }

    public void setLineType(Short lineType) {
        this.lineType = lineType;
    }

    
    public Short getIsDispaly() {
        return isDispaly;
    }

    
    public void setIsDispaly(Short isDispaly) {
        this.isDispaly = isDispaly;
    }

}
