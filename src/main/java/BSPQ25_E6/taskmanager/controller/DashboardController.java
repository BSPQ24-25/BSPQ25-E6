package BSPQ25_E6.taskmanager.controller;


import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController 
{
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    
	    if (user == null) {
	        return "redirect:/login"; 
	    }

	    List<Task> tasks = taskRepository.findAll();
	    List<Task> userTasks = new ArrayList<Task>();
	    List<Task> doneTasks = new ArrayList<Task>();
	    List<Task> myTasks = new ArrayList<Task>();
	    List<Category> categories = categoryRepository.findAll();
	    Map<Category,int[]> categoryProgress = new HashMap<>();
	    for (Category category: categories) {
	    	categoryProgress.put(category,new int[] {0,0});
	    }
	    for (Task task: tasks) {
	    	int[] progress = {0,0};
	    	if (task.getUser().getId()==user.getId()) {
	    		userTasks.add(task);
	    		progress=categoryProgress.get(task.getCategory());
	    		if (progress==null) {
	    			progress = new int[]{0, 0}; 
	                categoryProgress.put(task.getCategory(), progress);
	    		}
	    		progress[0]++;
	    		
		    	if (task.getProgress()==100) {
		    		progress[1]++;
		    		doneTasks.add(task);
		    		userTasks.remove(task);
		    		myTasks.remove(task);
		    	}
		    	System.out.print("AAAA: "+progress);
	    	}
	    	if (task.getAssignee().getId()==user.getId()) {
	    		System.out.print("Task added"+task.getTitle());
	    		myTasks.add(task);
	    		progress=categoryProgress.get(task.getCategory());
	    		if (progress==null) {
	    			progress = new int[]{0, 0}; 
	                categoryProgress.put(task.getCategory(), progress);
	    		}
	    		progress[0]++;
	    		
		    	if (task.getProgress()==100) {
		    		progress[1]++;
		    		doneTasks.add(task);
		    		userTasks.remove(task);
		    		myTasks.remove(task);
		    	}
	    	}
	    
	    	categoryProgress.put(task.getCategory(), progress);
	    }
	    model.addAttribute("categoryProgress",categoryProgress);
	    model.addAttribute("myTasks",myTasks);
	    model.addAttribute("doneTasks",doneTasks);
	    model.addAttribute("userTasks", userTasks);
	    return "dashboard"; 
	}

}
