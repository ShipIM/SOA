package org.example.dto.meta;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaResponse {

    @XmlElement(name = "current_page")
    private Integer currentPage;

    @XmlElement(name = "total_pages")
    private Integer totalPages;

    @XmlElement(name = "page_size")
    private Integer pageSize;

    @XmlElement(name = "total_items")
    private Integer totalItems;

}