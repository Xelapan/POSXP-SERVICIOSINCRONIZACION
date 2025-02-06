
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
 *         &lt;element name="ListarJefesResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "listarJefesResult"
})
@XmlRootElement(name = "ListarJefesResponse")
public class ListarJefesResponse {

    @XmlElement(name = "ListarJefesResult")
    protected String listarJefesResult;

    /**
     * Gets the value of the listarJefesResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListarJefesResult() {
        return listarJefesResult;
    }

    /**
     * Sets the value of the listarJefesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListarJefesResult(String value) {
        this.listarJefesResult = value;
    }

}
