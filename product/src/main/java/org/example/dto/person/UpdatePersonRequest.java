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
public class UpdatePersonRequest {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be empty")
    @XmlElement(required = true)
    private String name;

    @PastOrPresent(message = "birthday must be in the past or present")
    @XmlElement(required = true)
    private LocalDate birthday;

    @Positive(message = "height must be greater than 0")
    @XmlElement(required = true)
    private Double height;

    @XmlElement(name = "eye_color", required = true)
    private Color eyeColor;

    @XmlElement(required = true)
    private Country nationality;

}