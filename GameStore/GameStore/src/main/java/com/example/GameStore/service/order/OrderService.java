package com.example.GameStore.service.order;

import com.example.GameStore.entity.Order;
import com.example.GameStore.entity.OrderDetail;
import com.example.GameStore.entity.OrderStatus;
import com.example.GameStore.entity.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    Order createOrder(Order order, List<OrderDetail> orderDetails);

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void deleteOrderById(Long orderId);
    Order checkoutOrder(Long orderId, PaymentMethod paymentMethod);
}
