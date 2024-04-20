package com.example.GameStore.service.product;

import com.example.GameStore.entity.Product;
import com.example.GameStore.entity.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@Service
public interface ProductService {
    List<Product> getAllProducts();

    Optional<Product> getProductById(Long productId);

    Product createProduct(ProductDto productDto, MultipartFile[] images);

    Product updateProduct(Long productId, ProductDto productDto, MultipartFile[] images);

    void deleteProduct(Long productId);

    List<Product> searchProductByName(String productName);
}
