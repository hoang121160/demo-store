package com.example.GameStore.service.order;

import com.example.GameStore.entity.Order;
import com.example.GameStore.entity.OrderStatus;
import com.example.GameStore.entity.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order createOrder(Long productId, Long userId);

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order updateOrderStatus(Long orderId, PaymentMethod paymentMethod);

    void deleteOrderById(Long orderId);

    void confirmationOrder(Long orderId);
}
