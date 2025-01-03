package org.example.dto.person;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    private String name;

    private Double height;

    @JsonProperty("eye_color")
    private Color eyeColor;

    private Country nationality;

}
