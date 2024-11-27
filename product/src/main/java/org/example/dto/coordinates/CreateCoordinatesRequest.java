package org.example.dto.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoordinatesRequest {

    @NotNull(message = "x coordinate cannot be null")
    @DecimalMin(value = "0.0", message = "x coordinate must be non-negative")
    @DecimalMax(value = "39.0", message = "x coordinate must be less than or equal to 39")
    private Float x;

    @NotNull(message = "y coordinate cannot be null.")
    @Max(value = 905, message = "y coordinate must be less than or equal to 905")
    private Integer y;

}
