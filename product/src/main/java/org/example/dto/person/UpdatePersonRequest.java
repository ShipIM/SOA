package org.example.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @PastOrPresent(message = "birthday must be in the past or present")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @Positive(message = "height must be greater than 0")
    private Double height;

    @JsonbProperty("eye_color")
    private Color eyeColor;

    private Country nationality;
}