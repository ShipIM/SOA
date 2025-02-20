package org.example.controller;

import org.example.api.service.ProductService;
import org.example.exception.APIException;
import org.example.exception.BadRequestException;
import org.example.exception.InternalServerErrorException;
import org.example.exception.NotFoundException;
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
        try {
            validateCreateProductRequest(request);

            var product = mapProductFromRequest(request);

            product = productService.add(product);

            return mapProductToCreateProductResponse(product);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductsRequest")
    @ResponsePayload
    public GetProductsResponse findAll(@RequestPayload GetProductsRequest request) {
        try {
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
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductRequest")
    @ResponsePayload
    public GetProductResponse getProduct(@RequestPayload GetProductRequest request) {
        try {
            var product = productService.getById(request.getId());

            return mapProductToGetProductResponse(product);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateProductRequest")
    @ResponsePayload
    public UpdateProductResponse updateProduct(@RequestPayload UpdateProductRequest request) {
        try {
            validateUpdateProductRequest(request);

            var product = mapProductFromRequest(request);
            product.setId(request.getId());

            productService.update(product);

            var response = new UpdateProductResponse();
            response.setCode("204");

            return response;
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProductRequest")
    @ResponsePayload
    public DeleteProductResponse deleteProduct(@RequestPayload DeleteProductRequest request) {
        try {
            productService.delete(request.getId());

            var response = new DeleteProductResponse();
            response.setCode("204");

            return response;
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUniqueMeasurementsRequest")
    @ResponsePayload
    public GetUniqueMeasurementsResponse getUniqueMeasurements(@RequestPayload GetUniqueMeasurementsRequest request) {
        try {
            var uniqueMeasurements = productService.getUniqueUnitOfMeasure();

            return mapGetUniqueMeasurementsResponse(uniqueMeasurements);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getColorsRequest")
    @ResponsePayload
    public GetColorsResponse getColors(@RequestPayload GetColorsRequest request) {
        try {
            var colors = List.of(Color.values());

            return mapGetColorsResponse(colors);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountriesRequest")
    @ResponsePayload
    public GetCountriesResponse getCountries(@RequestPayload GetCountriesRequest request) {
        try {
            var countries = List.of(Country.values());

            return mapGetCountriesResponse(countries);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMeasuresRequest")
    @ResponsePayload
    public GetMeasuresResponse getMeasures(@RequestPayload GetMeasuresRequest request) {
        try {
            var measures = List.of(UnitOfMeasure.values());

            return mapGetMeasuresResponse(measures);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProductsByPriceRequest")
    @ResponsePayload
    public DeleteProductsByPriceResponse deleteProductsByPrice(@RequestPayload DeleteProductsByPriceRequest request) {
        try {
            productService.deleteByPrice(request.getPrice());

            var response = new DeleteProductsByPriceResponse();
            response.setCode("204");

            return response;
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEarliestProductRequest")
    @ResponsePayload
    public GetEarliestProductResponse getEarliestProduct(@RequestPayload GetEarliestProductRequest request) {
        try {
            var product = productService.getMinCreationDate();

            return mapProductToGetEarliestProductResponse(product);
        } catch (IllegalArgumentException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("422");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (BadRequestException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (NotFoundException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (InternalServerErrorException e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode(Integer.toString(e.getStatusCode()));
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse();
            errorResponse.setCode("500");
            errorResponse.setMessage(e.getMessage());

            throw new APIException("Error", errorResponse);
        }
    }

    private Product mapProductFromRequest(CreateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .owner(mapPersonFromRequest(request.getOwner()));

        if (request.getUnitOfMeasure() != null) {
            product.unitOfMeasure(UnitOfMeasure.valueOf(request.getUnitOfMeasure().value()));
        }

        return product.build();
    }

    private Product mapProductFromRequest(UpdateProductRequest request) {
        var product = Product.builder()
                .productName(request.getName())
                .coordinates(mapCoordinatesFromRequest(request.getCoordinates()))
                .price(request.getPrice())
                .owner(mapPersonFromRequest(request.getOwner()));

        if (request.getUnitOfMeasure() != null) {
            product.unitOfMeasure(UnitOfMeasure.valueOf(request.getUnitOfMeasure().value()));
        }

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

        var person = Person.builder()
                .personName(request.getName())
                .height(request.getHeight())
                .eyeColor(Color.valueOf(request.getEyeColor().value()))
                .nationality(Country.valueOf(request.getNationality().value()));

        if (request.getBirthday() != null) {
            var year = request.getBirthday().getYear();
            var month = request.getBirthday().getMonth();
            var day = request.getBirthday().getDay();

            person.birthday(LocalDate.of(year, month, day));
        }


        return person.build();
    }

    private Person mapPersonFromRequest(UpdatePersonRequest request) {
        if (request == null) {
            return null;
        }

        var person = Person.builder()
                .personName(request.getName())
                .height(request.getHeight())
                .eyeColor(Color.valueOf(request.getEyeColor().value()))
                .nationality(Country.valueOf(request.getNationality().value()));

        if (request.getBirthday() != null) {
            var year = request.getBirthday().getYear();
            var month = request.getBirthday().getMonth();
            var day = request.getBirthday().getDay();

            person.birthday(LocalDate.of(year, month, day));
        }

        return person.build();
    }

    private GetEarliestProductResponse mapProductToGetEarliestProductResponse(Product product) {
        var response = new GetEarliestProductResponse();
        response.setCode("200");
        response.setBody(mapProductToGetEarliestProductResponseType(product));

        return response;
    }

    private GetEarliestProductResponseType mapProductToGetEarliestProductResponseType(Product product) {
        var response = new GetEarliestProductResponseType();
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
        if (person.getBirthday() != null) {
            response.setBirthday(DateTimeFormatter.ofPattern("yyyy-MM-dd").
                    format(person.getBirthday()));
        }
        response.setHeight(person.getHeight());
        response.setEyeColor(org.example.products.Color.valueOf(person.getEyeColor().name()));
        response.setNationality(org.example.products.Country.valueOf(person.getNationality().name()));

        return response;
    }

    private MetaResponseType mapMetaToResponse(Meta meta) {
        var response = new MetaResponseType();
        response.setCurrentPage(meta.getCurrentPage() == null ? 0 : meta.getCurrentPage());
        response.setTotalPages(meta.getTotalPages() == null ? 0 : meta.getTotalPages());
        response.setPageSize(meta.getPageSize() == null ? 0 : meta.getPageSize());
        response.setTotalItems(meta.getTotalItems() == null ? 0 : meta.getTotalItems());

        return response;
    }

    private GetProductsResponse mapProductToGetProductsResponse(List<ProductResponseType> products, MetaResponseType meta) {
        var response = new GetProductsResponse();
        response.setCode("200");
        response.setBody(mapProductToGetProductsResponseType(products, meta));

        return response;
    }

    private GetProductsResponseType mapProductToGetProductsResponseType(List<ProductResponseType> products, MetaResponseType meta) {
        var response = new GetProductsResponseType();
        response.setMeta(meta);
        response.getData().addAll(products);

        return response;
    }

    private GetColorsResponse mapGetColorsResponse(List<Color> colors) {
        var response = new GetColorsResponse();
        response.setCode("200");
        response.setBody(mapGetColorsResponseType(colors));

        return response;
    }

    private GetColorsResponseType mapGetColorsResponseType(List<Color> colors) {
        var response = new GetColorsResponseType();
        var mapped = colors.stream()
                .map(x -> org.example.products.Color.valueOf(x.name())).collect(Collectors.toList());
        response.getColor().addAll(mapped);

        return response;
    }

    private GetUniqueMeasurementsResponse mapGetUniqueMeasurementsResponse(List<UnitOfMeasure> measures) {
        var response = new GetUniqueMeasurementsResponse();
        response.setCode("200");
        response.setBody(mapGetUniqueMeasurementsResponseType(measures));

        return response;
    }

    private GetUniqueMeasurementsResponseType mapGetUniqueMeasurementsResponseType(List<UnitOfMeasure> measures) {
        var response = new GetUniqueMeasurementsResponseType();
        var mapped = measures.stream()
                .map(x -> org.example.products.UnitOfMeasure.valueOf(x.name())).collect(Collectors.toList());
        response.getMeasure().addAll(mapped);

        return response;
    }

    private GetMeasuresResponse mapGetMeasuresResponse(List<UnitOfMeasure> measures) {
        var response = new GetMeasuresResponse();
        response.setCode("200");
        response.setBody(mapGetMeasuresResponseType(measures));

        return response;
    }

    private GetMeasuresResponseType mapGetMeasuresResponseType(List<UnitOfMeasure> measures) {
        var response = new GetMeasuresResponseType();
        var mapped = measures.stream()
                .map(x -> org.example.products.UnitOfMeasure.valueOf(x.name())).collect(Collectors.toList());
        response.getMeasure().addAll(mapped);

        return response;
    }

    private GetCountriesResponse mapGetCountriesResponse(List<Country> countries) {
        var response = new GetCountriesResponse();
        response.setCode("200");
        response.setBody(mapGetCountriesResponseType(countries));

        return response;
    }

    private GetCountriesResponseType mapGetCountriesResponseType(List<Country> countries) {
        var response = new GetCountriesResponseType();
        var mapped = countries.stream()
                .map(x -> org.example.products.Country.valueOf(x.name())).collect(Collectors.toList());
        response.getCountry().addAll(mapped);

        return response;
    }

    private CreateProductResponse mapProductToCreateProductResponse(Product product) {
        var response = new CreateProductResponse();
        response.setCode("201");
        response.setBody(mapProductToCreateProductResponseType(product));

        return response;
    }

    private CreateProductResponseType mapProductToCreateProductResponseType(Product product) {
        var response = new CreateProductResponseType();
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
        response.setCode("200");
        response.setBody(mapProductToGetProductResponseType(product));

        return response;
    }

    private GetProductResponseType mapProductToGetProductResponseType(Product product) {
        var response = new GetProductResponseType();
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

    private void validateCreateProductRequest(CreateProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateProductRequest cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (request.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        validateCreateCoordinatesRequest(request.getCoordinates());

        if (request.getUnitOfMeasure() == null) {
            throw new IllegalArgumentException("Product unit of measure cannot be null or illegal value");
        }

        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (request.getOwner() != null) {
            validateCreatePersonRequest(request.getOwner());
        }
    }

    private void validateCreateCoordinatesRequest(CreateCoordinatesRequest coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }

        if (coordinates.getX() > 39) {
            throw new IllegalArgumentException("Coordinates x must be less than or equal to 39");
        }

        if (coordinates.getY() > 905) {
            throw new IllegalArgumentException("Coordinates y must be less than or equal to 905");
        }
    }

    private void validateCreatePersonRequest(CreatePersonRequest person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }

        if (person.getHeight() <= 0) {
            throw new IllegalArgumentException("Person height must be greater than 0");
        }

        if (person.getEyeColor() == null) {
            throw new IllegalArgumentException("Person eye color cannot be null or illegal value");
        }

        if (person.getNationality() == null) {
            throw new IllegalArgumentException("Person nationality cannot be null or illegal value");
        }
    }

    private void validateUpdateProductRequest(UpdateProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateProductRequest cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (request.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        validateUpdateCoordinatesRequest(request.getCoordinates());

        if (request.getUnitOfMeasure() == null) {
            throw new IllegalArgumentException("Product unit of measure cannot be null or illegal value");
        }

        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (request.getOwner() != null) {
            validateUpdatePersonRequest(request.getOwner());
        }
    }

    private void validateUpdateCoordinatesRequest(UpdateCoordinatesRequest coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }

        if (coordinates.getX() > 39) {
            throw new IllegalArgumentException("Coordinates x must be less than or equal to 39");
        }

        if (coordinates.getY() > 905) {
            throw new IllegalArgumentException("Coordinates y must be less than or equal to 905");
        }
    }

    private void validateUpdatePersonRequest(UpdatePersonRequest person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }

        if (person.getHeight() <= 0) {
            throw new IllegalArgumentException("Person height must be greater than 0");
        }

        if (person.getEyeColor() == null) {
            throw new IllegalArgumentException("Person eye color cannot be null or illegal value");
        }

        if (person.getNationality() == null) {
            throw new IllegalArgumentException("Person nationality cannot be null or illegal value");
        }
    }

}