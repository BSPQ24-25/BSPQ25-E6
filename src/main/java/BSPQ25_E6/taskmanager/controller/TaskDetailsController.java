package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.service.TaskService;
import BSPQ25_E6.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskDetailsController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String taskDetail(@PathVariable("id") Long taskId, Model model) {
        Task task = taskService.findTaskById(taskId);
        List<User> availableUsers = userService.getAllUsers(); // Now this will work
        
        model.addAttribute("task", task);
        model.addAttribute("availableUsers", availableUsers);
        return "taskDetail";
    }

    @PostMapping("/{id}/assign")
    public String assignTask(
            @PathVariable("id") Long taskId,
            @RequestParam("assigneeId") Long assigneeId,
            Model model) {
        
        try {
            taskService.assignTask(taskId, assigneeId);
            model.addAttribute("success", "Task assigned successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Assignment failed: " + e.getMessage());
        }
        return "redirect:/tasks/" + taskId;
    }
}