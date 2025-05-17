package BSPQ25_E6.taskmanager.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;


class UserServiceTest 
{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() 
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() 
    {
        User user = new User("Diego", "diego@email.com", "password123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Diego", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByEmail() 
    {
        User user = new User("Diego", "diego@email.com", "password123");
        when(userRepository.findByEmail("diego@email.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserByEmail("diego@email.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Diego", foundUser.get().getUsername());
    }
    @Test
    void testGetAllUsers() 
    {
        User u1 = new User("Ana", "ana@email.com", "1234");
        User u2 = new User("Juan", "juan@email.com", "abcd");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }
}
