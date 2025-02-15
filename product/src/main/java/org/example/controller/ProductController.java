package org.example.controller;

import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@WebService
public class ProductController {

    @Inject
    private ProductService productService;

    @WebMethod
    public ProductResponse addProduct(@Valid CreateProductRequest request) {
        var product = mapProductFromRequest(request);
        product = productService.add(product);
        return mapProductToResponse(product);
    }

    @WebMethod
    public ProductListResponse findAll(@Valid PaginationRequest paginationRequest, List<String> sort, List<String> filter) {
        var result = productService.findAll(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                sort, filter
        );

        var products = result.getKey().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
        var meta = mapMetaToResponse(result.getValue());

        return new ProductListResponse(products, meta);
    }

    @WebMethod
    public ProductResponse getProduct(@NotNull Long id) {
        var product = productService.getById(id);
        return mapProductToResponse(product);
    }

    @WebMethod
    public void updateProduct(@NotNull Long id, @Valid @NotNull UpdateProductRequest request) {
        var product = mapProductFromRequest(request);
        product.setId(id);
        productService.update(product);
    }

    @WebMethod
    public void deleteProduct(@NotNull Long id) {
        productService.delete(id);
    }

    @WebMethod
    public List<UnitOfMeasure> getUniqueMeasurements() {
        return productService.getUniqueUnitOfMeasure();
    }

    @WebMethod
    public List<Color> getColors() {
        return List.of(Color.values());
    }

    @WebMethod
    public List<Country> getCountries() {
        return List.of(Country.values());
    }

    @WebMethod
    public List<UnitOfMeasure> getMeasures() {
        return List.of(UnitOfMeasure.values());
    }

    @WebMethod
    public void deleteProductsByPrice(@Positive Integer price) {
        productService.deleteByPrice(price);
    }

    @WebMethod
    public ProductResponse getEarliestProduct() {
        var product = productService.getMinCreationDate();
        return mapProductToResponse(product);
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
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                        format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)),
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
                person.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
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