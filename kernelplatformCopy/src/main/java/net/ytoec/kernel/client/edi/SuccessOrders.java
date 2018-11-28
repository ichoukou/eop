package net.ytoec.kernel.client.edi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lcId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="waybills" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "lcId", "waybills" })
@XmlRootElement(name = "SuccessOrders")
public class SuccessOrders {

	@XmlElementRef(name = "lcId", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> lcId;
	@XmlElementRef(name = "waybills", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> waybills;

	/**
	 * Gets the value of the lcId property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getLcId() {
		return lcId;
	}

	/**
	 * Sets the value of the lcId property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setLcId(JAXBElement<String> value) {
		this.lcId = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the waybills property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getWaybills() {
		return waybills;
	}

	/**
	 * Sets the value of the waybills property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setWaybills(JAXBElement<String> value) {
		this.waybills = ((JAXBElement<String>) value);
	}

}