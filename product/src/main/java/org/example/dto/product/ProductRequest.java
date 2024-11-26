package org.example.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CoordinatesRequest;
import org.example.dto.person.PersonRequest;
import org.example.model.enumeration.UnitOfMeasure;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = "Name cannot be null.")
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotNull(message = "Coordinates cannot be null.")
    private CoordinatesRequest coordinates;

    @NotNull(message = "Price must be greater than 0.")
    @Positive(message = "Price must be greater than 0.")
    private Integer price;

    @JsonProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    private PersonRequest owner;

}
