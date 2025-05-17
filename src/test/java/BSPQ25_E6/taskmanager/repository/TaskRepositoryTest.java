package BSPQ25_E6.taskmanager.repository;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.model.Category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void cleanUp() 
    {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    @DisplayName("Save a Task and verify it is stored correctly")
    void testSaveTask() {
        User user = new User("testuser", "testuser@example.com", "password123");
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

        assertNotNull(savedTask.getId());
        assertEquals("Test Task Title", savedTask.getTitle());
        assertEquals("testuser", savedTask.getUser().getUsername());
        assertEquals("Test Category", savedTask.getCategory().getName());

        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertTrue(foundTask.isPresent());
    }

    @Test
    @DisplayName("Find tasks by User")
    void testFindTasksByUser() {
        User user = new User("finduser", "finduser@example.com", "password456");
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
        assertEquals(2, userTasks.size());
        assertEquals("Task 1", userTasks.get(0).getTitle());
        assertEquals("Task 2", userTasks.get(1).getTitle());
    }

    @Test
    @DisplayName("Find tasks by Assignee")
    void testFindTasksByAssignee() {
        User creator = new User("creator", "creator@example.com", "pass123");
        creator = userRepository.save(creator);

        User assignee = new User("assignee", "assignee@example.com", "pass456");
        assignee = userRepository.save(assignee);

        Category category = new Category();
        category.setName("Assigned Work");
        category = categoryRepository.save(category);

        Task task = new Task();
        task.setTitle("Assigned Task");
        task.setDescription("Assigned to another user");
        task.setProgress(50);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(3));
        task.setUser(creator);
        task.setAssignee(assignee);
        task.setCategory(category);
        taskRepository.save(task);

        List<Task> tasksAssigned = taskRepository.findByAssignee(assignee);

        assertNotNull(tasksAssigned);
        assertEquals(1, tasksAssigned.size());
        assertEquals("Assigned Task", tasksAssigned.get(0).getTitle());
    }

    @Test
    @DisplayName("Find tasks by Category")
    void testFindTasksByCategory() {
        User user = new User("catuser", "catuser@example.com", "catpass");
        user = userRepository.save(user);

        Category category = new Category();
        category.setName("Urgent");
        category = categoryRepository.save(category);

        Task task = new Task();
        task.setTitle("Urgent Task");
        task.setDescription("This task is urgent");
        task.setProgress(20);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setUser(user);
        task.setAssignee(user);
        task.setCategory(category);
        taskRepository.save(task);

        List<Task> tasksByCategory = taskRepository.findByCategory(category);

        assertNotNull(tasksByCategory);
        assertEquals(1, tasksByCategory.size());
        assertEquals("Urgent Task", tasksByCategory.get(0).getTitle());
    }
}
