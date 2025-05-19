package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Project;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.ProjectRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService 
{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired  // importante para que Spring lo inyecte
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository=projectRepository;
    }
    
    
    public Task findTaskById(Long taskId) 
    {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getAllTasks() 
    {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) 
    {
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) 
    {
        return taskRepository.findByUser(user);
    }

    public void deleteTask(Long taskId) 
    {
        taskRepository.deleteById(taskId);
    }

    public Optional<Task> getTaskById(Long taskId) 
    {
        return taskRepository.findById(taskId);
    }

	public List<Task> findTasksByProjectId(Long projectId) {
		// TODO Auto-generated method stub
		
		List<Task> tasks = getAllTasks();
		List<Task> finalTasks = new ArrayList<>();
		for (Task task: tasks) {
			Project project = projectRepository.findById(projectId).orElse(null);
			if (task.getProject()==project) {
				finalTasks.add(task);
			}
		}
		return finalTasks;
	}
}
