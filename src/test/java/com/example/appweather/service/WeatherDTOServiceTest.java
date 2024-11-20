package com.example.appweather.service;

import com.example.appweather.dto.WeatherDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class WeatherDTOServiceTest {

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private CloseableHttpResponse mockResponse;
    @InjectMocks
    private WeatherDTOService weatherDTOService;
    @BeforeEach
    void setUp() {
        weatherDTOService = new WeatherDTOService();
    }

    @Test
    void testGetWeatherDTO() throws Exception {

        when(mockRequest.getParameter("location")).thenReturn("London");
        String apiResponse = """
        {
            "coord": {"lon": -0.13, "lat": 51.51},
            "main": {
                "temp": 15.0,
                "feels_like": 14.5,
                "temp_min": 10.0,
                "temp_max": 18.0,
                "pressure": 1012,
                "humidity": 82,
                "sea_level": 1020,
                "grnd_level": 1008
            },
            "name": "London"
        }
        """;
        HttpEntity entity = new StringEntity(apiResponse);
        when(mockResponse.getEntity()).thenReturn(entity);
        when(mockResponse.getCode()).thenReturn(200);

        WeatherDTO result = weatherDTOService.getWeatherDTO(mockRequest);

        assertNotNull(result);
        assertEquals("London", result.nameDTO().nameLocation());
        assertEquals(15.0, result.mainDTO().temp());
        assertEquals(-0.13, result.coordDTO().lon());

//        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
//        verify(mockResponse).getEntity();
//        HttpEntity capturedEntity = entityCaptor.getValue();
//        assertEquals(apiResponse, capturedEntity.toString());
    }
//    @Test
//    void testGetWeatherDTOList() throws Exception {
//        // Подготовка моков
//        Users mockUser = mock(Users.class);
//        LocationsService mockLocationsService = mock(LocationsService.class);
//        UsersService mockUsersService = mock(UsersService.class);
//
//        List<Locations> mockLocations = List.of(
//                new Locations("Paris"),
//                new Locations("Berlin")
//        );
//
//        when(mockUsersService.getUser(any(), eq("testUser"))).thenReturn(mockUser);
//        when(mockLocationsService.getAllLocations(eq(mockUser), any())).thenReturn(mockLocations);
//
//        weatherDTOService = new WeatherDTOService(mockLocationsService, mockUsersService);
//
//        // Вызов метода
//        List<WeatherDTO> result = weatherDTOService.getWeatherDTOList(mockRequest);
//
//        // Проверка результата
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("Paris", result.get(0).getNameDTO().getName());
//        assertEquals("Berlin", result.get(1).getNameDTO().getName());
//    }
}