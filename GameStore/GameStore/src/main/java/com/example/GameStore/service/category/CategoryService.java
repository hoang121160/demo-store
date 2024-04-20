package com.example.GameStore.service.category;

import com.example.GameStore.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CategoryService {

    List<Category> getAllCategory();

    Category getCategoryById(Long categoryId);

    Category createCategory(Category category);

    Category updateCategory(Long categoryId, Category category);

    void deleteCategory(Long categoryId);

    List<Category> searchCategoryByName(String categoryName);

}
