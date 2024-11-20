package com.example.appweather.repository;

import com.example.appweather.model.Users;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface SpecUsersRepository<E,K extends Serializable> extends Repository<E,K> {
    Optional<List<E>> getAllUsers(Session session);
    Optional<Users> getByLogin(String userLogin, Session session);

}
