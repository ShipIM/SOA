package org.example.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.person.PersonResponse;
import org.example.model.enumeration.UnitOfMeasure;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private CoordinatesResponse coordinates;

    @JsonbProperty("creation_date")
    @JsonbDateFormat("yyyy-MM-dd'T'HH:m:ss.SSSXXX")
    private ZonedDateTime creationDate;

    private Integer price;

    @JsonbProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    private PersonResponse owner;
}