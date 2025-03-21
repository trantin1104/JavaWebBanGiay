package com.tuan1.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;


    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "shipping_address")
    private String shippingAddress;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @PrePersist
    public void generateOrderData() {
        if (this.orderCode == null) {
            this.orderCode = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        }
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
}
