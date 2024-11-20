package com.example.appweather.servlets;

import com.example.appweather.dto.CoordDTO;
import com.example.appweather.dto.MainDTO;
import com.example.appweather.dto.NameDTO;
import com.example.appweather.dto.WeatherDTO;
import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import com.example.appweather.service.LocationsService;
import com.example.appweather.service.UsersService;
import com.example.appweather.service.WeatherDTOService;
import com.example.appweather.util.ConfigurationUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private WeatherDTOService weatherDTOService;
    @Override
    public void init() {
        weatherDTOService = new WeatherDTOService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("weatherDTO", weatherDTOService.getWeatherDTO(request));
        request.getRequestDispatcher(ConfigurationUtil.get("search_jsp")).forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("weatherDTOList", weatherDTOService.getWeatherDTOList(request));
        request.getRequestDispatcher(ConfigurationUtil.get("homePage_jsp")).forward(request, response);
    }


}