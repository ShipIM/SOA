package org.example.controller;

import org.example.api.service.ProductService;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.meta.MetaResponse;
import org.example.dto.meta.PaginationRequest;
import org.example.dto.person.PersonResponse;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductResponse;
import org.example.exception.ApiException;
import org.example.exception.ServiceUnavailableException;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/ebay")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EbayController {

    @EJB
    private ProductService productService;

    @GET
    @Path("/filter/price/{price-from}/{price-to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterProductsByPrice(@PathParam("price-from") Integer priceFrom,
                                          @PathParam("price-to") Integer priceTo,
                                          @Valid @BeanParam PaginationRequest paginationRequest) {
        if (productService == null) {
            throw new ApiException(
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "could not initialize product service bean"
            );
        }

        try {
            var result = productService.getProductsByPriceRange(
                    priceFrom, priceTo,
                    paginationRequest.getPage(),
                    paginationRequest.getSize()
            );

            var products = result.getKey().stream()
                    .map(this::mapProductToResponse)
                    .collect(Collectors.toList());
            var meta = mapMetaToResponse(result.getValue());

            var response = new ProductListResponse(products, meta);

            return Response.ok().entity(response).build();
        } catch (ServiceUnavailableException e) {
            throw new ApiException(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), "target service is unavailable right now, please try later");
        }
    }

    @POST
    @Path("/price/increase/{increase-percent}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response increaseProductPrices(@PathParam("increase-percent") Integer increasePercent) {
        if (productService == null) {
            throw new ApiException(
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "could not initialize product service bean"
            );
        }

        try {
            productService.increaseAllProductPrices(increasePercent);

            return Response.noContent().build();
        } catch (ServiceUnavailableException e) {
            throw new ApiException(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), "target service is unavailable right now, please try later");
        }
    }

    private ProductResponse mapProductToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                mapCoordinatesToResponse(product.getCoordinates()),
                product.getCreationDate(),
                product.getPrice(),
                product.getUnitOfMeasure(),
                mapPersonToResponse(product.getOwner())
        );
    }

    private CoordinatesResponse mapCoordinatesToResponse(Coordinates coordinates) {
        return new CoordinatesResponse(
                coordinates.getX(),
                coordinates.getY()
        );
    }

    private PersonResponse mapPersonToResponse(Person person) {
        return new PersonResponse(
                person.getPersonName(),
                person.getBirthday(),
                person.getHeight(),
                person.getEyeColor(),
                person.getNationality()
        );
    }

    private MetaResponse mapMetaToResponse(Meta meta) {
        return new MetaResponse(
                meta.getCurrentPage(),
                meta.getTotalPages(),
                meta.getPageSize(),
                meta.getTotalItems()
        );
    }

}
