package org.example.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.person.PersonResponse;
import org.example.model.enumeration.UnitOfMeasure;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private CoordinatesResponse coordinates;

    @JsonProperty("creation_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private String creationDate;

    private Integer price;

    @JsonProperty("unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    private PersonResponse owner;

}
