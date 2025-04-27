package BSPQ25_E6.taskmanager.repository;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Save a Task and verify it is stored correctly")
    void testSaveTask() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user = userRepository.save(user);

        Category category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category);

        Task task = new Task();
        task.setTitle("Test Task Title");
        task.setDescription("This is a test task description.");
        task.setProgress(0);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setUser(user);
        task.setAssignee(user); 
        task.setCategory(category);
        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId(), "Task ID should not be null after saving");
        assertEquals("Test Task Title", savedTask.getTitle());
        assertEquals("testuser", savedTask.getUser().getUsername());
        assertEquals("Test Category", savedTask.getCategory().getName());

        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertTrue(foundTask.isPresent(), "Task should be found in database");
    }

    @Test
    @DisplayName("Find tasks by User")
    void testFindTasksByUser() {
        User user = new User();
        user.setUsername("finduser");
        user.setEmail("finduser@example.com");
        user.setPassword("password456");
        user = userRepository.save(user);

        Category category = new Category();
        category.setName("Find Category");
        category = categoryRepository.save(category);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setProgress(0);
        task1.setCreationDate(LocalDateTime.now());
        task1.setDueDate(LocalDateTime.now().plusDays(5));
        task1.setUser(user);
        task1.setAssignee(user);
        task1.setCategory(category);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setProgress(0);
        task2.setCreationDate(LocalDateTime.now());
        task2.setDueDate(LocalDateTime.now().plusDays(10));
        task2.setUser(user);
        task2.setAssignee(user);
        task2.setCategory(category);
        taskRepository.save(task2);

        List<Task> userTasks = taskRepository.findByUser(user);

        assertNotNull(userTasks);
        assertEquals(2, userTasks.size(), "User should have exactly 2 tasks assigned.");
        assertEquals("Task 1", userTasks.get(0).getTitle());
        assertEquals("Task 2", userTasks.get(1).getTitle());
    }
}
