//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.02.17 at 01:13:08 AM MSK 
//


package org.example.product;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ProductListResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ProductListResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="data" type="{http://example.org/product}ProductResponse" maxOccurs="unbounded"/&gt;
 *         &lt;element name="meta" type="{http://example.org/product}MetaResponse"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductListResponse", propOrder = {
        "data",
        "meta"
})
public class ProductListResponse {

    @XmlElement(required = true)
    protected List<ProductResponse> data;
    @XmlElement(required = true)
    protected MetaResponse meta;

    /**
     * Gets the value of the data property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductResponse }
     */
    public List<ProductResponse> getData() {
        if (data == null) {
            data = new ArrayList<ProductResponse>();
        }
        return this.data;
    }

    /**
     * Gets the value of the meta property.
     *
     * @return possible object is
     * {@link MetaResponse }
     */
    public MetaResponse getMeta() {
        return meta;
    }

    /**
     * Sets the value of the meta property.
     *
     * @param value allowed object is
     *              {@link MetaResponse }
     */
    public void setMeta(MetaResponse value) {
        this.meta = value;
    }

}
