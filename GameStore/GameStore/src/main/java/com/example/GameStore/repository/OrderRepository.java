package com.example.GameStore.repository;

import com.example.GameStore.entity.Order;
import com.example.GameStore.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndOrderDateBeforeOrOrderStatus(OrderStatus orderStatus,
                                                                 OrderStatus orderStatus1,
                                                                 Date tanMinutesAgo);
}
