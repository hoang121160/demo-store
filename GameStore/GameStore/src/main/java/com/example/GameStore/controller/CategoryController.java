package com.example.GameStore.controller;

import com.example.GameStore.entity.Category;
import com.example.GameStore.service.category.CategoryService;
import com.example.GameStore.service.category.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = service.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = service.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Category category = service.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category updateCategory) {
        Category category = service.updateCategory(categoryId,updateCategory);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Danh mục đã bị xóa !");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategory(@RequestParam String cateGoryName) {
        List<Category> categories = service.searchCategoryByName(cateGoryName);
        return ResponseEntity.ok(categories);
    }

}
















