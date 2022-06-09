package com.spring.week3.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFoodRequestDto {
    private Long id;
    private int quantity;
}
