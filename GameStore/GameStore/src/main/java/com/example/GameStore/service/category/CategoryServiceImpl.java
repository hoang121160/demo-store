package com.example.GameStore.service.category;

import com.example.GameStore.entity.Category;
import com.example.GameStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục !"));
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new EntityNotFoundException("Danh mục đã tồn tại !");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, Category updateCategory) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            throw new EntityNotFoundException("Danh mục không tồn tại !");
        }
        Category existingCategory = categoryOptional.get();
        existingCategory.setCategoryName(updateCategory.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Không tìm thấy danh mục với id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> searchCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }
}
