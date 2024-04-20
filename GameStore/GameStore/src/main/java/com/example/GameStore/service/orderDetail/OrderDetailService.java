package com.example.GameStore.service.orderDetail;

import com.example.GameStore.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetail();

    List<OrderDetail> getOrderDetailByOrderId(Long orderId);

    OrderDetail createOrderDetail(OrderDetail orderDetail);

    OrderDetail getOrderDetailById(Long orderDetailId);

    void deleteOrderDetail(Long orderDetailId);


}
