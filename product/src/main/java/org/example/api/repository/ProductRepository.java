package org.example.api.repository;

import org.example.model.entity.Product;
import org.example.model.enumeration.UnitOfMeasure;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {

    Product create(Product product) throws SQLException;

    List<Product> findAll(Integer page, Integer size, List<String> sort, List<String> filter) throws SQLException;

    Integer countAll(List<String> filter) throws SQLException;

    Optional<Product> getById(Long id) throws SQLException;

    void update(Product product) throws SQLException;

    void delete(Long id) throws SQLException;

    void deleteByPrice(Integer price) throws SQLException;

    Optional<Product> getMinCreationDate() throws SQLException;

    List<UnitOfMeasure> getUniqueUnitOfMeasure() throws SQLException;

}
