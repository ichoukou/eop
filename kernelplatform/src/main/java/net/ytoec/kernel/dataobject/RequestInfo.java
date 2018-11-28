
package net.ytoec.kernel.dataobject;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RequestInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="RequestInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mailNoList" type="{http://www.w3dddddddd.org/2001/XMLSchema}MailNoInfo" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestInfo", propOrder = {
    "mailNoList"
})
public class RequestInfo {

    @XmlElement(required = true)
    protected List<MailNoInfo> mailNoList;

    /**
     * Gets the value of the mailNoList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mailNoList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMailNoList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MailNoInfo }
     * 
     * 
     */
    public List<MailNoInfo> getMailNoList() {
        if (mailNoList == null) {
            mailNoList = new ArrayList<MailNoInfo>();
        }
        return this.mailNoList;
    }

}
