//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.02.17 at 02:50:21 AM MSK 
//


package org.example.product;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCoordinatesRequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CreateCoordinatesRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCoordinatesRequest", propOrder = {
        "x",
        "y"
})
public class CreateCoordinatesRequest {

    protected float x;
    protected int y;

    /**
     * Gets the value of the x property.
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     */
    public void setX(float value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     */
    public void setY(int value) {
        this.y = value;
    }

}
