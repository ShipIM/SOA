package org.example.repository;

import jakarta.annotation.PostConstruct;
import org.example.api.repository.PersonRepository;
import org.example.model.entity.Person;
import org.example.model.enumeration.Color;
import org.example.model.enumeration.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

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
    public Person create(Person person) throws SQLException {
        var sql = "INSERT INTO person (person_name, birthday, height, eye_color, nationality) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.getPersonName());
            statement.setDate(2, person.getBirthday() != null ? Date.valueOf(person.getBirthday()) : null);
            statement.setDouble(3, person.getHeight());
            statement.setString(4, person.getEyeColor().name());
            statement.setString(5, person.getNationality().name());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    person.setId(resultSet.getLong("id"));

                    return person;
                }
            }
        }

        return null;
    }

    @Override
    public Optional<Person> getById(Long id) throws SQLException {
        var sql = "SELECT * FROM person WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Person(
                            resultSet.getLong("id"),
                            resultSet.getString("person_name"),
                            resultSet.getDate("birthday") != null ? resultSet.getDate("birthday").toLocalDate() : null,
                            resultSet.getDouble("height"),
                            Color.valueOf(resultSet.getString("eye_color")),
                            Country.valueOf(resultSet.getString("nationality"))
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(Person person) throws SQLException {
        var sql = new StringBuilder("UPDATE person SET ");
        var params = new ArrayList<>();

        if (person.getPersonName() != null) {
            sql.append("person_name = ?, ");
            params.add(person.getPersonName());
        }
        if (person.getBirthday() != null) {
            sql.append("birthday = ?, ");
            params.add(java.sql.Date.valueOf(person.getBirthday()));
        }
        if (person.getHeight() != null) {
            sql.append("height = ?, ");
            params.add(person.getHeight());
        }
        if (person.getEyeColor() != null) {
            sql.append("eye_color = ?, ");
            params.add(person.getEyeColor().name());
        }
        if (person.getNationality() != null) {
            sql.append("nationality = ?, ");
            params.add(person.getNationality().name());
        }

        if (params.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        params.add(person.getId());

        try (var statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM person WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }

}
