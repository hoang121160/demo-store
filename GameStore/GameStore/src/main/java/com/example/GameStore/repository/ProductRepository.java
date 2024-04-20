package com.example.GameStore.repository;

import com.example.GameStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductName(String productName);

    List<Product> findByProductNameContainingIgnoreCase(String productName);

    boolean existsByProductNameAndProductIdNot(String productName, Long productId);
}
