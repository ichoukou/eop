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
 *         &lt;element name="lcid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orders" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "lcid", "orders" })
@XmlRootElement(name = "CommitBackOrderStatus")
public class CommitBackOrderStatus {

	@XmlElementRef(name = "lcid", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> lcid;
	@XmlElementRef(name = "orders", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> orders;

	/**
	 * Gets the value of the lcid property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getLcid() {
		return lcid;
	}

	/**
	 * Sets the value of the lcid property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setLcid(JAXBElement<String> value) {
		this.lcid = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the orders property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getOrders() {
		return orders;
	}

	/**
	 * Sets the value of the orders property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setOrders(JAXBElement<String> value) {
		this.orders = ((JAXBElement<String>) value);
	}

}
