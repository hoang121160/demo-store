package com.example.GameStore.service.orderDetail;

import com.example.GameStore.entity.OrderDetail;
import com.example.GameStore.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) {
       return orderDetailRepository. findByOrderOrderId(orderId);

    }

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        double price = orderDetail.getProduct().getPrice(); // lay gia tu gia product
        double totalPrice = price * orderDetail.getQuantity(); // tinh tong gia tien
        orderDetail.setPrice(price);
        orderDetail.setTotalPrice(totalPrice);

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetailById(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Order Detail Not Found"));

    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) {
        if (!orderDetailRepository.existsById(orderDetailId)) {
            throw new EntityNotFoundException("Order Detail Not Found");
        }
        orderDetailRepository.deleteById(orderDetailId);
    }
}
