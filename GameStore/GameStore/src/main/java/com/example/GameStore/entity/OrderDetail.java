package com.example.GameStore.entity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "OrderDetail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    private int quantity;

    private double price;

    private double totalPrice;

}
