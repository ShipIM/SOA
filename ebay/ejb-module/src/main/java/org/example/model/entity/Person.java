package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    private String personName;

    private String birthday;

    private Double height;

    private Color eyeColor;

    private Country nationality;

}
