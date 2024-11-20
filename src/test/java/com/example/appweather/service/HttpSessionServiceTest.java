package com.example.appweather.service;

import com.example.appweather.model.HttpSessions;
import com.example.appweather.model.Users;
import com.example.appweather.repository.HttpSessionRepositoryImpl;
import com.example.appweather.repository.UsersRepositoryImpl;
import com.example.appweather.util.HibernateUtilTest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class HttpSessionServiceTest {
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpSession mockHttpSession;
    private Session session;
    private Users user;
    private HttpSessionService httpSessionService;

    @BeforeEach
    public void setUp() {

        HibernateUtilTest hibernateUtilTest = HibernateUtilTest.getInstance();
        SessionFactory sessionFactory = hibernateUtilTest.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        when(mockRequest.getAttribute("hibernateSession")).thenReturn(session);
        user = Users.builder().login("testUser").password("f").build();
        UsersRepositoryImpl usersRepository = new UsersRepositoryImpl();
        usersRepository.save(user, session);
        httpSessionService = new HttpSessionService();

    }
    @AfterEach
    void tearDown(){
        session.getTransaction().rollback();
        session.close();
    }
    @Test
    public void testAddHttpSessionOrUpdate_NewSession() {

        when(mockHttpSession.getId()).thenReturn("testSessionId");
        httpSessionService.addHttpSessionOrUpdate(mockRequest, mockHttpSession, user);
        HttpSessions savedHttpSession = httpSessionService.getHttpSession(mockRequest, "testSessionId");
        assertNotNull(savedHttpSession);
        assertEquals("testSessionId", savedHttpSession.getSessionId());
        assertEquals(user, savedHttpSession.getUser());
    }
    @Test
    public void testAddHttpSessionOrUpdate_UpdateExistingSession() {

        HttpSessions testSession = new HttpSessions();
        testSession.setSessionId("testSessionId");
        testSession.setUser(user);
        testSession.setDate(new Date(System.currentTimeMillis()+5000));
        HttpSessionRepositoryImpl httpSessionRepository = new HttpSessionRepositoryImpl();
        httpSessionRepository.save(testSession, session);
        HttpSessions httpSession = httpSessionService.getHttpSession(mockRequest, "testSessionId");
        assertEquals("testSessionId", httpSession.getSessionId());
        when(mockHttpSession.getId()).thenReturn("testSessionUpdate");
        httpSessionService.addHttpSessionOrUpdate(mockRequest, mockHttpSession, user);
        HttpSessions updatedSession = httpSessionService.getHttpSession(mockRequest, "testSessionUpdate");
        assertNotNull(updatedSession);
        assertEquals("testSessionUpdate", updatedSession.getSessionId());
        assertEquals(user, updatedSession.getUser());
    }
    @Test
    public void testGetHttpSession_NotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            httpSessionService.getHttpSession(mockRequest, "nonexistentSession");
        });
        assertEquals("Не найдена активная сессия для данного пользователя!", exception.getMessage());
    }
    @Test
    public void testIsHttpSessionExists_SessionExists() {
        when(mockHttpSession.getId()).thenReturn("testSessionId");
        httpSessionService.addHttpSessionOrUpdate(mockRequest, mockHttpSession, user);
        boolean sessionExists = httpSessionService.isHttpSessionExists(mockRequest, user);
        assertTrue(sessionExists);
    }
    @Test
    public void testIsHttpSessionExists_SessionDoesNotExist() {
        boolean sessionExists = httpSessionService.isHttpSessionExists(mockRequest, user);
        assertFalse(sessionExists);
    }
}