package com.example.appweather.repository;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.Optional;

public interface Repository<E,K extends Serializable> {
    void save(E entity, Session session);
    Optional<E> getById(K id, Session session);

}
