package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String personName;

    private LocalDate birthday;

    private Double height;

    private Color eyeColor;

    private Country nationality;

}
