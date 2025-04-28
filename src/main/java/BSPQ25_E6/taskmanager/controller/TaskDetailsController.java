package BSPQ25_E6.taskmanager.controller;

import BSPQ25_E6.taskmanager.model.Task;

import BSPQ25_E6.taskmanager.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller

public class TaskDetailsController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/taskDetail/{id}")



    public String taskDetail(@PathVariable("id") Long taskId, Model model) {
        Task task = taskService.findTaskById(taskId);


        model.addAttribute("task", task);

        return "taskDetail";
    }
}