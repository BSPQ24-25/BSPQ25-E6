package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import BSPQ25_E6.taskmanager.service.TaskService;
import BSPQ25_E6.taskmanager.service.UserService;
import BSPQ25_E6.taskmanager.service.CategoryService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskDetailsController {

    private final TaskService taskService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public TaskDetailsController(TaskService taskService, 
                               UserService userService,
                               CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/taskDetail/{id}")
    public String taskDetail(@PathVariable("id") Long taskId, Model model) {
        Task task = taskService.findTaskById(taskId);
        model.addAttribute("task", task);
        return "taskDetail";
    }

    @GetMapping("/taskDetail/edit/{id}")
    public String editTaskForm(@PathVariable("id") Long taskId, Model model) {
        Task task = taskService.findTaskById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editTask";
    }

    @PostMapping("/taskDetail/update/{id}")
    public String updateTask(@PathVariable("id") Long taskId,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam int progress,
                           @RequestParam(required = false) boolean completed,
                           @RequestParam String dueDate,
                           @RequestParam Long assignee,
                           @RequestParam Long category) {
        
        Task existingTask = taskService.findTaskById(taskId);
        
        // Actualizar campos básicos
        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setProgress(progress);
        existingTask.setCompleted(completed);
        
        // Convertir y actualizar fecha
        LocalDateTime dueDateTime = LocalDateTime.parse(dueDate + "T00:00:00");
        existingTask.setDueDate(dueDateTime);
        
        // Actualizar relaciones
        User assigneeUser = userService.getUserById(assignee);
        existingTask.setAssignee(assigneeUser);
        
        Category taskCategory = categoryService.getCategoryById(category);
        existingTask.setCategory(taskCategory);
        
        taskService.createTask(existingTask);
        return "redirect:/taskDetail/" + taskId;
    }

    @PostMapping("/taskDetail/delete/{id}")
    public String deleteTask(@PathVariable("id") Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/";
    }
}