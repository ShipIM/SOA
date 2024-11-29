package org.example.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CoordinatesRequest;
import org.example.dto.person.PersonRequest;
import org.example.model.enumeration.UnitOfMeasure;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;

    private CoordinatesRequest coordinates;

    private Integer price;

    @JsonProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    private PersonRequest owner;

}
