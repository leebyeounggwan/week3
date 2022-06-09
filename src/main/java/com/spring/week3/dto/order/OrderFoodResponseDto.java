package com.spring.week3.dto.order;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFoodResponseDto {
    private String name;
    private int quantity;
    private int price;
}
