package org.example.repository;

import org.example.api.repository.ProductRepository;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.UnitOfMeasure;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    @Resource(lookup = "java:/jdbc/datasource")
    private DataSource dataSource;

    @Override
    public Product create(Product product) throws SQLException {
        var sql = "INSERT INTO product (product_name, coordinates_id, creation_date, price, unit_of_measure, owner_id) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        product.setCreationDate(LocalDate.now());

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getProductName());
            statement.setLong(2, product.getCoordinates().getId());
            statement.setDate(3, Date.valueOf(product.getCreationDate()));
            statement.setDouble(4, product.getPrice());
            statement.setString(5, product.getUnitOfMeasure().name());
            statement.setLong(6, product.getOwner().getId());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product.setId(resultSet.getLong("id"));

                    return product;
                }
            }
        }

        return null;
    }

    @Override
    public List<Product> findAll(Integer page, Integer size, List<String> sort, List<String> filter) throws SQLException {
        var products = new ArrayList<Product>();

        var sql = new StringBuilder("SELECT p.*, c.x AS coord_x, c.y AS coord_y, " +
                "pe.person_name, pe.birthday, pe.height, " +
                "pe.eye_color, pe.nationality " +
                "FROM product p " +
                "JOIN coordinates c ON p.coordinates_id = c.id " +
                "JOIN person pe ON p.owner_id = pe.id " +
                "WHERE 1=1");

        if (filter != null) {
            filter = filter.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
        if (filter != null && !filter.isEmpty()) {
            for (var filterCondition : filter) {
                var parsedCondition = parseFilter(filterCondition);
                sql.append(" AND ").append(parsedCondition);
            }
        }

        if (sort != null) {
            sort = sort.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
        if (sort != null && !sort.isEmpty()) {
            sql.append(" ORDER BY ").append(parseSort(sort));
        }

        if (size != null && page != null) {
            sql.append(" LIMIT ? OFFSET ?");
        }

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql.toString())) {

            if (size != null && page != null) {
                statement.setInt(1, size);
                statement.setInt(2, (page - 1) * size);
            }

            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var coordinates = Coordinates.builder()
                            .id(resultSet.getLong("coordinates_id"))
                            .build();

                    var owner = Person.builder()
                            .id(resultSet.getLong("owner_id"))
                            .build();

                    var product = new Product(
                            resultSet.getLong("id"),
                            resultSet.getString("product_name"),
                            coordinates,
                            resultSet.getDate("creation_date").toLocalDate(),
                            resultSet.getInt("price"),
                            UnitOfMeasure.valueOf(resultSet.getString("unit_of_measure")),
                            owner
                    );

                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    public Integer countAll(List<String> filter) throws SQLException {
        var sql = new StringBuilder("SELECT COUNT(*) " +
                "FROM product p " +
                "JOIN coordinates c ON p.coordinates_id = c.id " +
                "JOIN person pe ON p.owner_id = pe.id " +
                "WHERE 1=1");

        if (filter != null) {
            filter = filter.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
        if (filter != null && !filter.isEmpty()) {
            for (var filterCondition : filter) {
                var parsedCondition = parseFilter(filterCondition);
                sql.append(" AND ").append(parsedCondition);
            }
        }

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql.toString())) {

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    private String parseFilter(String filter) {
        var field = filter.substring(0, filter.indexOf("["));
        var operator = filter.substring(filter.indexOf("[") + 1, filter.indexOf("]"));
        var value = filter.substring(filter.indexOf("]") + 2);

        switch (operator) {
            case "=":
            case "!=":
            case "<":
            case ">":
            case "<=":
            case ">=":
                return field + " " + operator + " '" + value + "'";
            default:
                throw new IllegalArgumentException("unsupported operator: " + operator);
        }
    }

    private String parseSort(List<String> sort) {
        var sorting = new StringBuilder();

        for (int i = 0; i < sort.size(); i++) {
            var parts = sort.get(i).split(":");
            var field = parts[0];
            var direction = parts.length > 1 ? parts[1].toUpperCase() : "ASC";

            if (!direction.equals("ASC") && !direction.equals("DESC")) {
                throw new IllegalArgumentException("invalid sort direction: " + direction);
            }

            sorting.append(field).append(" ").append(direction);

            if (i < sort.size() - 1) {
                sorting.append(", ");
            }
        }

        return sorting.toString();
    }

    @Override
    public Optional<Product> getById(Long id) throws SQLException {
        var sql = "SELECT * FROM product WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var coordinates = Coordinates.builder()
                            .id(resultSet.getLong("coordinates_id"))
                            .build();

                    var owner = Person.builder()
                            .id(resultSet.getLong("owner_id"))
                            .build();

                    return Optional.of(new Product(
                            resultSet.getLong("id"),
                            resultSet.getString("product_name"),
                            coordinates,
                            resultSet.getDate("creation_date").toLocalDate(),
                            resultSet.getInt("price"),
                            UnitOfMeasure.valueOf(resultSet.getString("unit_of_measure")),
                            owner
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(Product product) throws SQLException {
        var sql = new StringBuilder("UPDATE product SET ");
        var params = new ArrayList<>();

        if (product.getProductName() != null) {
            sql.append("product_name = ?, ");
            params.add(product.getProductName());
        }
        if (product.getCoordinates() != null && product.getCoordinates().getId() != null) {
            sql.append("coordinates_id = ?, ");
            params.add(product.getCoordinates().getId());
        }
        if (product.getCreationDate() != null) {
            sql.append("creation_date = ?, ");
            params.add(java.sql.Date.valueOf(product.getCreationDate()));
        }
        if (product.getPrice() != null) {
            sql.append("price = ?, ");
            params.add(product.getPrice());
        }
        if (product.getUnitOfMeasure() != null) {
            sql.append("unit_of_measure = ?, ");
            params.add(product.getUnitOfMeasure().name());
        }
        if (product.getOwner() != null && product.getOwner().getId() != null) {
            sql.append("owner_id = ?, ");
            params.add(product.getOwner().getId());
        }

        if (params.isEmpty()) {
            return;
        }
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(product.getId());

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM product WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteByPrice(double price) throws SQLException {
        var sql = "DELETE FROM product WHERE price = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, price);

            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Product> getMinCreationDate() throws SQLException {
        var sql = "SELECT * FROM product ORDER BY creation_date LIMIT 1";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                var coordinates = Coordinates.builder()
                        .id(resultSet.getLong("coordinates_id"))
                        .build();

                var owner = Person.builder()
                        .id(resultSet.getLong("owner_id"))
                        .build();

                return Optional.of(new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("product_name"),
                        coordinates,
                        resultSet.getDate("creation_date").toLocalDate(),
                        resultSet.getInt("price"),
                        UnitOfMeasure.valueOf(resultSet.getString("unit_of_measure")),
                        owner
                ));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<UnitOfMeasure> getUniqueUnitOfMeasure() throws SQLException {
        var sql = "SELECT DISTINCT unit_of_measure FROM product";
        var unitOfMeasures = new ArrayList<UnitOfMeasure>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var unitOfMeasure = UnitOfMeasure
                        .valueOf(resultSet.getString("unit_of_measure"));
                unitOfMeasures.add(unitOfMeasure);
            }
        }

        return unitOfMeasures;
    }

}
