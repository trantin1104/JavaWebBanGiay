package com.tuan1.demo.repository;

import com.tuan1.demo.model.Order;
import com.tuan1.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByEmail(String email);

    List<Order> findAllByOrderByOrderDateDesc();

    List<Order> findByUser(User user);
}
