
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
 *         &lt;element name="ObtenerTotalFacturaManualResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "obtenerTotalFacturaManualResult"
})
@XmlRootElement(name = "ObtenerTotalFacturaManualResponse")
public class ObtenerTotalFacturaManualResponse {

    @XmlElement(name = "ObtenerTotalFacturaManualResult")
    protected String obtenerTotalFacturaManualResult;

    /**
     * Gets the value of the obtenerTotalFacturaManualResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObtenerTotalFacturaManualResult() {
        return obtenerTotalFacturaManualResult;
    }

    /**
     * Sets the value of the obtenerTotalFacturaManualResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObtenerTotalFacturaManualResult(String value) {
        this.obtenerTotalFacturaManualResult = value;
    }

}
