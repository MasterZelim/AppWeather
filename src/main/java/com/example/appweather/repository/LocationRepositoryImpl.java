package com.example.appweather.repository;

import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import com.example.appweather.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LocationRepositoryImpl implements SpecLocationsRepository {

    // Добавление локации
    public void save(Session session, Locations location) {
        session.save(location);
    }

    // Получение всех локаций
    public Optional<List<Locations>> getAll(Users user, Session session) {
            return Optional.ofNullable(session.createQuery("from Locations where user=:user", Locations.class)
                                                          .setParameter("user",user).list());

    }

    // Удаление локации по id
    public void delete(Locations location, Session session) {
        session.delete(location);

    }

    @Override
    public Optional<Locations> getByNameAndUser(String nameLocation,Session session,Users user) {
        return session.createQuery("select l from Locations l where l.name=:nameLocation and l.user=:user", Locations.class)
                .setParameter("nameLocation", nameLocation)
                .setParameter("user", user).uniqueResultOptional();
    }

    public Optional<Locations> findByNameAndUserAndCoordinates(Session session, String name, Users user,
                                                               BigDecimal latitude, BigDecimal longitude){

        return session.createQuery(
                        "select l from Locations l where l.name = :name and l.user = :userId and l.latitude = :latitude and l.longitude = :longitude",
                        Locations.class)
                .setParameter("name", name)
                .setParameter("userId", user)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .uniqueResultOptional();

    }
}
