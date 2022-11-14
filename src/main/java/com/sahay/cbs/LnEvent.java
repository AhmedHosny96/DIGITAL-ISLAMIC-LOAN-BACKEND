//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.11.14 at 03:10:59 PM EAT 
//


package com.sahay.cbs;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for lnEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lnEvent"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="acctId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="amountPaid" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="amountUnpaid" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="dueDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="eventDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="eventId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="fundingAcctId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="latefeeFg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="repmntAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="serviceDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "lnEvent", propOrder = {
    "acctId",
    "amountPaid",
    "amountUnpaid",
    "dueDate",
    "eventDesc",
    "eventId",
    "fundingAcctId",
    "latefeeFg",
    "repmntAmount",
    "serviceDate",
    "status"
})
public class LnEvent {

    protected Long acctId;
    protected BigDecimal amountPaid;
    protected BigDecimal amountUnpaid;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dueDate;
    protected String eventDesc;
    protected Long eventId;
    protected Long fundingAcctId;
    protected String latefeeFg;
    protected BigDecimal repmntAmount;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar serviceDate;
    protected String status;

    /**
     * Gets the value of the acctId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAcctId() {
        return acctId;
    }

    /**
     * Sets the value of the acctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAcctId(Long value) {
        this.acctId = value;
    }

    /**
     * Gets the value of the amountPaid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    /**
     * Sets the value of the amountPaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountPaid(BigDecimal value) {
        this.amountPaid = value;
    }

    /**
     * Gets the value of the amountUnpaid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountUnpaid() {
        return amountUnpaid;
    }

    /**
     * Sets the value of the amountUnpaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountUnpaid(BigDecimal value) {
        this.amountUnpaid = value;
    }

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDueDate(XMLGregorianCalendar value) {
        this.dueDate = value;
    }

    /**
     * Gets the value of the eventDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDesc() {
        return eventDesc;
    }

    /**
     * Sets the value of the eventDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDesc(String value) {
        this.eventDesc = value;
    }

    /**
     * Gets the value of the eventId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Sets the value of the eventId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEventId(Long value) {
        this.eventId = value;
    }

    /**
     * Gets the value of the fundingAcctId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFundingAcctId() {
        return fundingAcctId;
    }

    /**
     * Sets the value of the fundingAcctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFundingAcctId(Long value) {
        this.fundingAcctId = value;
    }

    /**
     * Gets the value of the latefeeFg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatefeeFg() {
        return latefeeFg;
    }

    /**
     * Sets the value of the latefeeFg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatefeeFg(String value) {
        this.latefeeFg = value;
    }

    /**
     * Gets the value of the repmntAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepmntAmount() {
        return repmntAmount;
    }

    /**
     * Sets the value of the repmntAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepmntAmount(BigDecimal value) {
        this.repmntAmount = value;
    }

    /**
     * Gets the value of the serviceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the value of the serviceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setServiceDate(XMLGregorianCalendar value) {
        this.serviceDate = value;
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
