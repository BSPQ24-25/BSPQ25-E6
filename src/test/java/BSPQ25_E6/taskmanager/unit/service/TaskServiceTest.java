package BSPQ25_E6.taskmanager.unit.service;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest 
{

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private AutoCloseable closeable;

    private User user;

    @BeforeEach
    void setUp() 
    {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() 
    {
        Task task = new Task();
        task.setTitle("New Task");
        task.setUser(user);

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should return tasks by user")
    void testGetTasksByUser() 
    {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setUser(user);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setUser(user);

        when(taskRepository.findByUser(user)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getTasksByUser(user);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("Should return task by ID if found")
    void testGetTaskByIdFound() 
    {
        Task task = new Task();
        task.setId(10L);
        task.setTitle("Task by ID");

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(10L);

        assertTrue(result.isPresent());
        assertEquals("Task by ID", result.get().getTitle());
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask() 
    {
        Long taskId = 5L;

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }
}
