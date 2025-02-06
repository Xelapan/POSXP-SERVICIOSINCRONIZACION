
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
 *         &lt;element name="InsertarCorteFacturaManualMySqlResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "insertarCorteFacturaManualMySqlResult"
})
@XmlRootElement(name = "InsertarCorteFacturaManualMySqlResponse")
public class InsertarCorteFacturaManualMySqlResponse {

    @XmlElement(name = "InsertarCorteFacturaManualMySqlResult")
    protected String insertarCorteFacturaManualMySqlResult;

    /**
     * Gets the value of the insertarCorteFacturaManualMySqlResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsertarCorteFacturaManualMySqlResult() {
        return insertarCorteFacturaManualMySqlResult;
    }

    /**
     * Sets the value of the insertarCorteFacturaManualMySqlResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsertarCorteFacturaManualMySqlResult(String value) {
        this.insertarCorteFacturaManualMySqlResult = value;
    }

}
