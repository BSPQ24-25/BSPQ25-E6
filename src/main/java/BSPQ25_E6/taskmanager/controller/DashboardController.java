package BSPQ25_E6.taskmanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private TaskRepository taskRepository;

    @SuppressWarnings("unused")
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; 
        }

        List<Task> allTasks = taskRepository.findAll();
        List<Task> createdTasks = new ArrayList<>();
        List<Task> assignedTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();
        Map<Category, int[]> categoryProgress = new HashMap<>();

        for (Task task : allTasks) {
            if (task.getUser().getId() == user.getId()) {
                createdTasks.add(task);
                updateCategoryProgress(categoryProgress, task);
            }

            if (task.getAssignee() != null && task.getAssignee().getId() == user.getId()) {
                assignedTasks.add(task);
                updateCategoryProgress(categoryProgress, task);
            }

            if (task.getProgress() == 100) {
                doneTasks.add(task);
            }
        }

        model.addAttribute("createdTasks", createdTasks);
        model.addAttribute("assignedTasks", assignedTasks);
        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("categoryProgress", categoryProgress);

        return "dashboard"; 
    }

    private void updateCategoryProgress(Map<Category, int[]> progressMap, Task task) {
        Category category = task.getCategory();
        int[] progress = progressMap.getOrDefault(category, new int[]{0, 0});
        progress[0]++; // Increment total tasks in category
        if (task.getProgress() == 100) {
            progress[1]++; // Increment completed tasks
        }
        progressMap.put(category, progress);
    }
}

