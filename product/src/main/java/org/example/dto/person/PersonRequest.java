package org.example.dto.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    @NotNull(message = "Name cannot be null.")
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @PastOrPresent(message = "Birthday must be in the past or present.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;

    @NotNull(message = "Height cannot be null.")
    @Positive(message = "Height must be greater than 0.")
    private Double height;

    @NotNull(message = "Eye color cannot be null.")
    @JsonProperty("eye_color")
    private Color eyeColor;

    @NotNull(message = "Nationality cannot be null.")
    private Country nationality;

}
