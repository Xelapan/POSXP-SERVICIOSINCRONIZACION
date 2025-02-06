
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="fDel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fAl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "fDel",
    "fAl"
})
@XmlRootElement(name = "InformeEmpleador")
public class InformeEmpleador {

    protected String fDel;
    protected String fAl;

    /**
     * Gets the value of the fDel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFDel() {
        return fDel;
    }

    /**
     * Sets the value of the fDel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFDel(String value) {
        this.fDel = value;
    }

    /**
     * Gets the value of the fAl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFAl() {
        return fAl;
    }

    /**
     * Sets the value of the fAl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFAl(String value) {
        this.fAl = value;
    }

}
