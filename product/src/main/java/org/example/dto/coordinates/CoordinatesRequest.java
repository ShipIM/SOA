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
public class CoordinatesRequest {

    @NotNull(message = "X coordinate cannot be null.")
    @DecimalMin(value = "0.0", message = "X coordinate must be non-negative.")
    @DecimalMax(value = "39.0", message = "X coordinate must be less than or equal to 39.")
    private Float x;

    @NotNull(message = "Y coordinate cannot be null.")
    @Max(value = 905, message = "Y coordinate must be less than or equal to 905.")
    private Integer y;

}
