//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.01.18 at 03:25:16 PM EAT 
//


package com.sahay.cbs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for tlDrawer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlDrawer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="currencies" type="{http://vma.PHilae/}cnCurrency" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="drawerAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="drawerId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="drawerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlDrawer", propOrder = {
    "currencies",
    "drawerAccount",
    "drawerId",
    "drawerNumber",
    "openDate",
    "status"
})
public class TlDrawer {

    @XmlElement(nillable = true)
    protected List<CnCurrency> currencies;
    protected String drawerAccount;
    protected Long drawerId;
    protected String drawerNumber;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar openDate;
    protected String status;

    /**
     * Gets the value of the currencies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the currencies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCurrencies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CnCurrency }
     * 
     * 
     */
    public List<CnCurrency> getCurrencies() {
        if (currencies == null) {
            currencies = new ArrayList<CnCurrency>();
        }
        return this.currencies;
    }

    /**
     * Gets the value of the drawerAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrawerAccount() {
        return drawerAccount;
    }

    /**
     * Sets the value of the drawerAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrawerAccount(String value) {
        this.drawerAccount = value;
    }

    /**
     * Gets the value of the drawerId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDrawerId() {
        return drawerId;
    }

    /**
     * Sets the value of the drawerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDrawerId(Long value) {
        this.drawerId = value;
    }

    /**
     * Gets the value of the drawerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrawerNumber() {
        return drawerNumber;
    }

    /**
     * Sets the value of the drawerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrawerNumber(String value) {
        this.drawerNumber = value;
    }

    /**
     * Gets the value of the openDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOpenDate() {
        return openDate;
    }

    /**
     * Sets the value of the openDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOpenDate(XMLGregorianCalendar value) {
        this.openDate = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

}