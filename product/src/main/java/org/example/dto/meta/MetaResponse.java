package org.example.dto.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaResponse {

    @JsonbProperty("current_page")
    private Integer currentPage;

    @JsonbProperty("total_pages")
    private Integer totalPages;

    @JsonbProperty("page_size")
    private Integer pageSize;

    @JsonbProperty("total_items")
    private Integer totalItems;

}
