package com.example.appweather.repository;

import com.example.appweather.model.Users;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UsersRepositoryImpl implements SpecUsersRepository<Users, Integer> {
    @Override
    public void save(Users user, Session session) {
        session.save(user);
    }

    @Override
    public Optional<Users> getById(Integer id, Session session) {
        return Optional.ofNullable(session.get(Users.class, id));
    }

    //
    @Override
    public Optional<Users> getByLogin(String userLogin, Session session) {
        return session.createQuery("select p from Users p where p.login=:name", Users.class).setParameter
                ("name", userLogin).uniqueResultOptional();
    }

    @Override
    public Optional<List<Users>> getAllUsers(Session session) {
        return Optional.ofNullable(session.createQuery("from Users ", Users.class).getResultList());
    }
}
