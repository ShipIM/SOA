package org.example.dto.meta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaginationRequest {

    @NotNull(message = "page number must be set")
    @Min(value = 0, message = "page number must be at least 0")
    @XmlElement(name = "page", required = true)
    private Integer page;

    @NotNull(message = "size must be set")
    @Min(value = 0, message = "size must be at least 0")
    @XmlElement(name = "size", required = true)
    private Integer size;

}