package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public Task assignTask(Long taskId, Long assigneeId) {
        Task task = findTaskById(taskId);
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }
}