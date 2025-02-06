
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
 *         &lt;element name="json" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IdGenerico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "json",
    "idGenerico"
})
@XmlRootElement(name = "BuscarDatosNitYFactura")
public class BuscarDatosNitYFactura {

    protected int json;
    @XmlElement(name = "IdGenerico")
    protected String idGenerico;

    /**
     * Gets the value of the json property.
     * 
     */
    public int getJson() {
        return json;
    }

    /**
     * Sets the value of the json property.
     * 
     */
    public void setJson(int value) {
        this.json = value;
    }

    /**
     * Gets the value of the idGenerico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdGenerico() {
        return idGenerico;
    }

    /**
     * Sets the value of the idGenerico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdGenerico(String value) {
        this.idGenerico = value;
    }

}
