package BSPQ25_E6.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class InboxController {
    @GetMapping("/inbox")
    public String inbox() {
        return "inbox";
    }
}
