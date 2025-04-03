package BSPQ25_E6.taskmanager.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class NewTaskController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    @GetMapping("/newTask")
    public String newTaskForm() {
        return "newTask";
    }
    @PostMapping("/newTask")
    public String registerTask(@RequestParam String title, @RequestParam String description, HttpSession session) {
    	Task task = new Task();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }// Obtener el usuario de la sesión
    	task.setTitle(title);
    	task.setDescription(description);
    	task.setCompleted(false);
    	task.setUser(user);
    	taskRepository.save(task);
    	
    	
    	return "dashboard";
    }
    @GetMapping("/my-tasks")
    public String getMyTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Optional<Task> tasks = taskRepository.findByUser(user);
        model.addAttribute("tasks", tasks);
        return "my-tasks";
    }
}
