package org.example.dto.coordinates;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCoordinatesRequest {

    @DecimalMin(value = "0.0", message = "x coordinate must be non-negative")
    @DecimalMax(value = "39.0", message = "x coordinate must be less than or equal to 39")
    @XmlElement(required = false)
    private Float x;

    @Max(value = 905, message = "y coordinate must be less than or equal to 905")
    @XmlElement(required = false)
    private Integer y;

}