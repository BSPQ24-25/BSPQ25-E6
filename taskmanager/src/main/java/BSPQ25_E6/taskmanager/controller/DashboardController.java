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
import BSPQ25_E6.taskmanager.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    @Autowired
    private TaskRepository taskRepository;

    @SuppressWarnings("unused")
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TaskService taskService; 
    
    @Autowired  // importante para que Spring lo inyecte
    public DashboardController(TaskRepository taskRepository,TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService=taskService;
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; 
        }

        List<Category> categories = new ArrayList<>();
        List<Task> tasks = taskRepository.findAll();
        List<Task> userTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();
        List<Task> myTasks = new ArrayList<>();
        Map<Category, int[]> categoryProgress = new HashMap<>();

        for (Task task : tasks) {
            int[] progress = {0, 0};

            if (task.getUser().getId() == user.getId()) {
                userTasks.add(task);

                progress = categoryProgress.get(task.getCategory());
                if (progress == null) {
                    progress = new int[]{0, 0};
                }
                progress[0]++;

                if (task.getProgress() == 100) {
                    progress[1]++;
                    doneTasks.add(task);
                    userTasks.remove(task);
                    myTasks.remove(task);
                }
                categoryProgress.put(task.getCategory(), progress);
            }

            if (task.getAssignee().getId() == user.getId()) {
                System.out.print("Task added " + task.getTitle());
                myTasks.add(task);

                progress = categoryProgress.get(task.getCategory());
                if (progress == null) {
                    progress = new int[]{0, 0};
                    categoryProgress.put(task.getCategory(), progress);
                }
                progress[0]++;

                for (Category category : categories) {
                    categoryProgress.put(category, new int[]{0, 0});
                }

                if (task.getProgress() == 100) {
                    progress[1]++;
                    doneTasks.add(task);
                    userTasks.remove(task);
                    myTasks.remove(task);
                }
                categoryProgress.put(task.getCategory(), progress);
            }
        }

        model.addAttribute("categoryProgress", categoryProgress);
        model.addAttribute("myTasks", myTasks);
        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("userTasks", userTasks);
        model.addAttribute("tasks", tasks);

        return "dashboard"; 
    }
    @GetMapping("/dashboard/{projectId}")
    public String getTasksByProject(@PathVariable Long projectId, Model model) {
        List<Task> tasks = taskService.findTasksByProjectId(projectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        return "dashboard";  // nombre del template Thymeleaf (dashboard.html)
    }







}