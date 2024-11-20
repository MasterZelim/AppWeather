package com.example.appweather.service;

import com.example.appweather.dto.CoordDTO;
import com.example.appweather.dto.NameDTO;
import com.example.appweather.dto.WeatherDTO;
import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import com.example.appweather.repository.UsersRepositoryImpl;
import com.example.appweather.util.HibernateUtilTest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class LocationsServiceTest {
    private LocationsService locationsService;
    @Mock
    private HttpServletRequest mockRequest;
    private Users user;
    private WeatherDTO weatherDTO;
    Session session;
    @BeforeEach
    public void setUp() {

        locationsService = new LocationsService();
        HibernateUtilTest hibernateUtilTest = HibernateUtilTest.getInstance();
        SessionFactory sessionFactory = hibernateUtilTest.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        user = Users.builder().login("testUser").password("f").build();
        UsersRepositoryImpl usersRepository = new UsersRepositoryImpl();
        usersRepository.save(user, session);
        when(mockRequest.getAttribute("hibernateSession")).thenReturn(session);
        weatherDTO = new WeatherDTO(new CoordDTO(55.75, 37.61), null, new NameDTO("testLocation"));

    }
    @AfterEach
    void tearDown() {
        if (session.getTransaction().isActive()){
            session.getTransaction().rollback();
        }else {
            HibernateUtilTest.setSessionFactory(null);
        }
        session.close();
    }
    @Test
    public void testAddLocation_NewLocation() {

        locationsService.addLocation(user, mockRequest, weatherDTO);
        Locations savedLocation = locationsService.getLocationByNameAndUser(mockRequest, "testLocation", user);
        assertNotNull(savedLocation);
        assertEquals("testLocation", savedLocation.getName());
        assertEquals(user, savedLocation.getUser());
        assertEquals(BigDecimal.valueOf(37.61), savedLocation.getLatitude());
        assertEquals(BigDecimal.valueOf(55.75), savedLocation.getLongitude());
    }
//
    @Test
    public void testAddLocation_ExistingLocation() {

        locationsService.addLocation(user, mockRequest, weatherDTO);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            locationsService.addLocation(user, mockRequest, weatherDTO);
        });
        assertEquals("Данная локация уже добавлена!", exception.getMessage());
    }
    @Test
    public void testGetAllLocations_UserHasLocations() {

        locationsService.addLocation(user, mockRequest, weatherDTO);
        WeatherDTO anotherWeatherDTO = new WeatherDTO(new CoordDTO(55.21, 38.39), null, new NameDTO("testLocation2"));
        locationsService.addLocation(user, mockRequest, anotherWeatherDTO);
        List<Locations> locations = locationsService.getAllLocations(user, mockRequest);
        assertNotNull(locations);
        assertEquals(2, locations.size());
    }

    @Test
    public void testGetLocationByNameAndUser_LocationExists() {
        locationsService.addLocation(user, mockRequest, weatherDTO);
        Locations location = locationsService.getLocationByNameAndUser(mockRequest, "testLocation", user);
        assertNotNull(location);
        assertEquals("testLocation", location.getName());
        assertEquals(user, location.getUser());
    }
//
    @Test
    public void testGetLocationByNameAndUser_LocationNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationsService.getLocationByNameAndUser(mockRequest, "nonexistentLocation", user);
        });
        assertEquals("Локация не найдена в базе данных!", exception.getMessage());
    }

    @Test
    public void testRemoveLocation_LocationExists() {

        when(mockRequest.getParameter("nameLocation")).thenReturn("testLocation");
        when(mockRequest.getParameter("userLogin")).thenReturn(user.getLogin());
        locationsService.addLocation(user, mockRequest, weatherDTO);
        assertEquals("testLocation",locationsService.getLocationByNameAndUser(mockRequest,"testLocation",user).getName());
        locationsService.removeLocation(mockRequest);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationsService.getLocationByNameAndUser(mockRequest, "testLocation", user);
        });
        assertEquals("Локация не найдена в базе данных!", exception.getMessage());
    }
}