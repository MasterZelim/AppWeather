package com.example.appweather.service;
import com.example.appweather.exception.UserAlreadyExistsException;
import com.example.appweather.model.Users;
import com.example.appweather.repository.UsersRepositoryImpl;
import com.example.appweather.util.HibernateUtilTest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.hibernate.Session;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    private UsersRepositoryImpl usersRepository;
    @Mock
    private HttpServletRequest mockRequest;
    private UsersService usersService;
    private Session session;
    private Users user;

    @BeforeEach
     void setUp() {
        HibernateUtilTest hibernateUtilTest = HibernateUtilTest.getInstance();
        SessionFactory sessionFactory = hibernateUtilTest.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        usersRepository = new UsersRepositoryImpl();
        usersService = new UsersService();
        user = Users.builder().login("testUser").password("f").build();
        when(mockRequest.getAttribute("hibernateSession")).thenReturn(session);

    }

    @AfterEach
     void tearDown(){
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    public void testCheckUserExists_UserExists_ReturnsTrue() {

        usersRepository.save(user,session);
        boolean result = usersService.checkUserExists(mockRequest, user.getLogin());
        assertTrue(result);
    }


    @Test
    public void testCheckUserExists_UserDoesNotExist_ReturnsFalse() {

        boolean result = usersService.checkUserExists(mockRequest, user.getLogin());
        assertFalse(result);
    }

    @Test
    public void testRegisterNewUser_UserDoesNotExist_SuccessfullyRegisters() throws UserAlreadyExistsException {

        usersService.registerNewUser(mockRequest,user.getLogin(),user.getPassword());
        boolean result = usersService.checkUserExists(mockRequest, user.getLogin());
        assertTrue(result);
    }

    @Test
    public void testRegisterNewUser_UserAlreadyExists_ThrowsException() {
        usersRepository.save(user,session);
        assertThrows(UserAlreadyExistsException.class, () -> {
            usersService.registerNewUser(mockRequest, user.getLogin(),user.getPassword());
        });
    }

    @Test
    public void testGetUser_UserExists_ReturnsUser() {
        usersRepository.save(user,session);
        Users result = usersService.getUser(mockRequest, user.getLogin());
        assertEquals(user, result);
    }

    @Test
    public void testGetUser_UserDoesNotExist_ThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> {
            usersService.getUser(mockRequest,  user.getLogin());
        });
    }
}