//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.11.14 at 03:10:59 PM EAT 
//


package com.sahay.cbs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tqResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tqResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://vma.PHilae/}xiResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="txn" type="{http://vma.PHilae/}cbTxn"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tqResult", propOrder = {
    "txn"
})
public class TqResult
    extends XiResponse
{

    @XmlElement(required = true, nillable = true)
    protected CbTxn txn;

    /**
     * Gets the value of the txn property.
     * 
     * @return
     *     possible object is
     *     {@link CbTxn }
     *     
     */
    public CbTxn getTxn() {
        return txn;
    }

    /**
     * Sets the value of the txn property.
     * 
     * @param value
     *     allowed object is
     *     {@link CbTxn }
     *     
     */
    public void setTxn(CbTxn value) {
        this.txn = value;
    }

}
