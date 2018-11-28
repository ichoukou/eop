package net.ytoec.kernel.dataobject;

import java.util.Date;

import javax.xml.crypto.Data;

public class BuildSearchStatusWeightIndex {

    private Integer id;
    private Integer orderId;
    private String  mailNo;
    private Float   weight;
    
    //新增字段
    private Data createTimeData;
    private String buildTask;
	private String status;		
	private String prov;		
	private String provF;
	private Date createTime;
	private Date acceptTime;
	private String customerId;
	private int orderType;		//Integer
	private String name;
	private String phone;
	private String mobile;
	private int numProvF;		//Integer
	private int numProv;		
	private String cityF;      
	private String city;
	private String txLogisticId;
	private String address;
	private double trimFreight; //Float
	private String freightType; //Integer
	private Date partitiondate;
	private String lineType;    //Integer
	private String type;
	private String district;
	private String holdTime;
	private String oldMailNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

	public Data getCreateTimeData() {
		return createTimeData;
	}

	public void setCreateTimeData(Data createTimeData) {
		this.createTimeData = createTimeData;
	}

	public String getBuildTask() {
		return buildTask;
	}

	public void setBuildTask(String buildTask) {
		this.buildTask = buildTask;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
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

	public int getNumProvF() {
		return numProvF;
	}

	public void setNumProvF(int numProvF) {
		this.numProvF = numProvF;
	}

	public int getNumProv() {
		return numProv;
	}

	public void setNumProv(int numProv) {
		this.numProv = numProv;
	}

	public String getCityF() {
		return cityF;
	}

	public void setCityF(String cityF) {
		this.cityF = cityF;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getTrimFreight() {
		return trimFreight;
	}

	public void setTrimFreight(double trimFreight) {
		this.trimFreight = trimFreight;
	}

	public String getFreightType() {
		return freightType;
	}

	public void setFreightType(String freightType) {
		this.freightType = freightType;
	}

	public Date getPartitiondate() {
		return partitiondate;
	}

	public void setPartitiondate(Date partitiondate) {
		this.partitiondate = partitiondate;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public String getOldMailNo() {
		return oldMailNo;
	}

	public void setOldMailNo(String oldMailNo) {
		this.oldMailNo = oldMailNo;
	}
    
}
