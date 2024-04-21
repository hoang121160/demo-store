package com.example.GameStore.service.order;

import com.example.GameStore.entity.*;
import com.example.GameStore.exeption.InvalidException;
import com.example.GameStore.repository.OrderRepository;
import com.example.GameStore.repository.ProductRepository;
import com.example.GameStore.repository.UserRepository;
import com.example.GameStore.service.emailMessage.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            EmailService emailService,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(Long productId, Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        User user = userRepository.findByUsername(currentPrincipalName)
//                .orElseThrow(()-> new EntityNotFoundException("User not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found!"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found !"));
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.CHO_THANH_TOAN);
        order.setPaymentMethod(null);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found !"));
    }

    @Override
    public Order updateOrderStatus(Long orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found !"));
        order.setOrderStatus(OrderStatus.DANG_THANH_TOAN);
        order.setPaymentMethod(paymentMethod);
        return orderRepository.save(order);
    }


    @Override
    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found !");
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public void confirmationOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found !"));
        order.setOrderStatus(OrderStatus.DA_THANH_TOAN);
        sendProductInfoToUser(order);
    }

    private void sendProductInfoToUser(Order order) {
        Product product = order.getProduct();
        User user = order.getUser();

        // send email
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(user.getEmail());
        emailMessage.setSubject("Thông tin sản phẩm đã mua");
        emailMessage.setBody("Xin chào " + user.getUsername() + ",\n\n"
                + "Dưới đây là thông tin tài khoản của sản phẩm bạn đã mua:\n\n"
                + "Tên tài khoản: " + product.getAccountName() + "\n"
                + "Mật khẩu: " + product.getAccountPassword() + "\n\n"
                + "Cảm ơn bạn đã mua hàng!\n"
                + "Trân trọng,\n"
                + "By Hoang Khong");
        emailService.sendEmail(emailMessage);
    }

}
