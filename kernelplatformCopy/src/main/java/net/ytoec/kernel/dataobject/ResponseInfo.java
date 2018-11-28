
package net.ytoec.kernel.dataobject;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>ResponseInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ResponseInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="failReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="failCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorWaybillNos" type="{http://www.w3dddddddd.org/2001/XMLSchema}waybillNo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseInfo", propOrder = {
    "success",
    "failReason",
    "failCode",
    "errorWaybillNos"
})
public class ResponseInfo {

    protected boolean success;
    protected String failReason;
    protected String failCode;
    protected List<WaybillNo> errorWaybillNos;
    public static QName QNAME=	 new QName("","ResponseInfo");
    /**
     * ��ȡsuccess���Ե�ֵ��
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * ����success���Ե�ֵ��
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

    /**
     * ��ȡfailReason���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailReason() {
        return failReason;
    }

    /**
     * ����failReason���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailReason(String value) {
        this.failReason = value;
    }

    /**
     * ��ȡfailCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailCode() {
        return failCode;
    }

    /**
     * ����failCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailCode(String value) {
        this.failCode = value;
    }

    /**
     * Gets the value of the errorWaybillNos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorWaybillNos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorWaybillNos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WaybillNo }
     * 
     * 
     */
    public List<WaybillNo> getErrorWaybillNos() {
        if (errorWaybillNos == null) {
            errorWaybillNos = new ArrayList<WaybillNo>();
        }
        return this.errorWaybillNos;
    }

	public void setErrorWaybillNos(List<WaybillNo> errorWaybillNos) {
		this.errorWaybillNos = errorWaybillNos;
	}

}
