package org.example.dto.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @QueryParam("page")
    @NotNull(message = "page number must be set")
    @Min(value = 0, message = "page number must be at least 0")
    private Integer page;

    @QueryParam("size")
    @NotNull(message = "size must be set")
    @Min(value = 0, message = "size must be at least 0")
    private Integer size;

}
