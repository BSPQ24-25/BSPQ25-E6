package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/projects")
public class ProjectController 
{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    //new project
    @PostMapping("/create")
    public Project createProject(@RequestBody Project project) 
    {
        return projectRepository.save(project);




    }

    //get prouject by id
    @GetMapping("/all")
    public List<Project> getAllProjects() 
    {
        return projectRepository.findAll();
    }

    //and assign user to project
    @PostMapping("/{projectId}/addUser/{userId}")
    public ResponseEntity<String> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalProject.isPresent() && optionalUser.isPresent()) {
            Project project = optionalProject.get();
            User user = optionalUser.get();

            project.getUsers().add(user);
            user.getProjects().add(project);

            projectRepository.save(project);
            userRepository.save(user);

            return ResponseEntity.ok("User added to project successfully");
        }

        return ResponseEntity.status(404).body("User or project not found");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

}