package BSPQ25_E6.taskmanager.controller;


import java.util.ArrayList;
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
	        return "redirect:/login"; 
	    }

	    // Obtener las tareas asociadas al usuario
	    List<Task> tasks = taskRepository.findAll();
	    List<Task> userTasks = new ArrayList<Task>();
	    List<Task> doneTasks = new ArrayList<Task>();
	    List<Task> myTasks = new ArrayList<Task>();
	   
	    for (Task task: tasks) {
	    	if (task.getUser().getId()==user.getId()) {
	    		userTasks.add(task);
		    	if (task.getProgress()==100) {
		    		doneTasks.add(task);
		    		userTasks.remove(task);
		    		myTasks.remove(task);
		    	}

	    	}
	    	if (task.getAssignee().getId()==user.getId()) {
	    		System.out.print("Task added"+task.getTitle());
	    		myTasks.add(task);
	    	}
	    	
	    }
	    model.addAttribute("myTasks",myTasks);
	    model.addAttribute("doneTasks",doneTasks);
	    model.addAttribute("userTasks", userTasks);
	    return "dashboard"; 
	}

}
