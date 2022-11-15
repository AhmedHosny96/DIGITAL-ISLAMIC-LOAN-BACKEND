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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xiResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="xiResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="availableBalance" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ledgerBalance" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="txnId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xiResponse", propOrder = {
        "result",
        "reference",
        "message",
        "availableBalance",
        "ledgerBalance",
        "txnId"
})
@XmlSeeAlso({

        CaResponse.class,
        CcResponse.class,

})
public class XiResponse {

    @XmlElement(required = true)
    protected String result;
    @XmlElement(required = true, nillable = true)
    protected String reference;
    @XmlElement(required = true, nillable = true)
    protected String message;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal availableBalance;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal ledgerBalance;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long txnId;

    /**
     * Gets the value of the result property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Gets the value of the reference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the availableBalance property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Sets the value of the availableBalance property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setAvailableBalance(BigDecimal value) {
        this.availableBalance = value;
    }

    /**
     * Gets the value of the ledgerBalance property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getLedgerBalance() {
        return ledgerBalance;
    }

    /**
     * Sets the value of the ledgerBalance property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setLedgerBalance(BigDecimal value) {
        this.ledgerBalance = value;
    }

    /**
     * Gets the value of the txnId property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getTxnId() {
        return txnId;
    }

    /**
     * Sets the value of the txnId property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setTxnId(Long value) {
        this.txnId = value;
    }

}
