package org.example.dto.person;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonResponse {

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String birthday;

    @XmlElement(required = true)
    private Double height;

    @XmlElement(name = "eye_color", required = true)
    private Color eyeColor;

    @XmlElement(required = true)
    private Country nationality;

}