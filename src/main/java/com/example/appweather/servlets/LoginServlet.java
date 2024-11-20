package com.example.appweather.servlets;

import com.example.appweather.model.Users;
import com.example.appweather.service.HttpSessionService;
import com.example.appweather.service.UsersService;
import com.example.appweather.service.WeatherDTOService;
import com.example.appweather.util.ConfigurationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private UsersService usersService;
    private HttpSessionService httpSessionService;
    private WeatherDTOService weatherDTOService;

    @Override
    public void init() throws ServletException {
        usersService = new UsersService();
        httpSessionService = new HttpSessionService();
        weatherDTOService = new WeatherDTOService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter("userLogin");
        String password = request.getParameter("password");

        if (usersService.checkUserExists(request, userLogin)) {
            Users user = usersService.getUser(request,userLogin);
            if (user.getLogin().equals(userLogin) && user.getPassword().equals(password)) {

                HttpSession httpSession = getSessionFromCookieOrCreate(request);
                httpSessionService.addHttpSessionOrUpdate(request, httpSession, user);
                httpSession.setAttribute("userLogin",userLogin);
                Cookie sessionCookie = new Cookie("JSESSIONID", httpSession.getId());
                logger.log(Level.INFO, httpSession.getId());
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                sessionCookie.setMaxAge(3600);
                response.addCookie(sessionCookie);
                request.setAttribute("weatherDTOList", weatherDTOService.getWeatherDTOList(request));
                request.getRequestDispatcher(ConfigurationUtil.get("homePage_jsp")).forward(request, response);

            } else {
                request.setAttribute("message", "Неверный пароль!");
                request.getRequestDispatcher(ConfigurationUtil.get("index_jsp")).forward(request, response);
            }
        } else {
            request.setAttribute("message", "Неверный логин!");
            request.getRequestDispatcher(ConfigurationUtil.get("index_jsp")).forward(request, response);
        }

    }

    protected HttpSession getSessionFromCookieOrCreate(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    logger.log(Level.INFO, sessionId);
                    HttpSession httpSession = req.getSession(false);
                    if (httpSession != null && httpSession.getId().equals(sessionId)) {
                        logger.log(Level.INFO, httpSession.getId());
                        return httpSession;
                    }
                }
            }
        }
        return req.getSession();
    }
}
