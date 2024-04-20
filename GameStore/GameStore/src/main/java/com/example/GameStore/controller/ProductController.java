package com.example.GameStore.controller;

import com.example.GameStore.entity.Product;
import com.example.GameStore.entity.ProductDto;
import com.example.GameStore.exeption.InvalidException;
import com.example.GameStore.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService productService) {
        this.service = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = service.getProductById(productId)
                .orElseThrow(() -> new InvalidException("Product not found"));
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@ModelAttribute ProductDto productDto) {
        Product newProduct = service.createProduct(productDto, productDto.getImages().toArray(new MultipartFile[0]));
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
        MultipartFile[] images = (productDto.getImages() != null) ? productDto.getImages().toArray(new MultipartFile[0]) : null;
        Product updatedProduct = service.updateProduct(productId, productDto,images);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String productName) {
        List<Product> products = service.searchProductByName(productName);
        return ResponseEntity.ok(products);
    }


}





