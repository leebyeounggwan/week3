package com.spring.week3.repository;

import com.spring.week3.domain.Order;
import com.spring.week3.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findOrderDetailsByOrder(Order order);
}
