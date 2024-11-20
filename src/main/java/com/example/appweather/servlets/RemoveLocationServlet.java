package com.example.appweather.servlets;

import com.example.appweather.service.LocationsService;
import com.example.appweather.service.WeatherDTOService;
import com.example.appweather.util.ConfigurationUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/removeLocation")
public class RemoveLocationServlet extends HttpServlet {

    private LocationsService locationsService;
    private WeatherDTOService weatherDTOService;

    @Override
    public void init() throws ServletException {
        locationsService = new LocationsService();
        weatherDTOService = new WeatherDTOService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        locationsService.removeLocation(request);
        request.setAttribute("weatherDTOList", weatherDTOService.getWeatherDTOList(request));
        request.getRequestDispatcher(ConfigurationUtil.get("homePage_jsp")).forward(request, response);
        request.getRequestDispatcher(ConfigurationUtil.get("homePage_jsp")).forward(request, response);

    }
}
