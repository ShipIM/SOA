//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.02.21 at 04:23:38 AM MSK 
//


package org.example.products;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for country.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="country"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="USA"/&gt;
 *     &lt;enumeration value="CHINA"/&gt;
 *     &lt;enumeration value="INDIA"/&gt;
 *     &lt;enumeration value="NORTH_KOREA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "country")
@XmlEnum
public enum Country {

    USA,
    CHINA,
    INDIA,
    NORTH_KOREA;

    public String value() {
        return name();
    }

    public static Country fromValue(String v) {
        return valueOf(v);
    }

}
