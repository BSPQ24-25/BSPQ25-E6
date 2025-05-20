package BSPQ25_E6.taskmanager.repository;

import BSPQ25_E6.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BSPQ25_E6.taskmanager.model.User;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> 
{
    //finde by name the project
    Project findByName(String name);
    List<Project> findByOwner(User owner);

}
