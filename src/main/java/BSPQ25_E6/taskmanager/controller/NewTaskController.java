package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class NewTaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/new")
    public String newTaskForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "newTask";
    }

    // Process task creation
    @PostMapping("/create")
    public String createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String dueDate,
            @RequestParam("assigneeID") Long assigneeID,
            @RequestParam String categoryId,
            @RequestParam(required = false) String newCategory,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (isInvalidInput(title, description)) {
            model.addAttribute("error", "Title (3-100 chars) and description (10-255 chars) required.");
            return reloadForm(model);
        }

        Optional<User> assignee = userRepository.findById(assigneeID);
        if (assignee.isEmpty()) {
            model.addAttribute("error", "Invalid assignee selected.");
            return reloadForm(model);
        }

        Category category;
        try {
            category = resolveCategory(categoryId, newCategory, model);
        } catch (Exception e) {
            return reloadForm(model);
        }

        Task task = buildTask(title, description, dueDate, assignee.get(), user, category);
        taskRepository.save(task);

        return "redirect:/dashboard";
    }

    private boolean isInvalidInput(String title, String description) {
        return title == null || title.length() < 3 || title.length() > 100 ||
               description == null || description.length() < 10 || description.length() > 255;
    }

    private Category resolveCategory(String categoryId, String newCategory, Model model) 
            throws IllegalArgumentException {
        if (categoryId.equals("new") && newCategory != null && !newCategory.isEmpty()) {
            Category category = new Category();
            category.setName(newCategory);
            return categoryRepository.save(category);
        } else {
            return categoryRepository.findById(Long.parseLong(categoryId))
                .orElseThrow(() -> {
                    model.addAttribute("error", "Invalid category selection.");
                    throw new IllegalArgumentException();
                });
        }
    }

    private Task buildTask(String title, String description, String dueDate, 
                         User assignee, User creator, Category category) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        task.setProgress(0);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.parse(dueDate + "T00:00:00"));
        task.setAssignee(assignee);
        task.setUser(creator);
        task.setCategory(category);
        return task;
    }

    private String reloadForm(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "newTask";
    }
}