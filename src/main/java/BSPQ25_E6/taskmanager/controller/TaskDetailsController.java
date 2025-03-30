package BSPQ25_E6.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskDetailsController {

    @GetMapping("/taskDetail")
    public String taskDetail() {
        return "taskDetail";
    }
}
