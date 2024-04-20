package com.example.GameStore.service.order;

import com.example.GameStore.entity.Order;
import com.example.GameStore.entity.OrderDetail;
import com.example.GameStore.entity.OrderStatus;
import com.example.GameStore.entity.PaymentMethod;
import com.example.GameStore.repository.OrderDetailRepository;
import com.example.GameStore.repository.OrderRepository;
import com.example.GameStore.service.orderDetail.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderDetailService orderDetailService;
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public Order createOrder(Order order, List<OrderDetail> orderDetails) {
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setPaymentMethod(null);
        Order savedOrder = orderRepository.save(order);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(savedOrder);
            orderDetailService.createOrderDetail(orderDetail);
        }
        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:"));
    }


    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {


    }

    @Override
    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found");
        }
        orderRepository.deleteById(orderId);
    }


    @Override
    public Order checkoutOrder(Long orderId, PaymentMethod paymentMethod) {
        Order order = getOrderById(orderId);
        order.setPaymentMethod(paymentMethod);
        return orderRepository.save(order);
    }
}
