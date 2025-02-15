package org.example.dto.coordinates;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCoordinatesRequest {

    @NotNull(message = "x coordinate cannot be null")
    @DecimalMin(value = "0.0", message = "x coordinate must be non-negative")
    @DecimalMax(value = "39.0", message = "x coordinate must be less than or equal to 39")
    @XmlElement(name = "x", required = true)
    private Float x;

    @NotNull(message = "y coordinate cannot be null.")
    @Max(value = 905, message = "y coordinate must be less than or equal to 905")
    @XmlElement(name = "y", required = true)
    private Integer y;

}