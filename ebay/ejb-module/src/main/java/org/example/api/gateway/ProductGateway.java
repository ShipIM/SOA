package org.example.api.gateway;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.entity.Meta;
import org.example.model.entity.Product;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ProductGateway {

    Pair<List<Product>, Meta> fetchProducts(List<Pair<String, String>> params, Integer page, Integer size);

    void updateProduct(Product product);

}
