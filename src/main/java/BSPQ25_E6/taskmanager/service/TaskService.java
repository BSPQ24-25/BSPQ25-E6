package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Task;

import BSPQ25_E6.taskmanager.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;




    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));











    }
}