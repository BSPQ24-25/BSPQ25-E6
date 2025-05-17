package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest 
{

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() 
    {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a new project")
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
    @DisplayName("Should add user to project")
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

    @Test
    @DisplayName("Should return projects by owner")
    void testGetProjectsByOwner() 
    {
        User owner = new User();
        owner.setId(2L);
        owner.setUsername("ana");

        Project p1 = new Project("Project 1", "Desc 1"); p1.setOwner(owner);
        Project p2 = new Project("Project 2", "Desc 2"); p2.setOwner(owner);

        when(projectRepository.findByOwner(owner)).thenReturn(List.of(p1, p2));

        List<Project> result = projectService.getProjectsByOwner(owner);

        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Project 2", result.get(1).getName());
        verify(projectRepository, times(1)).findByOwner(owner);
    }
}
