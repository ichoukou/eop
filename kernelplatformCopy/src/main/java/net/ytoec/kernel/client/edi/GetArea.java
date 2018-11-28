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
 *         &lt;element name="province" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "lcId", "province", "city" })
@XmlRootElement(name = "GetArea")
public class GetArea {

	@XmlElementRef(name = "lcId", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> lcId;
	@XmlElementRef(name = "province", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> province;
	@XmlElementRef(name = "city", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> city;

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
	 * Gets the value of the province property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getProvince() {
		return province;
	}

	/**
	 * Sets the value of the province property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setProvince(JAXBElement<String> value) {
		this.province = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the city property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getCity() {
		return city;
	}

	/**
	 * Sets the value of the city property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setCity(JAXBElement<String> value) {
		this.city = ((JAXBElement<String>) value);
	}

}
