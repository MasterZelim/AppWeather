package com.example.appweather.filter;

import com.example.appweather.model.HttpSessions;
import com.example.appweather.model.Users;
import com.example.appweather.repository.HttpSessionRepositoryImpl;
import com.example.appweather.repository.UsersRepositoryImpl;
import com.example.appweather.util.ConfigurationUtil;
import com.example.appweather.util.HibernateUtilTest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;

import static org.mockito.Mockito.*;

class WebFilterTest {
    private WebFilter webFilter;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterChain mockChain;
    @Mock
    private HttpSession mockHttpSession;
    @Mock
    private FilterConfig mockFilterConfig;
    private Session session;
    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webFilter = Mockito.spy(new WebFilter());
        webFilter.init(mockFilterConfig);
        HibernateUtilTest hibernateUtilTest = HibernateUtilTest.getInstance();
        SessionFactory sessionFactory = hibernateUtilTest.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        when(mockRequest.getAttribute("hibernateSession")).thenReturn(session);
        when(mockRequest.getContextPath()).thenReturn("");
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void testDoFilter_UserLoggedIn_WithinExpiry() throws IOException, ServletException {

        user = Users.builder().login("testUser").password("f").build();
        UsersRepositoryImpl usersRepository = new UsersRepositoryImpl();
        usersRepository.save(user, session);
        HttpSessions testSession = new HttpSessions();
        testSession.setSessionId("testSessionId");
        testSession.setUser(user);
        testSession.setDate(new Date(System.currentTimeMillis() + 5000));
        HttpSessionRepositoryImpl httpSessionRepository = new HttpSessionRepositoryImpl();
        httpSessionRepository.save(testSession, session);
        when(mockRequest.getSession(false)).thenReturn(mockHttpSession);
        when(mockRequest.getRequestURI()).thenReturn("/home");
        when(mockHttpSession.getId()).thenReturn("testSessionId");
        when(mockHttpSession.getAttribute("userLogin")).thenReturn("testUser");
        webFilter.doFilter(mockRequest, mockResponse, mockChain);
        verify(webFilter).httpSessionsUpdate(mockRequest, mockHttpSession);
        verify(mockChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testDoFilter_NoSession_LoginRequest() throws IOException, ServletException {
        when(mockRequest.getSession(false)).thenReturn(null);
        when(mockRequest.getRequestURI()).thenReturn("/login");
        webFilter.doFilter(mockRequest, mockResponse, mockChain);
        verify(mockChain).doFilter(mockRequest, mockResponse);
    }


}