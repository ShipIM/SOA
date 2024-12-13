package org.example.controller;

import org.example.api.service.ProductService;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.coordinates.CreateCoordinatesRequest;
import org.example.dto.coordinates.UpdateCoordinatesRequest;
import org.example.dto.meta.MetaResponse;
import org.example.dto.meta.PaginationRequest;
import org.example.dto.person.CreatePersonRequest;
import org.example.dto.person.PersonResponse;
import org.example.dto.person.UpdatePersonRequest;
import org.example.dto.product.CreateProductRequest;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductResponse;
import org.example.dto.product.UpdateProductRequest;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;
import org.example.model.enumeration.UnitOfMeasure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @POST
    public Response addProduct(@Valid CreateProductRequest request) {
        var product = mapProductFromRequest(request);

        product = productService.add(product);

        var response = mapProductToResponse(product);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response findAll(@Valid @BeanParam PaginationRequest paginationRequest,
                            @QueryParam("sort") List<String> sort,
                            @QueryParam("filter") List<String> filter) {
        var result = productService.findAll(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                sort, filter
        );

        var products = result.getKey().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
        var meta = mapMetaToResponse(result.getValue());

        var response = new ProductListResponse(products, meta);

        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        var product = productService.getById(id);

        var response = mapProductToResponse(product);

        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, @Valid @NotNull UpdateProductRequest request) {
        var product = mapProductFromRequest(request);
        product.setId(id);

        productService.update(product);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.delete(id);

        return Response.noContent().build();
    }

    @GET
    @Path("/measurements/unique")
    public Response getUniqueMeasurements() {
        var uniqueMeasurements = productService.getUniqueUnitOfMeasure();

        return Response.ok(uniqueMeasurements).build();
    }

    @GET
    @Path("/color")
    public Response getColors() {
        var colors = List.of(Color.values());

        return Response.ok(colors).build();
    }

    @GET
    @Path("/country")
    public Response getCountries() {
        var countries = List.of(Country.values());

        return Response.ok(countries).build();
    }

    @GET
    @Path("/measure")
    public Response getMeasures() {
        var measures = List.of(UnitOfMeasure.values());

        return Response.ok(measures).build();
    }

    @DELETE
    @Path("/price/{price}")
    public Response deleteProductsByPrice(@PathParam("price") @Positive Integer price) {
        productService.deleteByPrice(price);

        return Response.noContent().build();
    }

    @GET
    @Path("/dates/min")
    public Response getEarliestProduct() {
        var product = productService.getMinCreationDate();

        var response = mapProductToResponse(product);

        return Response.ok(response).build();
    }

    private Product mapProductFromRequest(CreateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .unitOfMeasure(request.getUnitOfMeasure())
                .owner(mapPersonFromRequest(request.getOwner()));

        return product.build();
    }

    private Product mapProductFromRequest(UpdateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .unitOfMeasure(request.getUnitOfMeasure())
                .owner(mapPersonFromRequest(request.getOwner()));

        return product.build();
    }

    private Coordinates mapCoordinatesFromRequest(CreateCoordinatesRequest request) {
        if (request == null) {
            return null;
        }

        var coordinates = Coordinates.builder()
                .x(request.getX())
                .y(request.getY());

        return coordinates.build();
    }

    private Coordinates mapCoordinatesFromRequest(UpdateCoordinatesRequest request) {
        if (request == null) {
            return null;
        }

        var coordinates = Coordinates.builder()
                .x(request.getX())
                .y(request.getY());

        return coordinates.build();
    }

    private Person mapPersonFromRequest(CreatePersonRequest request) {
        if (request == null) {
            return null;
        }

        var person = Person.builder()
                .personName(request.getName())
                .birthday(request.getBirthday())
                .height(request.getHeight())
                .eyeColor(request.getEyeColor())
                .nationality(request.getNationality());

        return person.build();
    }

    private Person mapPersonFromRequest(UpdatePersonRequest request) {
        if (request == null) {
            return null;
        }

        var person = Person.builder()
                .personName(request.getName())
                .birthday(request.getBirthday())
                .height(request.getHeight())
                .eyeColor(request.getEyeColor())
                .nationality(request.getNationality());

        return person.build();
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
        if (person == null) {
            return null;
        }

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
