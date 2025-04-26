package BSPQ25_E6.taskmanager.service;

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
}
