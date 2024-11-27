package org.example.api.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.entity.Meta;
import org.example.model.entity.Product;

import java.util.List;

public interface ProductService {

    Pair<List<Product>, Meta> getProductsByPriceRange(Integer priceFrom, Integer priceTo, Integer page, Integer size);

    void increaseAllProductPrices(Integer increasePercent);

}
