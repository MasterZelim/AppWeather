package com.example.appweather.service;

import com.example.appweather.model.HttpSessions;
import com.example.appweather.model.Users;
import com.example.appweather.repository.HttpSessionRepositoryImpl;
import com.example.appweather.repository.SpecHttpSessionRepository;
import com.example.appweather.util.HibernateUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpSessionService {

    private final SpecHttpSessionRepository<HttpSessions, String> httpSessionRepository;
    private final HibernateUtil hibernateUtil;
    private static final Logger logger = Logger.getLogger(LocationsService.class.getName());

    public HttpSessionService() {
        httpSessionRepository = new HttpSessionRepositoryImpl();
        hibernateUtil = HibernateUtil.getInstance();
    }
    public void addHttpSessionOrUpdate(HttpServletRequest request, HttpSession httpSession, Users user) {
        Session session = (Session) request.getAttribute("hibernateSession");
        HttpSessions httpSessions = HttpSessions.builder()
                .sessionId(httpSession.getId())
                .user(user)
                .date(new Date(httpSession.getMaxInactiveInterval() * 1000L + System.currentTimeMillis())).build();
        if (isHttpSessionExists(request, user)) {
                HttpSessions httpSessionsForUpdate = httpSessionRepository.getByUserId(user.getId(), session)
                        .orElseThrow(() -> new EntityNotFoundException("Не найдена активная сессия для данного пользователя!"));
                httpSessionsForUpdate.setSessionId(httpSession.getId());
                httpSessionsForUpdate.setDate(new Date(httpSession.getMaxInactiveInterval() * 50 + System.currentTimeMillis()));
                httpSessionRepository.update(httpSessionsForUpdate, session);
        } else {
                httpSessionRepository.save(httpSessions, session);
        }
    }
    public HttpSessions getHttpSession(HttpServletRequest request,String sessionId) {
        Session session = (Session) request.getAttribute("hibernateSession");
        HttpSessions httpSessions;
            httpSessions = httpSessionRepository.getById(sessionId, session).orElseThrow(() ->
                    new EntityNotFoundException("Не найдена активная сессия для данного пользователя!"));
        return httpSessions;
    }
    public boolean isHttpSessionExists(HttpServletRequest request, Users users) {
        Session session = (Session) request.getAttribute("hibernateSession");
        boolean isHttpSessionExists;
            isHttpSessionExists =  httpSessionRepository.getByUserId(users.getId(), session).isPresent();
       return isHttpSessionExists;
    }
}


