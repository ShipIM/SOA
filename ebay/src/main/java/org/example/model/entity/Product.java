package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.UnitOfMeasure;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;

    private String productName;

    private Coordinates coordinates;

    private LocalDate creationDate;

    private Integer price;

    private UnitOfMeasure unitOfMeasure;

    private Person owner;

}
