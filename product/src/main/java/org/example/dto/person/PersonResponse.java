package org.example.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private String name;

    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate birthday;

    private Double height;

    @JsonbProperty("eye_color")
    private Color eyeColor;

    private Country nationality;
}
