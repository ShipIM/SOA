package org.example.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaResponse {

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("total_items")
    private Integer totalItems;

}
