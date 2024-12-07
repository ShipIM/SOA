package org.example.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.api.repository.CoordinatesRepository;
import org.example.api.repository.PersonRepository;
import org.example.api.repository.ProductRepository;
import org.example.api.service.ProductService;
import org.example.exception.BadRequestException;
import org.example.exception.InternalServerErrorException;
import org.example.exception.NotFoundException;
import org.example.model.entity.Meta;
import org.example.model.entity.Product;
import org.example.model.enumeration.UnitOfMeasure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private CoordinatesRepository coordinatesRepository;

    @Override
    public Product add(Product product) {
        try {
            var coordinates = coordinatesRepository.create(product.getCoordinates());
            product.setCoordinates(coordinates);

            if (product.getOwner() != null) {
                var person = personRepository.create(product.getOwner());
                product.setOwner(person);
            }

            return productRepository.create(product);
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on adding product: " + e.getMessage());
        }
    }

    @Override
    public Pair<List<Product>, Meta> findAll(Integer page, Integer size, List<String> sort, List<String> filter) {
        try {
            var products = productRepository.findAll(page, size, sort, filter);
            for (var product : products) {
                var coordinates = coordinatesRepository.getById(product.getCoordinates().getId())
                        .orElseThrow(() -> new NotFoundException("there is no coordinates with such an id"));
                product.setCoordinates(coordinates);

                if (product.getOwner().getId() != 0) {
                    var person = personRepository.getById(product.getOwner().getId())
                            .orElseThrow(() -> new NotFoundException("there is no person with such an id " + product.getOwner().getId()));
                    product.setOwner(person);
                } else {
                    product.setOwner(null);
                }
            }

            var totalItems = productRepository.countAll(filter);
            var totalPages = size == 0 ? null : (int) Math.ceil((double) totalItems / size);

            var meta = new Meta(
                    size == 0 || page == 0 ? null : page,
                    totalPages,
                    size == 0 || page == 0 ? null : size,
                    totalItems
            );

            return Pair.of(products, meta);
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on retrieving products: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Product getById(Long id) {
        try {
            var product = productRepository.getById(id)
                    .orElseThrow(() -> new NotFoundException("there is no product with such an id"));

            var coordinates = coordinatesRepository.getById(product.getCoordinates().getId())
                    .orElseThrow(() -> new NotFoundException("there is no coordinates with such an id"));
            product.setCoordinates(coordinates);

            if (product.getOwner().getId() != 0) {
                var person = personRepository.getById(product.getOwner().getId())
                        .orElseThrow(() -> new NotFoundException("there is no person with such an id"));
                product.setOwner(person);
            } else {
                product.setOwner(null);
            }

            return product;
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on retrieving product: " + e.getMessage());
        }
    }

    @Override
    public void update(Product product) {
        try {
            var retrievedProduct = getById(product.getId());

            if (product.getCoordinates() != null) {
                product.getCoordinates().setId(retrievedProduct.getCoordinates().getId());
                coordinatesRepository.update(product.getCoordinates());
            }

            if (retrievedProduct.getOwner().getId() != 0 && product.getOwner() != null) {
                product.getOwner().setId(retrievedProduct.getOwner().getId());
                personRepository.update(product.getOwner());
            }

            productRepository.update(product);
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on updating product: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            var retrievedProduct = getById(id);

            productRepository.delete(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on deleting product: " + e.getMessage());
        }
    }

    @Override
    public void deleteByPrice(Integer price) {
        try {
            productRepository.deleteByPrice(price);
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on deleting product: " + e.getMessage());
        }
    }

    @Override
    public Product getMinCreationDate() {
        try {
            var product = productRepository.getMinCreationDate()
                    .orElseThrow(() -> new NotFoundException("there is no product with such an id"));

            var coordinates = coordinatesRepository.getById(product.getCoordinates().getId())
                    .orElseThrow(() -> new NotFoundException("there is no coordinates with such an id"));
            product.setCoordinates(coordinates);

            var person = personRepository.getById(product.getOwner().getId())
                    .orElseThrow(() -> new NotFoundException("there is no person with such an id"));
            product.setOwner(person);

            return product;
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on retrieving product: " + e.getMessage());
        }
    }

    @Override
    public List<UnitOfMeasure> getUniqueUnitOfMeasure() {
        try {
            return productRepository.getUniqueUnitOfMeasure();
        } catch (SQLException e) {
            throw new InternalServerErrorException("something went wrong on retrieving measures: " + e.getMessage());
        }
    }

}
