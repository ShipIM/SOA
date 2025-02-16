package org.example.controller;

import jakarta.xml.bind.JAXBElement;
import org.example.api.service.ProductService;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;
import org.example.model.enumeration.UnitOfMeasure;
import org.example.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.namespace.QName;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class ProductController {

    private static final String NAMESPACE_URI = "http://example.org/product";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateProductRequest")
    @ResponsePayload
    public JAXBElement<ProductResponse> addProduct(@RequestPayload JAXBElement<CreateProductRequest> request) {
        var product = mapProductFromRequest(request.getValue());

        product = productService.add(product);

        var mapped = mapProductToResponse(product);

        return new JAXBElement<>(new QName("ProductResponse"), ProductResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProductsRequest")
    @ResponsePayload
    public JAXBElement<ProductListResponse> findAll(@RequestPayload JAXBElement<GetProductsRequest> request) {
        var result = productService.findAll(
                request.getValue().getPage(),
                request.getValue().getSize(),
                request.getValue().getSort(),
                request.getValue().getFilter()
        );

        var products = result.getKey().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
        var meta = mapMetaToResponse(result.getValue());

        var mapped = mapProductListResponse(products, meta);

        return new JAXBElement<>(new QName("ProductListResponse"), ProductListResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProductRequest")
    @ResponsePayload
    public JAXBElement<ProductResponse> getProduct(@RequestPayload JAXBElement<GetProductRequest> request) {
        var product = productService.getById(request.getValue().getId());

        var mapped = mapProductToResponse(product);

        return new JAXBElement<>(new QName("ProductResponse"), ProductResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateProductRequest")
    @ResponsePayload
    public void updateProduct(@RequestPayload JAXBElement<UpdateProductRequest> request) {
        var product = mapProductFromRequest(request.getValue());
        product.setId(request.getValue().getId());

        productService.update(product);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteProductRequest")
    @ResponsePayload
    public void deleteProduct(@RequestPayload JAXBElement<DeleteProductRequest> request) {
        productService.delete(request.getValue().getId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUniqueMeasurementsRequest")
    @ResponsePayload
    public JAXBElement<UniqueMeasurementsResponse> getUniqueMeasurements(@RequestPayload JAXBElement<GetUniqueMeasurementsRequest> request) {
        var uniqueMeasurements = productService.getUniqueUnitOfMeasure();

        var mapped = mapUniqueMeasurementsResponse(uniqueMeasurements);

        return new JAXBElement<>(new QName("UniqueMeasurementsResponse"), UniqueMeasurementsResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetColorsRequest")
    @ResponsePayload
    public JAXBElement<ColorsResponse> getColors(@RequestPayload JAXBElement<GetColorsRequest> request) {
        var colors = List.of(Color.values());

        var mapped = mapColorsResponse(colors);

        return new JAXBElement<>(new QName("ColorsResponse"), ColorsResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCountriesRequest")
    @ResponsePayload
    public JAXBElement<CountriesResponse> getCountries(@RequestPayload JAXBElement<GetCountriesRequest> request) {
        var countries = List.of(Country.values());

        var mapped = mapCountriesResponse(countries);

        return new JAXBElement<>(new QName("CountriesResponse"), CountriesResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetMeasuresRequest")
    @ResponsePayload
    public JAXBElement<MeasuresResponse> getMeasures(@RequestPayload JAXBElement<GetMeasuresRequest> request) {
        var measures = List.of(UnitOfMeasure.values());

        var mapped = mapMeasuresResponse(measures);

        return new JAXBElement<>(new QName("MeasuresResponse"), MeasuresResponse.class, mapped);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteProductsByPriceRequest")
    @ResponsePayload
    public void deleteProductsByPrice(@RequestPayload JAXBElement<DeleteProductsByPriceRequest> request) {
        productService.deleteByPrice(request.getValue().getPrice());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetEarliestProductRequest")
    @ResponsePayload
    public JAXBElement<ProductResponse> getEarliestProduct(@RequestPayload JAXBElement<GetEarliestProductRequest> request) {
        var product = productService.getMinCreationDate();

        var mapped = mapProductToResponse(product);

        return new JAXBElement<>(new QName("ProductResponse"), ProductResponse.class, mapped);
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

    private ProductResponse mapProductToResponse(Product product) {
        var response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getProductName());
        response.setCoordinates(mapCoordinatesToResponse(product.getCoordinates()));
        response.setCreationDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:m:ss.SSSXXX").withZone(ZoneId.from(ZoneOffset.UTC)).
                format(product.getCreationDate().toInstant().atZone(ZoneOffset.UTC)));
        response.setPrice(product.getPrice());
        response.setUnitOfMeasure(org.example.product.UnitOfMeasure.valueOf(product.getUnitOfMeasure().name()));
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
        response.setEyeColor(org.example.product.Color.valueOf(person.getEyeColor().name()));
        response.setNationality(org.example.product.Country.valueOf(person.getNationality().name()));

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

    private ProductListResponse mapProductListResponse(List<ProductResponse> products, MetaResponse meta) {
        var response = new ProductListResponse();
        response.setMeta(meta);
        response.getData().addAll(products);

        return response;
    }

    private UniqueMeasurementsResponse mapUniqueMeasurementsResponse(List<UnitOfMeasure> measurements) {
        var response = new UniqueMeasurementsResponse();
        var mapped = measurements.stream()
                .map(x -> org.example.product.UnitOfMeasure.valueOf(x.name()))
                .collect(Collectors.toList());
        response.getUnitOfMeasure().addAll(mapped);

        return response;
    }

    private ColorsResponse mapColorsResponse(List<Color> colors) {
        var response = new ColorsResponse();
        var mapped = colors.stream()
                .map(x -> org.example.product.Color.valueOf(x.name()))
                .collect(Collectors.toList());
        response.getColor().addAll(mapped);

        return response;
    }

    private MeasuresResponse mapMeasuresResponse(List<UnitOfMeasure> measures) {
        var response = new MeasuresResponse();
        var mapped = measures.stream()
                .map(x -> org.example.product.UnitOfMeasure.valueOf(x.name()))
                .collect(Collectors.toList());
        response.getMeasure().addAll(mapped);

        return response;
    }

    private CountriesResponse mapCountriesResponse(List<Country> measures) {
        var response = new CountriesResponse();
        var mapped = measures.stream()
                .map(x -> org.example.product.Country.valueOf(x.name()))
                .collect(Collectors.toList());
        response.getCountry().addAll(mapped);

        return response;
    }

}