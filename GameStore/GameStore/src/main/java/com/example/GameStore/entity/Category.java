package com.example.GameStore.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String description;

}
