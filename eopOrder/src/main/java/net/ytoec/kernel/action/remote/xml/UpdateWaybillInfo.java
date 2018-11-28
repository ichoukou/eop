package net.ytoec.kernel.action.remote.xml;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.remote.process.UpdateInfoProcessor;

/**
 * 
 * 运单更新.
 * 
 */
public class UpdateWaybillInfo   implements
        ObjectToXmlFragment {
    // 物流平台的物流号（不能为空）
    private String txLogisticId;
    // 运单号，在没有确定的情况下可能为空，如取消订单
    private String mailNo;
    //
    private String clientId;
    // 物流公司编号
    private String logisticProviderId;
    // 通知类型 INSTRUCTION：通知指令
    private String infoType;
    // 通知内容 在infoType为INSTRUCTION时：UPDATE ：更新运单号 WITHDRAW：取消订单
    private String infoContent;
    // 备注
    private String remark;
    //揽收成功的时候，将时间写入这个字段中，超时件要使用。
    private String type;
    // 疑似问题件(物流公司发送给淘宝)
    private String suspect;
    // 疑似问题件(淘宝发送给物流公司)
    private String exception;

    //ADD--MGL 2011-09-23
    private String name;
    private String acceptTime;
    private String currentCity;
    private String nextCity;
    private String facility;
    private String contactInfo;
    private Double weight;
    private String trackingInfo;
    private String weightStr;
    
    //cod 支付时间
    private String payAmount;
    private String payTime;
    private String unitId;
    private String employeeId;
    
    // 更新字段
    private String fieldName;
    private String fieldValue;


    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    // 响应对象
    private Response response = new Response();

    
    
    private UpdateInfoProcessor updateInfoProcessor = new UpdateInfoProcessor();

    public UpdateWaybillInfo() {

    }

    public UpdateWaybillInfo(String xmlFragment) {

    }

    // taobao1.4.1版本接口使用
    public List<UpdateWaybillInfo> toObjectTaoBao(String xmlFragment) {
    	 List<UpdateWaybillInfo> updateInfo = updateInfoProcessor.parseTaoBao(xmlFragment);
        return updateInfo;
    }
    
    public UpdateWaybillInfo toObject(String xmlFragment) {
   	 UpdateWaybillInfo updateInfo = updateInfoProcessor.parse(xmlFragment);
       return updateInfo;
   }
    public UpdateWaybillInfo toAlterObject(String xmlFragment) {
        UpdateWaybillInfo updateInfo = updateInfoProcessor.parseAlter(xmlFragment);
        return updateInfo;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getTxLogisticId() {
        return txLogisticId;
    }

    public void setTxLogisticId(String txLogisticId) {
        this.txLogisticId = txLogisticId;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getLogisticProviderId() {
        return logisticProviderId;
    }

    public void setLogisticProviderId(String logisticProviderId) {
        this.logisticProviderId = logisticProviderId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getNextCity() {
        return nextCity;
    }

    public void setNextCity(String nextCity) {
        this.nextCity = nextCity;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(String trackingInfo) {
        this.trackingInfo = trackingInfo;
    }

    public String toXmlFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append("<UpdateInfo>");

        sb.append("<txLogisticID>");
        sb.append(this.getTxLogisticId());
        sb.append("</txLogisticID>");

        sb.append("<mailNo>");
        sb.append(this.getMailNo());
        sb.append("</mailNo>");

        sb.append("<clientID>");
        sb.append(this.getClientId());
        sb.append("</clientID>");

        sb.append("<logisticProviderID>");
        sb.append(this.getLogisticProviderId());
        sb.append("</logisticProviderID>");

        sb.append("<infoType>");
        sb.append(this.getInfoType());
        sb.append("</infoType>");

        sb.append("<infoContent>");
        sb.append(this.getInfoContent());
        sb.append("</infoContent>");

        sb.append("<remark>");
        sb.append(this.getRemark());
        sb.append("</remark>");

        sb.append("</UpdateInfo>");

        return sb.toString();
    }

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getSuspect() {
		return suspect;
	}

	public void setSuspect(String suspect) {
		this.suspect = suspect;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getWeigthStr() {
		return weightStr;
	}

	public void setWeigthStr(String weigthStr) {
		this.weightStr = weigthStr;
	}
    

}
