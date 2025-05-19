package BSPQ25_E6.taskmanager.service;

import BSPQ25_E6.taskmanager.model.Category;
import BSPQ25_E6.taskmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category findOrCreateCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            category = new Category(categoryName);
            category = categoryRepository.save(category);
        }
        return category;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}