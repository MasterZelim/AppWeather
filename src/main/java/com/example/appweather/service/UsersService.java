package com.example.appweather.service;

import com.example.appweather.exception.UserAlreadyExistsException;
import com.example.appweather.model.Users;
import com.example.appweather.repository.SpecUsersRepository;
import com.example.appweather.repository.UsersRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;



public class UsersService {
    private final SpecUsersRepository<Users, Integer> usersRepository;
    public UsersService() {
        usersRepository = new UsersRepositoryImpl();
    }

    public boolean checkUserExists(HttpServletRequest request, String userLogin) {
        Session session = (Session) request.getAttribute("hibernateSession");
        return usersRepository.getByLogin(userLogin, session).isPresent();
    }
    public void registerNewUser(HttpServletRequest request, String userLogin, String password) throws UserAlreadyExistsException {

        Session session = (Session) request.getAttribute("hibernateSession");
        if (!checkUserExists(request, userLogin)) {
            Users user = new Users();
            user.setLogin(userLogin);
            user.setPassword(password);
            usersRepository.save(user, session);
        } else {
                throw new UserAlreadyExistsException("Пользователь с таким именем уже существует.");
        }
    }
    public Users getUser(HttpServletRequest request,String userLogin) {
        Session session = (Session) request.getAttribute("hibernateSession");
        Users users;
        users = usersRepository.getByLogin(userLogin, session).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден!"));
        return users;
    }

}


