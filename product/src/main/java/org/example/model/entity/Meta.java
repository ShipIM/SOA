package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    private Integer currentPage;

    private Integer totalPages;

    private Integer pageSize;

    private Integer totalItems;

}
