package org.example.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CreateCoordinatesRequest;
import org.example.dto.person.CreatePersonRequest;
import org.example.model.enumeration.UnitOfMeasure;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateProductRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    @XmlElement(required = true)
    private String name;

    @Valid
    @NotNull(message = "coordinates cannot be null")
    @XmlElement(required = true)
    private CreateCoordinatesRequest coordinates;

    @NotNull(message = "price must be greater than 0")
    @Positive(message = "price must be greater than 0")
    @XmlElement(required = true)
    private Integer price;

    @XmlElement(name = "unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    @Valid
    @XmlElement
    private CreatePersonRequest owner;

}