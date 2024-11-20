package com.example.appweather.filter;

import com.example.appweather.model.HttpSessions;
import com.example.appweather.service.HttpSessionService;
import com.example.appweather.service.UsersService;
import com.example.appweather.util.ConfigurationUtil;
import com.example.appweather.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@jakarta.servlet.annotation.WebFilter("/*")
public class WebFilter implements Filter {

    private static final Logger logger = Logger.getLogger(WebFilter.class.getName());
    private HibernateUtil hibernateUtil;
    private HttpSessionService httpSessionService;
    private UsersService usersService;

    @Override
    public void init(FilterConfig filterConfig) {
        hibernateUtil = HibernateUtil.getInstance();
        httpSessionService = new HttpSessionService();
        usersService = new UsersService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long dateExpiryHttpSession = System.currentTimeMillis()+5000;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession(false);

        boolean isLoggedIn = (httpSession != null && httpSession.getAttribute("userLogin") != null);
        if (httpSession != null && isLoggedIn) {
            Session session = hibernateUtil.getSessionFactory().openSession();
            httpRequest.setAttribute("hibernateSession", session);
            HttpSessions httpSessions = httpSessionService.getHttpSession(httpRequest,httpSession.getId());
            dateExpiryHttpSession = httpSessions.getDate().getTime();
        }
        String loginURI = httpRequest.getContextPath() + "/login";
        String registerURI = httpRequest.getContextPath() + "/register";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isRegisterRequest = httpRequest.getRequestURI().equals(registerURI);
        boolean isRegisterPage = httpRequest.getRequestURI().endsWith(ConfigurationUtil.get("register_jsp"));

        if (dateExpiryHttpSession > System.currentTimeMillis()) {

             if (isLoggedIn || isLoginRequest || isRegisterPage || isRegisterRequest) {
                Session session = hibernateUtil.getSessionFactory().openSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    httpRequest.setAttribute("hibernateSession", session);
                    chain.doFilter(httpRequest, httpResponse);
                    transaction.commit();
                    transaction = session.beginTransaction();
                    if (httpRequest.getSession(false)!=null){
                        httpSessionsUpdate(httpRequest,httpSession);
                    }
                    transaction.commit();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Transaction rollback due to exception", e);
                    if (transaction != null && transaction.isActive()) {
                        transaction.rollback();
                    }
                    throw new ServletException("Ошибка при обработке транзакции.", e);
                } finally {
                    session.close();
                }

            } else {
                 httpRequest.getRequestDispatcher(ConfigurationUtil.get("index_jsp")).forward(request, response);
            }
        } else {
            if (httpSession!=null){
                httpSession.invalidate();
            }
            httpRequest.getRequestDispatcher(ConfigurationUtil.get("index_jsp")).forward(request, response);
        }
    }


    public void httpSessionsUpdate(HttpServletRequest httpRequest, HttpSession httpSession){
        httpSessionService.addHttpSessionOrUpdate(httpRequest, httpSession,usersService.getUser(httpRequest,httpSession.getAttribute("userLogin").toString()));
        logger.log(Level.INFO,httpSession.getAttribute("userLogin").toString());
    }

    @Override
    public void destroy() {
        if (hibernateUtil.getSessionFactory() != null) {
            hibernateUtil.getSessionFactory().close();
        }
    }
}