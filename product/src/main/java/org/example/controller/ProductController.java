package org.example.controller;

import org.example.api.service.ProductService;
import org.example.dto.coordinates.CoordinatesRequest;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.meta.MetaResponse;
import org.example.dto.person.PersonRequest;
import org.example.dto.person.PersonResponse;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductRequest;
import org.example.dto.product.ProductResponse;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;
import org.example.model.enumeration.UnitOfMeasure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @POST
    public Response addProduct(ProductRequest request) {
        var product = mapProductFromRequest(request);

        product = productService.add(product);

        var response = mapProductToResponse(product);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response findAll(@QueryParam("page") Integer page,
                            @QueryParam("size") Integer size,
                            @QueryParam("sort") List<String> sort,
                            @QueryParam("filter") List<String> filter) {
        var result = productService.findAll(page, size, sort, filter);

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
    public Response updateProduct(@PathParam("id") Long id, ProductRequest request) {
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
    public Response deleteProductsByPrice(@PathParam("price") Double price) {
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

    private Product mapProductFromRequest(ProductRequest request) {
        var product = Product.builder()
                .productName(request == null ? null : request.getName())
                .coordinates(request == null ? null : mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request == null ? null : request.getPrice())
                .unitOfMeasure(request == null ? null : request.getUnitOfMeasure())
                .owner(request == null ? null : mapPersonFromRequest(request.getOwner()));

        return product.build();
    }

    private Coordinates mapCoordinatesFromRequest(CoordinatesRequest request) {
        var coordinates = Coordinates.builder()
                .x(request == null ? null : request.getX())
                .y(request == null ? null : request.getY());

        return coordinates.build();
    }

    private Person mapPersonFromRequest(PersonRequest request) {
        var person = Person.builder()
                .personName(request == null ? null : request.getName())
                .birthday(request == null ? null : request.getBirthday())
                .height(request == null ? null : request.getHeight())
                .eyeColor(request == null ? null : request.getEyeColor())
                .nationality(request == null ? null : request.getNationality());

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
