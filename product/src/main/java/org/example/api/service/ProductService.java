package org.example.api.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.entity.Meta;
import org.example.model.entity.Product;
import org.example.model.enumeration.UnitOfMeasure;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product add(Product product);

    Pair<List<Product>, Meta> findAll(Integer page, Integer size, List<String> sort, List<String> filter);

    Product getById(Long id);

    void update(Product product);

    void delete(Long id);

    void deleteByPrice(Integer price);

    Product getMinCreationDate();

    List<UnitOfMeasure> getUniqueUnitOfMeasure();

}
