package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService 
{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    //creat a project
    public Project createProject(Project project) 
    {
        return projectRepository.save(project);
    }

    //id search
    public Optional<Project> getProjectById(Long projectId) 
    {
        return projectRepository.findById(projectId);
    }
    public Optional<List<Project>> getUsersProjects(Long userId){
    	
    	List<Project> userProjects = new ArrayList<>();

    	for (Project project: projectRepository.findAll()) {
    		Set<User> users = project.getUsers();
    		User user =  userRepository.findById(userId).orElse(null);
    		if (project.getOwner().getId()==userId) {
    			userProjects.add(project);
    		}else if(users.contains(user)) {
    			userProjects.add(project);
    		}
    	}
    	return Optional.ofNullable(userProjects);
    }

    // assgin a user to a project
    public boolean addUserToProject(Long projectId, Long userId) 
    {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalProject.isPresent() && optionalUser.isPresent()) 
        {
            Project project = optionalProject.get();
            User user = optionalUser.get();

            project.getUsers().add(user);
            user.getProjects().add(project);

            projectRepository.save(project);
            userRepository.save(user);

            return true;
        }
        return false;
    }
    public List<Project> getAllProjects() 
    {
        return projectRepository.findAll();
    }
    
    public List<Project> getProjectsByOwner(User owner) 
    {
    return projectRepository.findByOwner(owner);
    }
    public void deleteProject(Long id) 
    {
        projectRepository.deleteById(id);
    }

}
