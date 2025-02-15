package org.example.dto.product;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.person.PersonResponse;
import org.example.model.enumeration.UnitOfMeasure;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductResponse {

    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private CoordinatesResponse coordinates;

    @XmlElement(name = "creation_date", required = true)
    private String creationDate;

    @XmlElement(required = true)
    private Integer price;

    @XmlElement(name = "unit_of_measure", required = true)
    private UnitOfMeasure unitOfMeasure;

    @XmlElement(required = true)
    private PersonResponse owner;

}