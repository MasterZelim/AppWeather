package com.example.appweather.service;

import com.example.appweather.dto.WeatherDTO;
import com.example.appweather.model.Locations;
import com.example.appweather.model.Users;
import com.example.appweather.repository.LocationRepositoryImpl;
import com.example.appweather.repository.SpecLocationsRepository;
import com.example.appweather.util.HibernateUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationsService {

    private final SpecLocationsRepository locationRepository;
    private final UsersService usersService;
    private static final Logger logger = Logger.getLogger(LocationsService.class.getName());

    public LocationsService() {
        locationRepository = new LocationRepositoryImpl();
        usersService = new UsersService();
    }

    public void addLocation(Users user, HttpServletRequest request, WeatherDTO weatherDTO) {
        Session session = (Session) request.getAttribute("hibernateSession");
        Locations location = Locations.builder()
                .name(weatherDTO.nameDTO().nameLocation())
                .user(user)
                .latitude(BigDecimal.valueOf(weatherDTO.coordDTO().lat()))
                .longitude(BigDecimal.valueOf(weatherDTO.coordDTO().lon()))
                .build();

        Optional<Locations> existingLocation;

            existingLocation = locationRepository.findByNameAndUserAndCoordinates
                    (session,location.getName(),location.getUser(),location.getLatitude().setScale(2, RoundingMode.HALF_UP),location.getLongitude().setScale(2,RoundingMode.HALF_UP));

        if (existingLocation.isPresent()){
            throw new IllegalStateException("Данная локация уже добавлена!");
        }

        locationRepository.save(session, location);
    }

    public List<Locations> getAllLocations(Users user, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("hibernateSession");
        List<Locations> locations = locationRepository.getAll(user, session).orElseThrow(() ->
                new EntityNotFoundException("Ошибка при получении локаций из базы данных"));
        return locations;
    }

    public Locations getLocationByNameAndUser(HttpServletRequest request, String nameLocation,Users user) {
        Session session = (Session) request.getAttribute("hibernateSession");
        return  locationRepository.getByNameAndUser(nameLocation, session,user)
                                               .orElseThrow(()-> new EntityNotFoundException("Локация не найдена в базе данных!"));
    }

    public void removeLocation(HttpServletRequest request) {
        Session session = (Session) request.getAttribute("hibernateSession");
        String nameLocation = request.getParameter("nameLocation");
        Users user = usersService.getUser(request,request.getParameter("userLogin"));
        Locations location = getLocationByNameAndUser(request,nameLocation,user);
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            locationRepository.delete(location, session);
            transaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Transaction rollback due to exception", e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }
}

