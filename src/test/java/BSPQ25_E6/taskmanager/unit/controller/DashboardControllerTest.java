package BSPQ25_E6.taskmanager.unit.controller;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;
import org.mockito.junit.jupiter.MockitoExtension;
import BSPQ25_E6.taskmanager.controller.DashboardController;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DashboardControllerTest
{

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    private MockHttpSession session;
    private User sampleUser;
    private Category sampleCategory;

    @BeforeEach
    void setup() 
    {
        session = new MockHttpSession();
        sampleUser = new User("diego", "diego@example.com", "password123");
        sampleUser.setId(1L);

        sampleCategory = new Category();
        sampleCategory.setName("General");
    }

    @Test
    void testShowDashboardWithValidUser() 
    {
        session.setAttribute("user", sampleUser);

        Task task = new Task();
        task.setTitle("Tarea");
        task.setDescription("Descripción");
        task.setUser(sampleUser);
        task.setAssignee(sampleUser);
        task.setCategory(sampleCategory);
        task.setProgress(50);
        task.setCompleted(false);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(5));

        List<Task> allTasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(allTasks);

        String viewName = dashboardController.showDashboard(session, model);

        assertEquals("dashboard", viewName);
        verify(model).addAttribute(eq("categoryProgress"), any());
        verify(model).addAttribute(eq("myTasks"), any());
        verify(model).addAttribute(eq("doneTasks"), any());
        verify(model).addAttribute(eq("userTasks"), any());
        verify(model).addAttribute(eq("tasks"), eq(allTasks));
    }

    @Test
    void testShowDashboardWithoutUserInSession() 
    {
        String viewName = dashboardController.showDashboard(session, model);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testShowDashboardWithNoTasks() 
    {
        session.setAttribute("user", sampleUser);
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        String viewName = dashboardController.showDashboard(session, model);

        assertEquals("dashboard", viewName);
        verify(model).addAttribute(eq("categoryProgress"), any());
        verify(model).addAttribute(eq("myTasks"), any());
        verify(model).addAttribute(eq("doneTasks"), any());
        verify(model).addAttribute(eq("userTasks"), any());
        verify(model).addAttribute(eq("tasks"), eq(Collections.emptyList()));
    }
}
