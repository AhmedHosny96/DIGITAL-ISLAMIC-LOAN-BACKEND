//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.11.14 at 03:10:59 PM EAT 
//


package com.sahay.cbs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchCustomerInBranch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="searchCustomerInBranch"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="query" type="{http://vma.PHilae/}clQuery" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchCustomerInBranch", propOrder = {
    "query"
})
public class SearchCustomerInBranch {

    protected ClQuery query;

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link ClQuery }
     *     
     */
    public ClQuery getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClQuery }
     *     
     */
    public void setQuery(ClQuery value) {
        this.query = value;
    }

}
