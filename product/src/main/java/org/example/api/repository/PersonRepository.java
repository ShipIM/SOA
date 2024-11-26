package org.example.api.repository;

import org.example.model.entity.Person;

import java.sql.SQLException;
import java.util.Optional;

public interface PersonRepository {

    Person create(Person person) throws SQLException;

    Optional<Person> getById(Long id) throws SQLException;

    void update(Person person) throws SQLException;

    void delete(Long id) throws SQLException;

}
