/**
 * JgWaybill.java Wangyong 2011-8-18 上午10:08:34
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 金刚运单接口信息数据对象
 * 
 * @author Wangyong
 * @2011-8-18 net.ytoec.kernel.dataobject
 */
public class JgWaybill implements Serializable {

    /**
     * JgWaybill.java Wangyong 上午10:09:08 2011-8-18
     */
    private static final long serialVersionUID = -7970848646314840509L;

    private Integer           id;
    private String            mailNo;
    private String            sName;
    private String            sPostCode;
    private String            sPhone;
    private String            sMobile;
    private String            sProv;
    private String            sCity;
    private String            sDistrict;
    private String            sAddress;
    private String            bName;
    private String            bPostCode;
    private String            bPhone;
    private String            bMobile;
    private String            bProv;
    private String            bCity;
    private String            bDistrict;
    private String            bAddress;
    private Date              createTime;
    private Integer           orderId;
    private String            backup1;
    private String            backup2;
    private String            logisticId;
    private String            lineType;
    private String            clientID;
    private String            serviceType;
    private String            customerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPostCode() {
        return sPostCode;
    }

    public void setsPostCode(String sPostCode) {
        this.sPostCode = sPostCode;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsMobile() {
        return sMobile;
    }

    public void setsMobile(String sMobile) {
        this.sMobile = sMobile;
    }

    public String getsProv() {
        return sProv;
    }

    public void setsProv(String sProv) {
        this.sProv = sProv;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsDistrict() {
        return sDistrict;
    }

    public void setsDistrict(String sDistrict) {
        this.sDistrict = sDistrict;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbPostCode() {
        return bPostCode;
    }

    public void setbPostCode(String bPostCode) {
        this.bPostCode = bPostCode;
    }

    public String getbPhone() {
        return bPhone;
    }

    public void setbPhone(String bPhone) {
        this.bPhone = bPhone;
    }

    public String getbMobile() {
        return bMobile;
    }

    public void setbMobile(String bMobile) {
        this.bMobile = bMobile;
    }

    public String getbProv() {
        return bProv;
    }

    public void setbProv(String bProv) {
        this.bProv = bProv;
    }

    public String getbCity() {
        return bCity;
    }

    public void setbCity(String bCity) {
        this.bCity = bCity;
    }

    public String getbDistrict() {
        return bDistrict;
    }

    public void setbDistrict(String bDistrict) {
        this.bDistrict = bDistrict;
    }

    public String getbAddress() {
        return bAddress;
    }

    public void setbAddress(String bAddress) {
        this.bAddress = bAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1;
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2;
    }

    public String getLogisticId() {
        return logisticId;
    }

    public void setLogisticId(String logisticId) {
        this.logisticId = logisticId;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
