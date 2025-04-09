package BSPQ25_E6.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.model.Task;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	@Override
	default Optional<Category> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	default Category getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
