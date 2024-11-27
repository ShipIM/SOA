package org.example.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.api.gateway.ProductGateway;
import org.example.api.service.ProductService;
import org.example.model.entity.Meta;
import org.example.model.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    private ProductGateway productGateway;

    @Override
    public Pair<List<Product>, Meta> getProductsByPriceRange(Integer priceFrom, Integer priceTo, Integer page, Integer size) {
        var params = List.of(
                Pair.of("filter", "price[>=]=" + priceFrom),
                Pair.of("filter", "price[<=]=" + priceTo)
        );

        return productGateway.fetchProducts(params, page, size);
    }

    @Override
    public void increaseAllProductPrices(Integer increasePercent) {
        var products = productGateway.fetchProducts(Collections.emptyList(), 0, 0);

        for (Product product : products.getKey()) {
            var newPrice = (int) (product.getPrice() * (1 + increasePercent / 100.0));
            product.setPrice(newPrice);

            productGateway.updateProduct(product);
        }
    }
}
