package BSPQ25_E6.taskmanager.unit.controller;

import BSPQ25_E6.taskmanager.model.User;
import java.util.Optional;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import BSPQ25_E6.taskmanager.controller.AuthController;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest 
{

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    private User sampleUser;

    @BeforeEach
    void setup() 
    {
        sampleUser = new User("diego", "diego@example.com", "password123");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    }

    @Test
    void testRegisterUserApi_Success() 
    {
        when(userRepository.findByEmail("diego@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        ResponseEntity<User> response = authController.registerUserApi(sampleUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleUser.getEmail(), response.getBody().getEmail());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testRegisterUserApi_EmailExists() 
    {
        when(userRepository.findByEmail("diego@example.com")).thenReturn(Optional.of(sampleUser));
        ResponseEntity<User> response = authController.registerUserApi(sampleUser);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userRepository, never()).save(any());
    }
}
