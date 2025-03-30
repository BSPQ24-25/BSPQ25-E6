package BSPQ25_E6.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController 
{

    //Login page 
    @GetMapping("/login")
    public String showLoginForm(Model model) 
    {
        return "login";
    }
/* 
    //login form controller
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model) 
                            {
        //Spring 2 
        if (email.equals("test@test.com") && password.equals("1234")) 
        {
            return "redirect:/dashboard";
        } else 
        {
            model.addAttribute("error", "Password invalid");
            return "login";
        }
    }
*/
    //Signup page
    @GetMapping("/signup")
    public String showSignupForm(Model model) 
    {
        return "signup";
    }
/* 
    //signup form controller
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) 
                               {
        //spring 2 
        model.addAttribute("success", "User register");
        return "login";
    }
*/
}
