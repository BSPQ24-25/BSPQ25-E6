package BSPQ25_E6.taskmanager.controller;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import java.util.Optional;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController 
{
	
	@Autowired
	private UserRepository userRepository;
	
    //Login page 
    @GetMapping("/login")
    public String showLoginForm(Model model) 
    {
        return "login";
    }

    //login form controller
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model
                           ) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password.equals(user.getPassword())) {
            	
                
                return "redirect:/dashboard";
            }
        }
        model.addAttribute("error", "Credenciales inválidas");
        return "login";
    }
    //Signup page
    @GetMapping("/signup")
    public String showSignupForm(Model model) 
    {
        return "signup";
    }
    //signup form controller
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) 
                               {
    	if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "The email is already registered.");
            return "signup";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);

        model.addAttribute("success", "User registered succesfully, please log in.");
        return "login";
    
    }
    @GetMapping("/logout")
    public String logout( ) {
        userSession.setLoggedUser(null); 
        return "redirect:/login"; 
    }


}
