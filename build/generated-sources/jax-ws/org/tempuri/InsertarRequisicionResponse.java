
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InsertarRequisicionResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "insertarRequisicionResult"
})
@XmlRootElement(name = "InsertarRequisicionResponse")
public class InsertarRequisicionResponse {

    @XmlElement(name = "InsertarRequisicionResult")
    protected String insertarRequisicionResult;

    /**
     * Gets the value of the insertarRequisicionResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsertarRequisicionResult() {
        return insertarRequisicionResult;
    }

    /**
     * Sets the value of the insertarRequisicionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsertarRequisicionResult(String value) {
        this.insertarRequisicionResult = value;
    }

}
