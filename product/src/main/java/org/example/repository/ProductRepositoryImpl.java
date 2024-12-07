package org.example.repository;

import org.example.api.repository.ProductRepository;
import org.example.model.entity.Coordinates;
import org.example.model.entity.Person;
import org.example.model.entity.Product;
import org.example.model.enumeration.UnitOfMeasure;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

        product.setCreationDate(ZonedDateTime.now(ZoneOffset.UTC));

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getProductName());
            statement.setLong(2, product.getCoordinates().getId());
            statement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate().toLocalDateTime()));
            statement.setDouble(4, product.getPrice());
            if (product.getUnitOfMeasure() != null) {
                statement.setString(5, product.getUnitOfMeasure().name());
            } else {
                statement.setNull(5, java.sql.Types.VARCHAR);
            }
            if (product.getOwner() != null) {
                statement.setLong(6, product.getOwner().getId());
            } else {
                statement.setNull(6, java.sql.Types.BIGINT);
            }

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

        var sql = new StringBuilder("SELECT p.* " +
                "FROM product p " +
                "JOIN coordinates c ON p.coordinates_id = c.id " +
                "LEFT JOIN person pe ON p.owner_id = pe.id " +
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

        if (size != 0 && page != 0) {
            sql.append(" LIMIT ? OFFSET ?");
        }

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql.toString())) {

            if (size != 0 && page != 0) {
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
                            resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.of("UTC")),
                            resultSet.getInt("price"),
                            resultSet.getString("unit_of_measure") == null ? null : UnitOfMeasure.valueOf(resultSet.getString("unit_of_measure")),
                            owner
                    );

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("invalid input syntax")) {
                throw new IllegalArgumentException(e.getMessage().split(":")[1].trim());
            }

            throw e;
        }

        return products;
    }

    @Override
    public Integer countAll(List<String> filter) throws SQLException {
        var sql = new StringBuilder("SELECT p.* " +
                "FROM product p " +
                "JOIN coordinates c ON p.coordinates_id = c.id " +
                "LEFT JOIN person pe ON p.owner_id = pe.id " +
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
        } catch (SQLException e) {
            if (e.getMessage().contains("invalid input syntax")) {
                throw new IllegalArgumentException(e.getMessage().split(":")[1].trim());
            }

            throw e;
        }

        return 0;
    }

    private String mapField(String field) {
        switch (field) {
            case "id":
                return "p.id";
            case "name":
                return "product_name";
            case "creationDate":
                return "creation_date";
            case "price":
                return "price";
            case "unitOfMeasure":
                return "unit_of_measure";
            case "owner.name":
                return "person_name";
            case "owner.birthday":
                return "birthday";
            case "owner.height":
                return "height";
            case "owner.eyeColor":
                return "eye_color";
            case "owner.nationality":
                return "nationality";
            case "coordinates.x":
                return "x";
            case "coordinates.y":
                return "y";
            default:
                throw new IllegalArgumentException("unsupported field: " + field);
        }
    }

    private String parseFilter(String filter) {
        if (filter == null || !filter.contains("[") || !filter.contains("]") || !filter.contains("=")) {
            throw new IllegalArgumentException("invalid filter format, expected format: field[operator]=value");
        }

        try {
            var field = filter.substring(0, filter.indexOf("["));
            var operator = filter.substring(filter.indexOf("[") + 1, filter.indexOf("]"));
            var value = filter.substring(filter.indexOf("]") + 2);
            value = value.replace("'", "''");

            if (field.isBlank() || operator.isBlank() || value.isBlank()) {
                throw new IllegalArgumentException("invalid filter: field, operator, or value is missing");
            }

            var column = mapField(field);

            switch (operator) {
                case "=":
                case "!=":
                case "<":
                case ">":
                case "<=":
                case ">=":
                    return column + " " + operator + " '" + value + "'";
                default:
                    throw new IllegalArgumentException("unsupported operator: " + operator);
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("invalid filter format, expected format: field[operator]=value", e);
        }
    }

    private String parseSort(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("sort conditions cannot be null or empty");
        }

        var sorting = new StringBuilder();

        for (int i = 0; i < sort.size(); i++) {
            var condition = sort.get(i);

            if (condition == null || !condition.contains(":")) {
                throw new IllegalArgumentException("invalid sort format, expected format: field:direction");
            }

            try {
                var parts = condition.split(":");
                var field = parts[0];
                var direction = parts.length > 1 ? parts[1].toUpperCase() : "ASC";

                if (field.isBlank()) {
                    throw new IllegalArgumentException("field name cannot be blank in sort condition");
                }

                if (!direction.equals("ASC") && !direction.equals("DESC")) {
                    throw new IllegalArgumentException("invalid sort direction: " + direction + ". Use ASC or DESC");
                }

                var column = mapField(field);
                sorting.append(column).append(" ").append(direction);

                if (i < sort.size() - 1) {
                    sorting.append(", ");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("invalid sort format, expected format: field:direction", e);
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
                            resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.of("UTC")),
                            resultSet.getInt("price"),
                            resultSet.getString("unit_of_measure") == null ? null : UnitOfMeasure.valueOf(resultSet.getString("unit_of_measure")),
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
            params.add(Timestamp.from(product.getCreationDate().toInstant()));
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
    public void deleteByPrice(Integer price) throws SQLException {
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
                        resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.of("UTC")),
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
                if (resultSet.getString("unit_of_measure") != null) {
                    var unitOfMeasure = UnitOfMeasure
                            .valueOf(resultSet.getString("unit_of_measure"));
                    unitOfMeasures.add(unitOfMeasure);
                }
            }
        }

        return unitOfMeasures;
    }

}
