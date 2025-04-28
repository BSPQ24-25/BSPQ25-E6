package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/projects")  // Added "/api" for clarity
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Assign user to project (improved)
    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<String> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalProject.isEmpty() || optionalUser.isEmpty()) {
            return new ResponseEntity<>("Project or user not found", HttpStatus.NOT_FOUND);
        }

        Project project = optionalProject.get();
        User user = optionalUser.get();

        // Check if user is already assigned
        if (project.getUsers().contains(user)) {
            return new ResponseEntity<>("User already assigned to project", HttpStatus.CONFLICT);
        }

        project.getUsers().add(user);
        user.getProjects().add(project);

        projectRepository.save(project);
        userRepository.save(user);

        return new ResponseEntity<>("User added to project successfully", HttpStatus.OK);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<Set<User>> getProjectUsers(@PathVariable Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.map(value -> new ResponseEntity<>(value.getUsers(), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}