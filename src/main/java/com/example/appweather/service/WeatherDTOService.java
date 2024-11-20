package com.example.appweather.service;

import com.example.appweather.dto.CoordDTO;
import com.example.appweather.dto.MainDTO;
import com.example.appweather.dto.NameDTO;
import com.example.appweather.dto.WeatherDTO;
import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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

public class WeatherDTOService {
    private final LocationsService locationsService;
    private final UsersService usersService;
    ObjectMapper objectMapper;

    public WeatherDTOService() {
        usersService = new UsersService();
        locationsService = new LocationsService();
        objectMapper = new ObjectMapper();
    }

    public WeatherDTO getWeatherDTO(HttpServletRequest request) {

        String nameLocation = request.getParameter("location");
        String apiKey = "9c7605496fc381dafb6c9c2e210fe633";
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", nameLocation, apiKey);
        WeatherDTO weatherDTO;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0");

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                int responseCode = httpResponse.getCode();
                System.out.println("Response Code: " + responseCode);

                HttpEntity entity = httpResponse.getEntity();
                String responseBody = entity != null ? EntityUtils.toString(entity) : null;

                objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                JsonNode coordNode = rootNode.path("coord");
                JsonNode mainNode = rootNode.path("main");
                JsonNode nameNode = rootNode.path("name");


                CoordDTO coordDto = objectMapper.treeToValue(coordNode, CoordDTO.class);
                MainDTO mainDto = objectMapper.treeToValue(mainNode, MainDTO.class);
                NameDTO nameDTO = new NameDTO(nameNode.asText());

                weatherDTO = new WeatherDTO(coordDto, mainDto, nameDTO);


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return weatherDTO;
    }


    public List<WeatherDTO> getWeatherDTOList(HttpServletRequest request) {

        Users user = usersService.getUser(request,request.getParameter("userLogin"));
        List<Locations> locations = locationsService.getAllLocations(user, request);
        List<WeatherDTO> weatherDTOList = new ArrayList<>();

        for (Locations location : locations) {
            String apiKey = "9b92ebdcf993310a69c55941b53a0d28";
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", location.getName(), apiKey);

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("User-Agent", "Mozilla/5.0");

                try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                    int responseCode = httpResponse.getCode();
                    System.out.println("Response Code: " + responseCode);

                    HttpEntity entity = httpResponse.getEntity();
                    String responseBody = entity != null ? EntityUtils.toString(entity) : null;

                    JsonNode rootNode = objectMapper.readTree(responseBody);
                    JsonNode coordNode = rootNode.path("coord");
                    JsonNode mainNode = rootNode.path("main");
                    JsonNode nameNode = rootNode.path("name");

                    CoordDTO coordDto = objectMapper.treeToValue(coordNode, CoordDTO.class);
                    MainDTO mainDto = objectMapper.treeToValue(mainNode, MainDTO.class);
                    NameDTO nameDTO = new NameDTO(nameNode.asText());

                    WeatherDTO weatherDTO = new WeatherDTO(coordDto, mainDto, nameDTO);
                    weatherDTOList.add(weatherDTO);


                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return weatherDTOList;
    }
}

