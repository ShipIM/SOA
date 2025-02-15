package org.example.dto.coordinates;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class CoordinatesResponse {

    @XmlElement(name = "x")
    private Float x;

    @XmlElement(name = "y")
    private Integer y;

}