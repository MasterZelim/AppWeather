package com.example.appweather.repository;

import com.example.appweather.model.HttpSessions;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Optional;

public interface SpecHttpSessionRepository<E,K extends Serializable> extends Repository<E,K>{

    Optional<HttpSessions> getByUserId(Integer id, Session session);

    void update(E httpSessionsForUpdate,Session session);
}
