package com.spring.week3.controller;

import com.spring.week3.domain.Order;
import com.spring.week3.dto.order.OrderRequestDto;
import com.spring.week3.dto.order.OrderResponseDto;
import com.spring.week3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/request")
    public OrderResponseDto orderFood(
            @RequestBody OrderRequestDto ordersRequestDto
    ) {
        return orderService.saveOrder(ordersRequestDto);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> findAllOrder() {
        return orderService.findAllOrder();
    }
}
