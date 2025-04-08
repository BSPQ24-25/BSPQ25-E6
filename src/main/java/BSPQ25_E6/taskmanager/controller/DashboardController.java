package BSPQ25_E6.taskmanager.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController 
{
	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
	    // Obtener el usuario de la sesión
	    User user = (User) session.getAttribute("user");
	    
	    if (user == null) {
	        return "redirect:/login"; // Redirigir si no hay usuario logeado
	    }

	    // Obtener las tareas asociadas al usuario
	    List<Task> tasks = taskRepository.findByUser(user); // Asumiendo que tienes un método en el repositorio para esto
	    // Pasar las tareas a la vista
	    model.addAttribute("tasks", tasks);
	    return "dashboard"; // Nombre de la plantilla Thymeleaf (puede ser dashboard.html)
	}

}
