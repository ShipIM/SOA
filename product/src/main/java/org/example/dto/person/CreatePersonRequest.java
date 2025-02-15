package org.example.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePersonRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    @XmlElement(required = true)
    private String name;

    @PastOrPresent(message = "birthday must be in the past or present")
    @XmlElement(required = true)
    private LocalDate birthday;

    @NotNull(message = "height cannot be null")
    @Positive(message = "height must be greater than 0")
    @XmlElement(required = true)
    private Double height;

    @NotNull(message = "eye color cannot be null")
    @XmlElement(name = "eye_color", required = true)
    private Color eyeColor;

    @NotNull(message = "nationality cannot be null")
    @XmlElement(required = true)
    private Country nationality;

}