//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.01.18 at 03:25:16 PM EAT 
//


package com.sahay.cbs;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for lnDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lnDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="clearedBalance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="loanAccount" type="{http://vma.PHilae/}cnAccount" minOccurs="0"/&gt;
 *         &lt;element name="mobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paymentDueDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="repaymentAccount" type="{http://vma.PHilae/}cnAccount" minOccurs="0"/&gt;
 *         &lt;element name="repaymentAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="unpaidAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lnDetail", propOrder = {
    "clearedBalance",
    "loanAccount",
    "mobileNumber",
    "paymentDueDate",
    "repaymentAccount",
    "repaymentAmount",
    "unpaidAmount"
})
public class LnDetail {

    protected BigDecimal clearedBalance;
    protected CnAccount loanAccount;
    protected String mobileNumber;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar paymentDueDate;
    protected CnAccount repaymentAccount;
    protected BigDecimal repaymentAmount;
    protected BigDecimal unpaidAmount;

    /**
     * Gets the value of the clearedBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getClearedBalance() {
        return clearedBalance;
    }

    /**
     * Sets the value of the clearedBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setClearedBalance(BigDecimal value) {
        this.clearedBalance = value;
    }

    /**
     * Gets the value of the loanAccount property.
     * 
     * @return
     *     possible object is
     *     {@link CnAccount }
     *     
     */
    public CnAccount getLoanAccount() {
        return loanAccount;
    }

    /**
     * Sets the value of the loanAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CnAccount }
     *     
     */
    public void setLoanAccount(CnAccount value) {
        this.loanAccount = value;
    }

    /**
     * Gets the value of the mobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of the mobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNumber(String value) {
        this.mobileNumber = value;
    }

    /**
     * Gets the value of the paymentDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaymentDueDate() {
        return paymentDueDate;
    }

    /**
     * Sets the value of the paymentDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaymentDueDate(XMLGregorianCalendar value) {
        this.paymentDueDate = value;
    }

    /**
     * Gets the value of the repaymentAccount property.
     * 
     * @return
     *     possible object is
     *     {@link CnAccount }
     *     
     */
    public CnAccount getRepaymentAccount() {
        return repaymentAccount;
    }

    /**
     * Sets the value of the repaymentAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CnAccount }
     *     
     */
    public void setRepaymentAccount(CnAccount value) {
        this.repaymentAccount = value;
    }

    /**
     * Gets the value of the repaymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    /**
     * Sets the value of the repaymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepaymentAmount(BigDecimal value) {
        this.repaymentAmount = value;
    }

    /**
     * Gets the value of the unpaidAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    /**
     * Sets the value of the unpaidAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnpaidAmount(BigDecimal value) {
        this.unpaidAmount = value;
    }

}