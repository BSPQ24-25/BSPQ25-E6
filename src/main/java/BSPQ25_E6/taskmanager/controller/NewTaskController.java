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
public class NewTaskController 
{

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    //show the form
    @GetMapping("/new")
    public String newTaskForm(HttpSession session, Model model) 
    {
        User user = (User) session.getAttribute("user");
        if (user == null) 
        {
            return "redirect:/login";
        }

        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "newTask";
    }
    @PostMapping("/create")
    public String createTask(@RequestParam String title,
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

        //here we validate the form manually 
        if (title == null || title.length() < 3 || title.length() > 100 ||
            description == null || description.length() < 10 || description.length() > 255) 
        {
            model.addAttribute("error", "Please fill the form correctly.");
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            return "newTask";
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        task.setProgress(0);
        task.setCreationDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.parse(dueDate + "T00:00:00"));
        Optional<User> assignee = userRepository.findById(assigneeID);
        assignee.ifPresent(task::setAssignee);

        if (categoryId.equals("new") && newCategory != null && !newCategory.isEmpty()) {
            Category category = new Category();
            category.setName(newCategory);
            categoryRepository.save(category);
            task.setCategory(category);
        } else {
            try {
                Long catId = Long.parseLong(categoryId);
                Optional<Category> category = categoryRepository.findById(catId);
                category.ifPresent(task::setCategory);
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid category selection.");
                model.addAttribute("users", userRepository.findAll());
                model.addAttribute("categories", categoryRepository.findAll());
                return "newTask";
            }
        }
        task.setUser(user);

        taskRepository.save(task);

        return "redirect:/dashboard";
    }
}
