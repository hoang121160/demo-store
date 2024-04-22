package com.example.GameStore.service.product;

import com.example.GameStore.entity.Category;
import com.example.GameStore.entity.Product;
import com.example.GameStore.entity.ProductDto;
import com.example.GameStore.exeption.InvalidException;
import com.example.GameStore.repository.CategoryRepository;
import com.example.GameStore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "uploads";

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return Optional.ofNullable(productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm !")));
    }

    @Override
    public Product createProduct(ProductDto productDto, MultipartFile[] images) {
        validateProduct(productDto);
        Product product = convertToProduct(productDto);
        List<String> imageUrls = new ArrayList<>();
        // xử lý hình ảnh
        for (MultipartFile image : images) {
            String urlImage = saveImage(image);
            imageUrls.add(urlImage);
        }
        product.setImageUrls(imageUrls);
        return productRepository.save(product);
    }

    private Product convertToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setAccountName(productDto.getAccountName());
        product.setAccountPassword(productDto.getAccountPassword());
        product.setSold(false);
        return product;
    }

    private String saveImage(MultipartFile image) {
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        String imagePath = UPLOAD_DIR + File.separator + fileName;

        try {
            // Kiểm tra và tạo thư mục UPLOAD_DIR nếu nó chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // Lưu hình ảnh
            byte[] bytes = image.getBytes();
            Path path = Paths.get(imagePath);
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể lưu hình ảnh!");
        }

        return imagePath;
    }

    private void validateProduct(ProductDto productDto) {
        if (isNullOrEmpty(productDto.getProductName())) {
            throw new InvalidException("Tên sản phẩm không được để trống !");
        }
        if (isNullOrEmpty(productDto.getDescription())) {
            throw new InvalidException("Mô tả không được để trống !");
        }
        if (productDto.getImages() == null || productDto.getImages().isEmpty()) {
            throw new InvalidException("Danh sách ảnh không được để trống !");
        }
        if (productDto.getCategory() == null) {
            throw new InvalidException("Danh mục không được để trống !");
        }
        if (productDto.getPrice() <= 0) {
            throw new InvalidException("Giá tiền phải lớn hơn 0 !");
        }
        if (productRepository.existsByProductName(productDto.getProductName())) {
            throw new InvalidException("Tên sản phẩm đã tồn tại!");
        }
        if (!categoryRepository.existsById(productDto.getCategory().getCategoryId())) {
            throw new InvalidException("Danh mục không tồn tại trong cơ sở dữ liệu!");
        }
    }


    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

//    private boolean isNullOrEmpty(List<String> value) {
//        return value == null || value.isEmpty();
//    }


    @Override
    public Product updateProduct(Long productId, ProductDto productDto, MultipartFile[] images) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Sản phẩm không tồn tại !")));

        Product existingProduct = productOptional.get();

//        validateUpdateProduct(productDto, existingProduct);
//        // Cập nhật tên sản phẩm chỉ khi nó được cung cấp trong DTO
//        existingProduct.setProductName(productDto.getProductName());
//        existingProduct.setPrice(productDto.getPrice());
//        existingProduct.setDescription(productDto.getDescription());
//        if (productDto.getCategory() != null) {
//            existingProduct.setCategory(productDto.getCategory());
//        }
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());
        Optional<Category> category = categoryRepository.findById(productDto.getCategory().getCategoryId());
        existingProduct.setCategory(category.get());

        if (images != null && images.length > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String urlImage = saveImage(image);
                imageUrls.add(urlImage);
            }
            deleteOldImages(existingProduct);
            existingProduct.setImageUrls(imageUrls);
        }

        return productRepository.save(existingProduct);
    }

    private void validateUpdateProduct(ProductDto productDto, Product existingProduct) {
        // kiem tra tan  sp dã ton tai cho san pham khac chua
        if (productRepository.existsByProductName(productDto.getProductName()) &&
                !existingProduct.getProductName().equals(productDto.getProductName())) {
            throw new InvalidException("Tên sản phẩm đã tồn tại !");
        }
        // Kiem tra category
        if (productDto.getCategory() != null &&
                !categoryRepository.existsById(productDto.getCategory().getCategoryId())) {
            throw new InvalidException("Danh mục không tồn tại trong cơ sở dữ liệu !");
        }

        // xoa anh cu neu anh moi duoc cap nhat
        if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
            deleteOldImages(existingProduct);
        }

    }

    private void deleteOldImages(Product existingProduct) {
        List<String> oldImageUrls = existingProduct.getImageUrls();
        if (oldImageUrls != null && !oldImageUrls.isEmpty()) {
            for (String imageUrl : oldImageUrls) {
                File file = new File(imageUrl);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    private void updateProductData(Product existingProduct, ProductDto productDto) {
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setCategory(productDto.getCategory());
    }


    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new EntityNotFoundException("San pham khong ton tai !");
        }
        productRepository.delete(productOptional.get());
    }

    @Override
    public List<Product> searchProductByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }
}
