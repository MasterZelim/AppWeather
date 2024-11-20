package com.example.appweather.repository;
import com.example.appweather.model.HttpSessions;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;


import java.util.Optional;

public class HttpSessionRepositoryImpl implements SpecHttpSessionRepository<HttpSessions,String> {

    @Override
    public void save(HttpSessions httpSessions, Session session) {
            session.save(httpSessions);
    }

    @Override
    public Optional<HttpSessions> getById(String sessionId, Session session) {
        return session.createQuery("select hs from HttpSessions hs where hs.sessionId=:sessionId", HttpSessions.class)
                .setParameter("sessionId", sessionId).uniqueResultOptional();
    }

    @Override
    public Optional<HttpSessions> getByUserId(Integer userId, Session session) {
        return session.createQuery("select hs from HttpSessions hs where hs.user.id=:userId", HttpSessions.class)
                                              .setParameter("userId", userId).uniqueResultOptional();
    }

    @Override
    public void update(HttpSessions httpSessionsForUpdate,Session session) {
        session.update(httpSessionsForUpdate);
    }
}
