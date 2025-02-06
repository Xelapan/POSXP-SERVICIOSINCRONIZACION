
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
 *         &lt;element name="ObtenerLlamadasResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "obtenerLlamadasResult"
})
@XmlRootElement(name = "ObtenerLlamadasResponse")
public class ObtenerLlamadasResponse {

    @XmlElement(name = "ObtenerLlamadasResult")
    protected String obtenerLlamadasResult;

    /**
     * Gets the value of the obtenerLlamadasResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObtenerLlamadasResult() {
        return obtenerLlamadasResult;
    }

    /**
     * Sets the value of the obtenerLlamadasResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObtenerLlamadasResult(String value) {
        this.obtenerLlamadasResult = value;
    }

}
