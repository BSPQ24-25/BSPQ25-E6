package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Añade este nuevo método
    public User getUserById(Long id) {
        return userRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}