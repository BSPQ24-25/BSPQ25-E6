package BSPQ25_E6.taskmanager.controller;

import java.time.LocalDateTime;
import java.util.List;

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
    public String newTaskForm(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    List<User> users = userRepository.findAll();
	    model.addAttribute("users",users);
    	if (user == null) {
	        return "redirect:/login"; // Redirigir si no hay usuario logeado
	    }
        return "newTask";
    }
    @PostMapping("/newTask")
    public String registerTask(@RequestParam String title, @RequestParam String description,@RequestParam String dueDate, @RequestParam("assigneeID") Long assigneeID, HttpSession session) {
    	Task task = new Task();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }// Obtener el usuario de la sesión
        System.out.println("Assignee ID: " + assigneeID);  // Imprimir el valor recibido
        LocalDateTime taskDate = LocalDateTime.parse(dueDate + "T00:00:00");
    	task.setTitle(title);
    	task.setDescription(description);
    	task.setCompleted(false);
    	task.setProgress(0);
    	task.setCreationDate(LocalDateTime.now());
    	task.setAssignee(userRepository.findById(assigneeID).orElse(null));
    	task.setDueDate(taskDate);

    	task.setUser(user);
    	taskRepository.save(task);
    	
    	
    	return "redirect:dashboard";
    }
    @GetMapping("/my-tasks")
    public String getMyTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Task> tasks = taskRepository.findByUser(user);
        model.addAttribute("tasks", tasks);
        return "my-tasks";
    }
}
