package com.example.GameStore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(length = 20)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
