package BSPQ25_E6.taskmanager.config;


import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import BSPQ25_E6.taskmanager.repository.TaskRepository;
import BSPQ25_E6.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
@Transactional
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, TaskRepository taskRepository, CategoryRepository categoryRepository) {
        return args -> {
        	if ((userRepository.count()==0)&&(taskRepository.count()==0)&&(categoryRepository.count()==0)) {
                for (int i = 1; i <= 10; i++) {
                    User user = new User("user" + i, "user" + i + "@mail.com", "password" + i);
                    userRepository.save(user);
                }

                var users = userRepository.findAll();
                Random random = new Random();
               
                
                for (int i = 1; i <= 10; i++) {
                	Category category = new Category();
                	category.setName("Category"+i);
                	categoryRepository.save(category);
                    Task task = new Task();
                    task.setTitle("Tarea " + i);
                    task.setDescription("Descripción de la tarea " + i);
                    task.setProgress(random.nextInt(101)); // 0-100
                    task.setCompleted(random.nextBoolean());
                    task.setCreationDate(LocalDateTime.now());
                    task.setDueDate(LocalDateTime.now().plusDays(random.nextInt(10) + 1));
                    task.setCategory(category);
                    User creator = users.get(random.nextInt(users.size()));
                    User assignee = users.get(random.nextInt(users.size()));
                    task.setUser(creator);
                    task.setAssignee(assignee);

                    taskRepository.save(task);
                }
                
                

                System.out.println("Datos de prueba insertados correctamente.");
            }
        	
       };
    
    }
}
