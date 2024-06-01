package com.example.jp.services.Presentations;

import com.example.jp.model.Presentations.Category;
import com.example.jp.repositories.Presentations.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoriesRepository categoryRepository;

    public CategoryService(CategoriesRepository categoriesRepository) {
        this.categoryRepository = categoriesRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesWithPresentations() {
        return categoryRepository.findAllWithPresentations();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
