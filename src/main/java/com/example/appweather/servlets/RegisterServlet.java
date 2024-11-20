package com.example.appweather.servlets;

import com.example.appweather.exception.UserAlreadyExistsException;
import com.example.appweather.model.Users;
import com.example.appweather.service.UsersService;
import com.example.appweather.util.ConfigurationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    UsersService usersService;

    @Override
    public void init() throws ServletException {
        usersService = new UsersService();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter("userLogin");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if (password.equals(confirmPassword)) {
            usersService.registerNewUser(request,userLogin,password);
            request.setAttribute("message", "Регистрация прошла успешно!");
            request.getRequestDispatcher(ConfigurationUtil.get("index_jsp")).forward(request, response);
        } else {
            request.setAttribute("message", "Пароли не совпадают.");
            request.getRequestDispatcher(ConfigurationUtil.get("register_jsp")).forward(request, response);
        }
    }
}
