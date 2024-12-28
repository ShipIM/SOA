package org.example.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CreateCoordinatesRequest;
import org.example.dto.person.CreatePersonRequest;
import org.example.model.enumeration.UnitOfMeasure;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @Valid
    @NotNull(message = "coordinates cannot be null")
    private CreateCoordinatesRequest coordinates;

    @NotNull(message = "price must be greater than 0")
    @Positive(message = "price must be greater than 0")
    private Integer price;

    @JsonbProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    @Valid
    private CreatePersonRequest owner;

}
