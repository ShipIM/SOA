package org.example.controller;

import org.example.api.service.ProductService;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;
import org.example.model.enumeration.UnitOfMeasure;
import org.example.products.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class ProductController {

    private static final String NAMESPACE_URI = "http://example.org/products";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createProductRequest")
    @ResponsePayload
    public CreateProductResponse addProduct(@RequestPayload CreateProductRequest request) {
        var product = mapProductFromRequest(request);

        product = productService.add(product);

        return mapProductToCreateProductResponse(product);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductsRequest")
    @ResponsePayload
    public GetProductsResponse findAll(@RequestPayload GetProductsRequest request) {
        var result = productService.findAll(
                request.getPage(),
                request.getSize(),
                request.getSort(),
                request.getFilter()
        );

        var products = result.getKey().stream()
                .map(this::mapProductToResponseType)
                .collect(Collectors.toList());
        var meta = mapMetaToResponse(result.getValue());

        return mapProductToGetProductsResponse(products, meta);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductRequest")
    @ResponsePayload
    public GetProductResponse getProduct(@RequestPayload GetProductRequest request) {
        var product = productService.getById(request.getId());

        return mapProductToGetProductResponse(product);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateProductRequest")
    @ResponsePayload
    public UpdateProductResponse updateProduct(@RequestPayload UpdateProductRequest request) {
        var product = mapProductFromRequest(request);
        product.setId(request.getId());

        productService.update(product);

        return new UpdateProductResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProductRequest")
    @ResponsePayload
    public DeleteProductResponse deleteProduct(@RequestPayload DeleteProductRequest request) {
        productService.delete(request.getId());

        return new DeleteProductResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUniqueMeasurementsRequest")
    @ResponsePayload
    public GetUniqueMeasurementsResponse getUniqueMeasurements(@RequestPayload GetUniqueMeasurementsRequest request) {
        var uniqueMeasurements = productService.getUniqueUnitOfMeasure();

        return mapGetUniqueMeasurementsResponse(uniqueMeasurements);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getColorsRequest")
    @ResponsePayload
    public GetColorsResponse getColors(@RequestPayload GetColorsRequest request) {
        var colors = List.of(Color.values());

        return mapGetColorsResponse(colors);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountriesRequest")
    @ResponsePayload
    public GetCountriesResponse getCountries(@RequestPayload GetCountriesRequest request) {
        var countries = List.of(Country.values());

        return mapGetCountriesResponse(countries);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMeasuresRequest")
    @ResponsePayload
    public GetMeasuresResponse getMeasures(@RequestPayload GetMeasuresRequest request) {
        var measures = List.of(UnitOfMeasure.values());

        return mapGetMeasuresResponse(measures);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProductsByPriceRequest")
    @ResponsePayload
    public DeleteProductsByPriceResponse deleteProductsByPrice(@RequestPayload DeleteProductsByPriceRequest request) {
        productService.deleteByPrice(request.getPrice());

        return new DeleteProductsByPriceResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEarliestProductRequest")
    @ResponsePayload
    public GetEarliestProductResponse getEarliestProduct(@RequestPayload GetEarliestProductRequest request) {
        var product = productService.getMinCreationDate();

        return mapProductToGetEarliestProductResponse(product);
    }

    private Product mapProductFromRequest(CreateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .unitOfMeasure(UnitOfMeasure.valueOf(request.getUnitOfMeasure().value()))
                .owner(mapPersonFromRequest(request.getOwner()));

        return product.build();
    }

    private Product mapProductFromRequest(UpdateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .unitOfMeasure(UnitOfMeasure.valueOf(request.getUnitOfMeasure().value()))
                .owner(mapPersonFromRequest(request.getOwner()));

        return product.build();
    }

    private Coordinates mapCoordinatesFromRequest(CreateCoordinatesRequest request) {
        var coordinates = Coordinates.builder()
                .x(request.getX())
                .y(request.getY());

        return coordinates.build();
    }

    private Coordinates mapCoordinatesFromRequest(UpdateCoordinatesRequest request) {
        var coordinates = Coordinates.builder()
                .x(request.getX())
                .y(request.getY());

        return coordinates.build();
    }

    private Person mapPersonFromRequest(CreatePersonRequest request) {
        if (request == null) {
            return null;
        }

        var year = request.getBirthday().getYear();
        var month = request.getBirthday().getMonth();
        var day = request.getBirthday().getDay();

        var person = Person.builder()
                .personName(request.getName())
                .birthday(LocalDate.of(year, month, day))
                .height(request.getHeight())
                .eyeColor(Color.valueOf(request.getEyeColor().value()))
                .nationality(Country.valueOf(request.getNationality().value()));

        return person.build();
    }

    private Person mapPersonFromRequest(UpdatePersonRequest request) {
        if (request == null) {
            return null;
        }

        var year = request.getBirthday().getYear();
        var month = request.getBirthday().getMonth();
        var day = request.getBirthday().getDay();

        var person = Person.builder()
                .personName(request.getName())
                .birthday(LocalDate.of(year, month, day))
                .height(request.getHeight())
                .eyeColor(Color.valueOf(request.getEyeColor().value()))
                .nationality(Country.valueOf(request.getNationality().value()));

        return person.build();
    }

    private GetEarliestProductResponse mapProductToGetEarliestProductResponse(Product product) {
        var response = new GetEarliestProductResponse();
        response.setId(product.getId());
        response.setName(product.getProductName());
        response.setCoordinates(mapCoordinatesToResponse(product.getCoordinates()));
        response.setCreationDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)));
        response.setPrice(product.getPrice());
        response.setUnitOfMeasure(org.example.products.UnitOfMeasure.valueOf(product.getUnitOfMeasure().name()));
        response.setOwner(mapPersonToResponse(product.getOwner()));

        return response;
    }

    private ProductResponseType mapProductToResponseType(Product product) {
        var response = new ProductResponseType();
        response.setId(product.getId());
        response.setName(product.getProductName());
        response.setCoordinates(mapCoordinatesToResponse(product.getCoordinates()));
        response.setCreationDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)));
        response.setPrice(product.getPrice());
        response.setUnitOfMeasure(org.example.products.UnitOfMeasure.valueOf(product.getUnitOfMeasure().name()));
        response.setOwner(mapPersonToResponse(product.getOwner()));

        return response;
    }

    private CoordinatesResponse mapCoordinatesToResponse(Coordinates coordinates) {
        if (coordinates == null) {
            return null;
        }

        var response = new CoordinatesResponse();
        response.setX(coordinates.getX());
        response.setY(coordinates.getY());

        return response;
    }

    private PersonResponse mapPersonToResponse(Person person) {
        if (person == null) {
            return null;
        }

        var response = new PersonResponse();
        response.setName(person.getPersonName());
        response.setBirthday(DateTimeFormatter.ofPattern("yyyy-MM-dd").
                format(person.getBirthday()));
        response.setHeight(person.getHeight());
        response.setEyeColor(org.example.products.Color.valueOf(person.getEyeColor().name()));
        response.setNationality(org.example.products.Country.valueOf(person.getNationality().name()));

        return response;
    }

    private MetaResponse mapMetaToResponse(Meta meta) {
        var response = new MetaResponse();
        response.setCurrentPage(meta.getCurrentPage());
        response.setTotalPages(meta.getTotalPages());
        response.setPageSize(meta.getPageSize());
        response.setTotalItems(meta.getTotalItems());

        return response;
    }

    private GetProductsResponse mapProductToGetProductsResponse(List<ProductResponseType> products, MetaResponse meta) {
        var response = new GetProductsResponse();
        response.setMeta(meta);
        response.getData().addAll(products);

        return response;
    }

    private GetColorsResponse mapGetColorsResponse(List<Color> colors) {
        var response = new GetColorsResponse();
        var mapped = colors.stream()
                .map(x -> org.example.products.Color.valueOf(x.name())).collect(Collectors.toList());
        response.getColor().addAll(mapped);

        return response;
    }

    private GetUniqueMeasurementsResponse mapGetUniqueMeasurementsResponse(List<UnitOfMeasure> measures) {
        var response = new GetUniqueMeasurementsResponse();
        var mapped = measures.stream()
                .map(x -> org.example.products.UnitOfMeasure.valueOf(x.name())).collect(Collectors.toList());
        response.getMeasure().addAll(mapped);

        return response;
    }

    private GetMeasuresResponse mapGetMeasuresResponse(List<UnitOfMeasure> measures) {
        var response = new GetMeasuresResponse();
        var mapped = measures.stream()
                .map(x -> org.example.products.UnitOfMeasure.valueOf(x.name())).collect(Collectors.toList());
        response.getMeasure().addAll(mapped);

        return response;
    }

    private GetCountriesResponse mapGetCountriesResponse(List<Country> measures) {
        var response = new GetCountriesResponse();
        var mapped = measures.stream()
                .map(x -> org.example.products.Country.valueOf(x.name())).collect(Collectors.toList());
        response.getCountry().addAll(mapped);

        return response;
    }

    private CreateProductResponse mapProductToCreateProductResponse(Product product) {
        var response = new CreateProductResponse();
        response.setId(product.getId());
        response.setName(product.getProductName());
        response.setCoordinates(mapCoordinatesToResponse(product.getCoordinates()));
        response.setCreationDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)));
        response.setPrice(product.getPrice());
        response.setUnitOfMeasure(org.example.products.UnitOfMeasure.valueOf(product.getUnitOfMeasure().name()));
        response.setOwner(mapPersonToResponse(product.getOwner()));

        return response;
    }

    private GetProductResponse mapProductToGetProductResponse(Product product) {
        var response = new GetProductResponse();
        response.setId(product.getId());
        response.setName(product.getProductName());
        response.setCoordinates(mapCoordinatesToResponse(product.getCoordinates()));
        response.setCreationDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)));
        response.setPrice(product.getPrice());
        response.setUnitOfMeasure(org.example.products.UnitOfMeasure.valueOf(product.getUnitOfMeasure().name()));
        response.setOwner(mapPersonToResponse(product.getOwner()));

        return response;
    }

}