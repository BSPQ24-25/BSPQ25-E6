package BSPQ25_E6.taskmanager.unit.controller;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import BSPQ25_E6.taskmanager.controller.ProjectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest 
{
	
	@Mock
	private org.springframework.ui.Model model;

	
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectController projectController;

    private User sampleUser;
    private Project sampleProject;
    
    @BeforeEach
    void setup() 
    {
        sampleUser = new User("TestDiego", "diego@example.com", "password123");
        sampleUser.setId(1L);
        sampleUser.setProjects(new HashSet<>());

        sampleProject = new Project("Test Project", "Description");
        sampleProject.setUsers(new HashSet<>());
    }
    
    @Test
    void testCreateProject_Success() {
        when(projectRepository.save(sampleProject)).thenReturn(sampleProject);

        String result = projectController.createProjectWeb(sampleProject, model);

        assertEquals("redirect:/projects", result); 
        verify(projectRepository, times(1)).save(sampleProject);
        verify(model).addAttribute("success", "Project created successfully!");
    }



    @Test
    void testAddUserToProject_Success() 
    {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(sampleProject));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(projectRepository.save(any())).thenReturn(sampleProject);
        when(userRepository.save(any())).thenReturn(sampleUser);

        ResponseEntity<String> response = projectController.addUserToProject(100L, 1L);
        assertEquals("User added to project successfully", response.getBody());

        assertTrue(sampleProject.getUsers().contains(sampleUser));
        assertTrue(sampleUser.getProjects().contains(sampleProject));
    }

    @Test
    void testAddUserToProject_NotFound() 
    {
        when(projectRepository.findById(100L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = projectController.addUserToProject(100L, 1L);
        assertEquals("User or project not found", response.getBody());

        verify(projectRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }
}
