package org.example.dto.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCoordinatesRequest {

    @DecimalMin(value = "0.0", message = "x coordinate must be non-negative")
    @DecimalMax(value = "39.0", message = "x coordinate must be less than or equal to 39")
    private Float x;

    @Max(value = 905, message = "y coordinate must be less than or equal to 905")
    private Integer y;

}
