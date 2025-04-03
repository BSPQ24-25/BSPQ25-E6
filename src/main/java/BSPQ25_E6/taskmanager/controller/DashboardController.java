package BSPQ25_E6.taskmanager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import BSPQ25_E6.taskmanager.model.User;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController 
{

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) 
    {
    	 User user = (User) session.getAttribute("user");
         if (user == null) {
             return "redirect:/login";
         }// Obtener el usuario de la sesión
        return "dashboard";
    }
}
