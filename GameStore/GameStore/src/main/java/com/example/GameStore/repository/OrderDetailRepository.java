package com.example.GameStore.repository;

import com.example.GameStore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail>  findByOrderOrderId(Long orderId);
}
