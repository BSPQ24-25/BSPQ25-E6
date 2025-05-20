package BSPQ25_E6.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;

import BSPQ25_E6.taskmanager.model.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

@Controller
public class InboxController {
    @GetMapping("/inbox")
    public String inbox(HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    if (user==null) {
	    	return "redirect:login";
	    }
        return "inbox";
    }
}
