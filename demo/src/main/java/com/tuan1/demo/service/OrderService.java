package com.tuan1.demo.service;

import com.tuan1.demo.model.*;
import com.tuan1.demo.repository.OrderDetailRepository;
import com.tuan1.demo.repository.OrderRepository;
import com.tuan1.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductService productService;


    public Order createOrder(String customerName, String shippingAddress, String phoneNumber, String email, List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng không được trống.");
        }

        validateOrderData(customerName, shippingAddress, phoneNumber, email);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));




        Order order = new Order();
        order.setCustomerName(customerName);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);

        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());

            //  Trừ số lượng sản phẩm trong kho
            productService.reduceProductQuantity(item.getProduct().getId(), item.getQuantity());

            orderDetailRepository.save(detail);
        }

        cartService.clearCart();

        return order;
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc(); // Sắp xếp mới nhất -> cũ nhất
    }

    public List<Order> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user); // Thêm phương thức này trong OrderRepository
    }

    private void validateOrderData(String customerName, String shippingAddress, String phoneNumber, String email) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ.");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ giao hàng không hợp lệ.");
        }
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10,15}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }


}
