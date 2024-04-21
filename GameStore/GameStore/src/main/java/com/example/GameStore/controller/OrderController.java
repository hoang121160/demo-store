package com.example.GameStore.controller;

import com.example.GameStore.entity.Order;
import com.example.GameStore.entity.PaymentMethod;
import com.example.GameStore.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create/{productId}/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long productId, @PathVariable Long userId){
        Order order = orderService.createOrder(productId,userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam PaymentMethod paymentMethod) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, paymentMethod);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<Void> confirmationOrder(@PathVariable Long orderId) {
        orderService.confirmationOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
