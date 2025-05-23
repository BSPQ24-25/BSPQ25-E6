package BSPQ25_E6.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;
import BSPQ25_E6.taskmanager.model.Category;

@Repository
 public interface TaskRepository extends JpaRepository<Task, Long> {
     List<Task> findByUser(User user);
     List<Task> findByAssignee(User assignee);
     List<Task> findByCategory(Category category);

 }