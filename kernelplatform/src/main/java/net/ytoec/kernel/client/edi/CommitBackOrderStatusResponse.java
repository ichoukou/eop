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
 *         &lt;element name="CommitBackOrderStatusResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "commitBackOrderStatusResult" })
@XmlRootElement(name = "CommitBackOrderStatusResponse")
public class CommitBackOrderStatusResponse {

	@XmlElementRef(name = "CommitBackOrderStatusResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> commitBackOrderStatusResult;

	/**
	 * Gets the value of the commitBackOrderStatusResult property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getCommitBackOrderStatusResult() {
		return commitBackOrderStatusResult;
	}

	/**
	 * Sets the value of the commitBackOrderStatusResult property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setCommitBackOrderStatusResult(JAXBElement<String> value) {
		this.commitBackOrderStatusResult = ((JAXBElement<String>) value);
	}

}
