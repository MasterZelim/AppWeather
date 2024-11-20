package com.example.appweather.servlets;

import com.example.appweather.dto.WeatherDTO;
import com.example.appweather.model.Users;
import com.example.appweather.service.LocationsService;
import com.example.appweather.service.UsersService;
import com.example.appweather.util.ConfigurationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/searchLocation")
public class SearchLocationServlet extends HttpServlet {

    private LocationsService locationsService;
    private UsersService usersService;

    @Override
    public void init() throws ServletException {
        locationsService = new LocationsService();
        usersService = new UsersService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String weatherDTOJson = request.getParameter("weatherDTO");
        Users user = usersService.getUser(request,request.getParameter("userLogin"));
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherDTO weatherDTO = objectMapper.readValue(weatherDTOJson, WeatherDTO.class);
        locationsService.addLocation(user,request, weatherDTO);

        request.getRequestDispatcher(ConfigurationUtil.get("search_jsp")).forward(request, response);


    }
}
