package org.example.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.meta.MetaResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {

    private List<ProductResponse> data;

    private MetaResponse meta;

}
