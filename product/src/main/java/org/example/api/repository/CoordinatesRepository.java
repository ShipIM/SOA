package org.example.api.repository;

import org.example.model.entity.Coordinates;

import java.sql.SQLException;
import java.util.Optional;

public interface CoordinatesRepository {

    Coordinates create(Coordinates coordinates) throws SQLException;

    Optional<Coordinates> getById(Long id) throws SQLException;

    void update(Coordinates coordinates) throws SQLException;

    void delete(Long id) throws SQLException;

}