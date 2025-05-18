package BSPQ25_E6.taskmanager.unit.controller;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import BSPQ25_E6.taskmanager.controller.NewTaskController;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
@MockitoSettings(strictness = Strictness.LENIENT)
class NewTaskControllerTest 
{
    @InjectMocks
    private NewTaskController newTaskController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User("Diego", "diego@example.com", "pasword123");
        testUser.setId(1L);
    }

    @Test
    void testNewTaskFormReturnsFormView() 
    {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(categoryRepository.findAll()).thenReturn(List.of(new Category("Work")));

        String view = newTaskController.newTaskForm(session, model);

        assertEquals("newTask", view);
        verify(model).addAttribute(eq("task"), any());
        verify(model).addAttribute(eq("users"), anyList());
        verify(model).addAttribute(eq("categories"), anyList());
    }

    @Test
    void testCreateTaskWithValidDataReturnsRedirect() 
    {
        User assignee = new User("Assignee", "assignee@mail.com", "pass");
        assignee.setId(2L);

        Category category = new Category("Work");
        category.setId(1L);

        String dueDate = LocalDateTime.now().plusDays(2).toLocalDate().toString();

        when(session.getAttribute("user")).thenReturn(testUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        String result = newTaskController.createTask(
                "Test Title",
                "Test Description",
                dueDate,
                2L,
                "1",
                "",
                session,
                model
        );

        assertEquals("redirect:/dashboard", result);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testCreateTaskWithInvalidTitleReturnsFormWithError() 
    {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        String result = newTaskController.createTask(
                "", "Desc",
                LocalDateTime.now().toLocalDate().toString(),
                2L, "1", "", session, model
        );

        assertEquals("newTask", result);
        verify(model).addAttribute(eq("error"), contains("Please fill the form correctly."));
    }
}
