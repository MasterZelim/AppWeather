package com.example.appweather.repository;

import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SpecLocationsRepository {
     void save(Session session, Locations location);


    Optional <List<Locations>> getAll(Users user, Session session);


     void delete(Locations location, Session session);

   Optional<Locations> getByNameAndUser(String nameLocation, Session session,Users user);

   Optional<Locations> findByNameAndUserAndCoordinates(Session session, String name, Users user,
                                                               BigDecimal latitude, BigDecimal longitude);

}
