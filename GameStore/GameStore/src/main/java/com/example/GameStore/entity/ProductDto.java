package com.example.GameStore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productName;
    private String accountName;
    private String accountPassword;
    private double price;
    private String description;
    private List<String> imageUrls;
    private Category category;
    private List<MultipartFile> images;
}
