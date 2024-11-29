package org.example.api.service;

import org.example.model.entity.Coordinates;

public interface CoordinatesService {

    Long add(Coordinates coordinates);

    Coordinates getById(Long id);

    void update(Coordinates coordinates);

    void delete(Long id);

}
