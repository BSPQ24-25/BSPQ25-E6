package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import BSPQ25_E6.taskmanager.service.ProjectService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/projects")
public class ProjectController 
{
	private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectRepository projectRepository,UserRepository userRepository) {
        this.projectService = projectService;
        this.projectRepository=projectRepository;
        this.userRepository=userRepository;
    }
    
    @GetMapping("")
    public String projects(HttpSession session, Model model) 
    {
    	
        User user = (User) session.getAttribute("user");
        System.out.print(user.getId());
        if (user == null) 
        {
            return "redirect:/login";
        }else {
        	List<Project> projects = projectService.getUsersProjects(user.getId()).orElse(null);
            model.addAttribute("projects", projects);
        }
        
        return "projects";
    }
 
    @GetMapping("/new")
    public String newProjectForm(Model model) {
        
        model.addAttribute("project", new Project());
        model.addAttribute("users", userRepository.findAll());
        return "newProject"; 
    }
    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, Model model) {
        // guardar el proyecto
        projectRepository.save(project);
        model.addAttribute("success", "Project created successfully!");
        return "redirect:/projects"; // o donde quieras
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