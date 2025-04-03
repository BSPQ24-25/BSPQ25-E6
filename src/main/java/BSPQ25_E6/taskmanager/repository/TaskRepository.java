package BSPQ25_E6.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BSPQ25_E6.taskmanager.model.Task;
import BSPQ25_E6.taskmanager.model.User;

@Repository
 public interface TaskRepository extends JpaRepository<Task, Long> {
     Optional<Task> findByUser(User user);
 }