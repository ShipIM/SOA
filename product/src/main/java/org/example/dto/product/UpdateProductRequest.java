package org.example.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.UpdateCoordinatesRequest;
import org.example.dto.person.UpdatePersonRequest;
import org.example.model.enumeration.UnitOfMeasure;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @Valid
    private UpdateCoordinatesRequest coordinates;

    @Positive(message = "price must be greater than 0")
    private Integer price;

    @JsonbProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    @Valid
    private UpdatePersonRequest owner;

}
