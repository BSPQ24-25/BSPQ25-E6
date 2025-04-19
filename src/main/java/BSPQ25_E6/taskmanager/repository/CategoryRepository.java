package BSPQ25_E6.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import BSPQ25_E6.taskmanager.model.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	
	
}
