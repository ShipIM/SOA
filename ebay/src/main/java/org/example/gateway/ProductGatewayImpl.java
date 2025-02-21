package org.example.gateway;

import com.sun.mail.iap.ConnectionException;
import org.apache.commons.lang3.tuple.Pair;
import org.example.api.gateway.ProductGateway;
import org.example.dto.coordinates.CoordinatesRequest;
import org.example.dto.coordinates.CoordinatesResponse;
import org.example.dto.meta.MetaResponse;
import org.example.dto.person.PersonRequest;
import org.example.dto.person.PersonResponse;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductRequest;
import org.example.dto.product.ProductResponse;
import org.example.exception.ServiceUnavailableException;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Meta;
import org.example.model.entity.Person;
import org.example.model.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductGatewayImpl implements ProductGateway {

    private final WebTarget webTarget;
    private final String baseUrl = "https://localhost:8090/api/v1";

    public ProductGatewayImpl() throws NoSuchAlgorithmException, KeyManagementException, ConnectionException {
        TrustManager[] noopTrustManager = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("ssl");
        sc.init(null, noopTrustManager, null);

        try {
            var client = ClientBuilder.newBuilder()
                    .sslContext(sc)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            this.webTarget = client.target(baseUrl);
        } catch (Exception e) {
            throw new ConnectionException("can't connect to the server");
        }
    }

    @Override
    public Pair<List<Product>, Meta> fetchProducts(List<Pair<String, String>> filters, Integer page, Integer size) {
        var target = webTarget.path("/products");

        if (filters != null) {
            for (var filter : filters) {
                target = target.queryParam(filter.getKey(), filter.getValue());
            }
        }
        if (page != null) {
            target = target.queryParam("page", page);
        }
        if (size != null) {
            target = target.queryParam("size", size);
        }

        var response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == 200) {
            var productsResponse = response.readEntity(ProductListResponse.class);

            var products = productsResponse.getData().stream()
                    .map(this::mapProductFromResponse)
                    .collect(Collectors.toList());
            var meta = mapMetaFromResponse(productsResponse.getMeta());

            return Pair.of(products, meta);
        } else {
            throw new ServiceUnavailableException("failed to fetch products: " + response.getStatus());
        }
    }

    @Override
    public void updateProduct(Product product) {
        var productRequest = mapProductToRequest(product);

        var target = webTarget.path("/products/" + product.getId());

        try (Response response = target
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(productRequest, MediaType.APPLICATION_JSON))) {
            if (response.getStatus() != 204) {
                throw new ServiceUnavailableException("failed to update product: " + response.getStatus());
            }
        }
    }

    private Product mapProductFromResponse(ProductResponse response) {
        return new Product(
                response.getId(),
                response.getName(),
                mapCoordinatesFromResponse(response.getCoordinates()),
                response.getCreationDate(),
                response.getPrice(),
                response.getUnitOfMeasure(),
                mapPersonFromResponse(response.getOwner())
        );
    }

    private Coordinates mapCoordinatesFromResponse(CoordinatesResponse response) {
        return new Coordinates(
                response.getX(),
                response.getY()
        );
    }

    private Person mapPersonFromResponse(PersonResponse response) {
        return new Person(
                response.getName(),
                response.getBirthday(),
                response.getHeight(),
                response.getEyeColor(),
                response.getNationality()
        );
    }

    private Meta mapMetaFromResponse(MetaResponse response) {
        return new Meta(
                response.getCurrentPage(),
                response.getTotalPages(),
                response.getPageSize(),
                response.getTotalItems()
        );
    }

    private ProductRequest mapProductToRequest(Product product) {
        return new ProductRequest(
                product.getProductName(),
                mapCoordinatesToRequest(product.getCoordinates()),
                product.getPrice(),
                product.getUnitOfMeasure(),
                mapPersonToRequest(product.getOwner())
        );
    }

    private CoordinatesRequest mapCoordinatesToRequest(Coordinates coordinates) {
        return new CoordinatesRequest(
                coordinates.getX(),
                coordinates.getY()
        );
    }

    private PersonRequest mapPersonToRequest(Person person) {
        return new PersonRequest(
                person.getPersonName(),
                person.getBirthday(),
                person.getHeight(),
                person.getEyeColor(),
                person.getNationality()
        );
    }

}
