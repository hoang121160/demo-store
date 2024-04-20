package com.example.GameStore.repository;

import com.example.GameStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
