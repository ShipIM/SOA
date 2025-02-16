package org.example.repository;

import jakarta.annotation.PostConstruct;
import org.example.api.repository.CoordinatesRepository;
import org.example.model.entity.Coordinates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class CoordinatesRepositoryImpl implements CoordinatesRepository {

    @Value("${spring.db.url}")
    private String jdbcUrl;

    @Value("${spring.db.username}")
    private String jdbcUser;

    @Value("${spring.db.password}")
    private String jdbcPassword;

    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.postgresql.Driver");

            this.connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Coordinates create(Coordinates coordinates) throws SQLException {
        var sql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";

        try (var statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, coordinates.getX());
            statement.setInt(2, coordinates.getY());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    coordinates.setId(resultSet.getLong("id"));

                    return coordinates;
                }
            }
        }

        return null;
    }

    @Override
    public Optional<Coordinates> getById(Long id) throws SQLException {
        var sql = "SELECT * FROM coordinates WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Coordinates(
                            resultSet.getLong("id"),
                            resultSet.getFloat("x"),
                            resultSet.getInt("y")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(Coordinates coordinates) throws SQLException {
        var sql = new StringBuilder("UPDATE coordinates SET ");
        var params = new ArrayList<>();

        if (coordinates.getX() != null) {
            sql.append("x = ?, ");
            params.add(coordinates.getX());
        }
        if (coordinates.getY() != null) {
            sql.append("y = ?, ");
            params.add(coordinates.getY());
        }

        if (params.isEmpty()) {
            return;
        }
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(coordinates.getId());

        try (var statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM coordinates WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }

}