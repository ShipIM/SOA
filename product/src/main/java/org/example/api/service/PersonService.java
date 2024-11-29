package org.example.api.service;

import org.example.model.entity.Person;

public interface PersonService {

    Long add(Person person);

    Person getById(Long id);

    void update(Person person);

    void delete(Long id);

}
