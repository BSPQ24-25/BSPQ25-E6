package BSPQ25_E6.taskmanager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class ProjectServiceTest 
{

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() 
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() 
    {
        Project project = new Project("Proyect A", "A");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertNotNull(createdProject);
        assertEquals("Proyect A", createdProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testAddUserToProject() 
    {
        User user = new User("Diego", "diego@email.com", "password123");
        Project project = new Project("Proyect B", "B");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean success = projectService.addUserToProject(1L, 1L);

        assertTrue(success);
        assertTrue(project.getUsers().contains(user));
        assertTrue(user.getProjects().contains(project));
        verify(projectRepository, times(1)).save(project);
        verify(userRepository, times(1)).save(user);
    }
}
